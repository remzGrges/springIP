package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.Domain.Turn;
import be.kdg.integratieproject2.api.dto.SessionDto;
import be.kdg.integratieproject2.api.dto.SessionStateDto;
import be.kdg.integratieproject2.api.dto.TurnDto;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.PlayersNotReadyException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Date;
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
    public ResponseEntity<SessionDto> updateSession(Authentication authentication, @Valid @RequestBody SessionDto dto) throws ObjectNotFoundException, UserNotAuthorizedException {
        Session session = modelMapper.map(dto, Session.class);
        SessionDto mappedSession;
        mappedSession = modelMapper.map(sessionService.updateSession(session, authentication.getName()), SessionDto.class);
        return new ResponseEntity<>(mappedSession, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<SessionDto> createSession(@RequestBody SessionDto dto, Authentication authentication) throws ObjectNotFoundException {
        Session session = modelMapper.map(dto, Session.class);
        SessionDto mappedSession = null;
        mappedSession = modelMapper.map(sessionService.addSession(session, authentication.getName()), SessionDto.class);
        return new ResponseEntity<>(mappedSession, HttpStatus.OK);
    }

    @RequestMapping(value = "/getSession/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity<SessionDto> getSession(Authentication authentication, @PathVariable String sessionId) throws ObjectNotFoundException, UserNotAuthorizedException {
        Session session;
        session = sessionService.getSession(sessionId, authentication.getName());
        SessionDto sessionDto = modelMapper.map(session, SessionDto.class);
        return new ResponseEntity<SessionDto>(sessionDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity deleteCard(Authentication authentication, @PathVariable String sessionId) throws ObjectNotFoundException, UserNotAuthorizedException {
        sessionService.deleteSession(sessionId, authentication.getName());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<SessionDto>> getAllSessionsByUser(Authentication authentication) throws ObjectNotFoundException {
        List<Session> sessions;
        sessions = sessionService.getAllSessionsByUser(authentication.getName());
        List<SessionDto> sessionDtos = new LinkedList<>();
        for (Session session : sessions) {
            sessionDtos.add(modelMapper.map(session, SessionDto.class));
        }
        return new ResponseEntity<>(sessionDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllByTheme/{themeId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<SessionDto>> getAllSessionsByUserAndTheme(@PathVariable("themeId") String themeId, Authentication authentication) throws ObjectNotFoundException {
        List<Session> sessions;

        sessions = sessionService.getAllSessionsByUserAndTheme(authentication.getName(),themeId);
        List<SessionDto> sessionDtos = new LinkedList<>();
        for (Session session : sessions) {
            sessionDtos.add(modelMapper.map(session, SessionDto.class));
        }
        return new ResponseEntity<>(sessionDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/invitePlayers/{sessionId}", method = RequestMethod.POST)
    public ResponseEntity invitePlayers(Authentication authentication, @RequestBody List<String> players, @PathVariable("sessionId") String sessionId, BindingResult result, WebRequest request) throws UserAlreadyExistsException, ObjectNotFoundException, UserNotAuthorizedException {
        ApplicationUser user;
        String appUrl = request.getContextPath();
        sessionService.invitePlayers(players, authentication.getName(), sessionId, appUrl, request.getLocale());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/acceptInvite/{token}", method = RequestMethod.GET)
    public ResponseEntity acceptInvite(Authentication authentication, @PathVariable("token") String token) throws ObjectNotFoundException, UserAlreadyExistsException, UserNotAuthorizedException {
        sessionService.addPlayerByToken(authentication.getName(), token);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/acceptSessionInviteNon/{token}", method = RequestMethod.GET)
    public ResponseEntity acceptInviteNon(@RequestParam("email") String email, @PathVariable("token") String token) throws ObjectNotFoundException, UserAlreadyExistsException, UserNotAuthorizedException {
        sessionService.addPlayerByToken(email, token);
        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/getSessionState/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity<SessionStateDto> getSessionState(@PathVariable String sessionId, Authentication authentication) throws ObjectNotFoundException, UserNotAuthorizedException {
        SessionStateDto dto = modelMapper.map(sessionService.getSessionState(authentication.getName(), sessionId), SessionStateDto.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value ="/getSessionState/{sessionId}/{date}", method = RequestMethod.POST)
    public ResponseEntity<SessionStateDto> getSessionStateByDate(@PathVariable String sessionId, @RequestBody Date date, Authentication authentication) throws UserNotAuthorizedException, ObjectNotFoundException {
        SessionStateDto dto = modelMapper.map(sessionService.getSessionStateByDate(authentication.getName(), sessionId, date), SessionStateDto.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @RequestMapping(value="/addTurn/{sessionId}", method = RequestMethod.POST)
    public ResponseEntity<SessionDto> addTurnToSession(@Valid @RequestBody TurnDto dto, @PathVariable String sessionId, Authentication authentication) throws UserNotAuthorizedException, ObjectNotFoundException {
        Turn turn = modelMapper.map(dto, Turn.class);
         Session session = sessionService.addTurnToSession(turn, authentication.getName(), sessionId);
        return new ResponseEntity<>(modelMapper.map(session, SessionDto.class), HttpStatus.OK);
    }
}
