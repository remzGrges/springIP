package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.SessionDto;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private ModelMapper modelMapper;
    private SessionService sessionService;

    public SessionController(ModelMapper modelMapper, SessionService sessionService) {
        this.modelMapper = modelMapper;
        this.sessionService = sessionService;
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
            mappedSession = modelMapper.map(sessionService.addSession(session, authentication.getName()), SessionDto.class);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }

        return new ResponseEntity<>(mappedSession, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<SessionDto> createSession(@RequestBody SessionDto dto,  Authentication authentication) throws BadRequestException {
        Session session = modelMapper.map(dto, Session.class);
        SessionDto mappedSession = null;

        try {
            mappedSession = modelMapper.map(sessionService.addSession(session, authentication.getName()), SessionDto.class);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
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

}
