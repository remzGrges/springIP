package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.SessionDto;
import be.kdg.integratieproject2.api.sessionInvitation.OnSessionInvitationCompleteEvent;
import be.kdg.integratieproject2.api.sessionInvitation.OnSessionInvitationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.PlayersNotReadyException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private ModelMapper modelMapper;
    private SessionService sessionService;
    private UserService userService;
    private ApplicationEventPublisher eventPublisher;


    public SessionController(ModelMapper modelMapper, SessionService sessionService, UserService userService, ApplicationEventPublisher eventPublisher) {
        this.modelMapper = modelMapper;
        this.sessionService = sessionService;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<SessionDto> updateSession(Authentication authentication, @Valid @RequestBody SessionDto dto) throws BadRequestException {
        Session session = modelMapper.map(dto, Session.class);
        SessionDto mappedSession = null;
        Session getSes = null;
        try {
            getSes = sessionService.getSession(session.getSessionId(), authentication.getName());
            if (getSes == null) {
                throw new BadRequestException("No valid Session");
            }
            mappedSession = modelMapper.map(sessionService.updateSession(session, authentication.getName()), SessionDto.class);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }

        return new ResponseEntity<>(mappedSession, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<SessionDto> createSession(@RequestBody SessionDto dto, Authentication authentication) throws BadRequestException, ObjectNotFoundException {
        Session session = modelMapper.map(dto, Session.class);
        SessionDto mappedSession = null;
        mappedSession = modelMapper.map(sessionService.addSession(session, authentication.getName()), SessionDto.class);
        return new ResponseEntity<>(mappedSession, HttpStatus.OK);
    }

    @RequestMapping(value = "/getSession/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity<SessionDto> getSession(Authentication authentication, @PathVariable String sessionId) {
        Session session;

        try {
            session = sessionService.getSession(sessionId, authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException("Session not found");
        }
        SessionDto sessionDto = modelMapper.map(session, SessionDto.class);
        return new ResponseEntity<SessionDto>(sessionDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity deleteCard(Authentication authentication, @PathVariable String sessionId) {
        try {
            sessionService.deleteSession(sessionId, authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<SessionDto>> getAllSessionsByUser(Authentication authentication) {
        List<Session> sessions;

        try {
            sessions = sessionService.getAllSessionsByUser(authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        List<SessionDto> sessionDtos = new LinkedList<>();
        for (Session session : sessions) {
            sessionDtos.add(modelMapper.map(session, SessionDto.class));
        }
        return new ResponseEntity<>(sessionDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/invitePlayers/{sessionId}", method = RequestMethod.POST)
    public ResponseEntity inviteOrganiser(Authentication authentication, @RequestBody List<String> players, @PathVariable("sessionId") String sessionId, BindingResult result, WebRequest request) throws UserAlreadyExistsException, ObjectNotFoundException {
        ApplicationUser user;
        String appUrl = request.getContextPath();
        //Session session = sessionService.getSession(sessionId, authentication.getName());


        // if (session.getOrganiser().equals(authentication.getName())) {
        for (String player : players) {
            try {
                user = userService.getUserByUsername(player);
                if (user.getEmail() != null) {
                    eventPublisher.publishEvent(new OnSessionInvitationCompleteEvent(user, appUrl, request.getLocale(), player, authentication.getName(), sessionId));

                }
            } catch (UsernameNotFoundException a) {
                ApplicationUser newUser = new ApplicationUser();
                newUser.setEmail(player);
                eventPublisher.publishEvent(new OnSessionInvitationCompleteEvent(userService.registerUser(newUser), appUrl, request.getLocale(), authentication.getName(), player, sessionId));

            }
        }
        //}



   /*     try {
            user = userService.getUserByUsername(email);
            if (user.getEmail() != null) {
                eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.getUserByUsername(email), request.getLocale(), appUrl, themeId));
            }
        } catch (UsernameNotFoundException a) {
            ApplicationUser newUser = new ApplicationUser();
            newUser.setEmail(email);
            eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.registerUser(newUser), request.getLocale(), appUrl, themeId));

        }*/
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/acceptInvite/{token}", method = RequestMethod.GET)
    public ResponseEntity acceptInvite(Authentication authentication, @PathVariable("token") String token) throws ObjectNotFoundException, UserAlreadyExistsException {
        String name = authentication.getName();
        sessionService.addPlayer(authentication.getName(), token);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/acceptSessionInviteNon/{token}", method = RequestMethod.GET)
    public ResponseEntity acceptInviteNon(@RequestParam("email") String email, @PathVariable("token") String token) throws ObjectNotFoundException, UserAlreadyExistsException {

        sessionService.addPlayer(email, token);
        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/goReady/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity goReady(Authentication authentication, @PathVariable("sessionID") String sessionId) throws ObjectNotFoundException {
        sessionService.setPlayerReady(authentication.getName(), sessionId);

        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value = "/nextState/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity nextState(Authentication authentication , @PathVariable("sessionId") String sessionId) throws PlayersNotReadyException, ObjectNotFoundException {
        sessionService.nextState(authentication.getName(), sessionId);
        return new ResponseEntity(HttpStatus.OK);
    }



}
