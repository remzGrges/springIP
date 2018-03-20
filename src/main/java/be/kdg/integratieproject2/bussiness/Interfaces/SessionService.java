package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.Domain.SessionState;
import be.kdg.integratieproject2.Domain.Turn;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public interface SessionService {
    Session addSession(Session session, String userId) throws ObjectNotFoundException;

    Session updateSession(Session session, String userId) throws ObjectNotFoundException, UserNotAuthorizedException;

    Session getSession(String sessionId, String userId) throws ObjectNotFoundException, UserNotAuthorizedException;

    void deleteSession(String sessionId, String userId) throws ObjectNotFoundException, UserNotAuthorizedException;

    List<Session> getAllSessionsByUser(String userId) throws ObjectNotFoundException;

    void addPlayerByToken(String sessionId, String themeId) throws ObjectNotFoundException, UserNotAuthorizedException, UserAlreadyExistsException;

    SessionState getSessionState(String username, String sessionId) throws ObjectNotFoundException, UserNotAuthorizedException;

    Session addTurnToSession(Turn turn, String username, String sessionId) throws UserNotAuthorizedException, ObjectNotFoundException;

    void invitePlayers(List<String> players, String username, String sessionId, String appUrl, Locale locale) throws UserAlreadyExistsException, UserNotAuthorizedException, ObjectNotFoundException;
}
