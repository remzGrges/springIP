package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.InputMessage;
import be.kdg.integratieproject2.Domain.OutputMessage;
import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.Domain.verification.SessionInvitationToken;
import be.kdg.integratieproject2.bussiness.Interfaces.*;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.data.implementations.SessionRepository;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    ThemeService themeService;
    UserService userService;
    SessionRepository sessionRepository;
    TokenRepository tokenRepository;

    public SessionServiceImpl(ThemeService themeService, UserService userService, SessionRepository sessionRepository, TokenRepository repository) {
        this.themeService = themeService;
        this.userService = userService;
        this.tokenRepository= repository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session addSession(Session session, String userId)  {
        List<String> users = new LinkedList<>();
        users.add(userId);
        session.setPlayers(users);
        session.setOrganiser(userId);
       return sessionRepository.save(session);
    }

    @Override
    public Session updateSession(Session session, String userId)
    {
        return sessionRepository.save(session);
    }

    @Override
    public Session getSession(String sessionId, String userId) throws ObjectNotFoundException {
        Session session = sessionRepository.findOne(sessionId);
        for (String s : session.getPlayers()) {
            if (s.equals(userId)){
                return session;
            }
        }
        throw new ObjectNotFoundException(sessionId);
    }

    @Override
    public void deleteSession(String sessionId, String userId) throws ObjectNotFoundException {
        ApplicationUser user = userService.getUserByUsername(userId);
        Session session = sessionRepository.findOne(sessionId);
            if (session.getOrganiser().equals(user.getEmail())){
                sessionRepository.delete(sessionId);
            }
    }

    @Override
    public List<Session> getAllSessionsByUser(String userId) throws ObjectNotFoundException {
        List<Session> sessions = sessionRepository.findSessionsByPlayersContaining(userId);
        if (sessions == null || sessions.size() == 0) {
            return new LinkedList<>();
        }
        return sessions;
    }

    @Override
    public void addPlayer(String ingelogdeUser, String token) throws ObjectNotFoundException {
        SessionInvitationToken invitationToken = this.getSessionInvitationToken(token);
        if (invitationToken == null) {
            throw new ObjectNotFoundException("geen invitationToken");
        }
        String sessionId = invitationToken.getSessionId();
        String userId = invitationToken.getUserId();
        String organiser = invitationToken.getOrganiser();

        Session session = getSession(sessionId, organiser);
        List<String> users = session.getPlayers();

        if (userId.equals(ingelogdeUser)) {
            users.add(userId);

            sessionRepository.save(session);
        }
    }

    @Override
    public SessionInvitationToken getSessionInvitationToken(String token) {
        return (SessionInvitationToken) tokenRepository.findByToken(token);
    }

    @Override
    public OutputMessage addMessageToSession(String sessionId, InputMessage message) {
        OutputMessage oMessage = new OutputMessage(
                message.getFrom(),
                message.getText(),
                new SimpleDateFormat("dd-MM HH:mm").format(new Date()));
        Session session = sessionRepository.findOne(sessionId);
        List<OutputMessage> messages = session.getMessages();
        messages.add(oMessage);
        session.setMessages(messages);
        sessionRepository.save(session);
        return oMessage;
    }


    @Override
    public void createSessionInvitationToken(String email, String sessionId, String token, String organiser) {
        tokenRepository.save(new SessionInvitationToken(token, sessionId,email, organiser));
    }
}
