package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.*;
import be.kdg.integratieproject2.Domain.verification.SessionInvitationToken;
import be.kdg.integratieproject2.api.sessionInvitation.OnSessionInvitationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.TokenService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import be.kdg.integratieproject2.data.implementations.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    ThemeService themeService;
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    SessionRepository sessionRepository;
    @Autowired
    ApplicationEventPublisher eventPublisher;


    @Override
    public Session addSession(Session session, String userId)  {
        List<String> users = new LinkedList<>();
        users.add(userId);
        session.setPlayers(users);
        session.setOrganiser(userId);
        return sessionRepository.save(session);
    }

    @Override
    public Session updateSession(Session session, String userId) throws ObjectNotFoundException, UserNotAuthorizedException {
        Session oldSession = getSession(session.getSessionId(), userId);
        if (oldSession.getOrganiser().equalsIgnoreCase(userId)){
            return sessionRepository.save(session);
        }
        else throw new UserNotAuthorizedException("User not authorized to update session");
    }

    @Override
    public Session getSession(String sessionId, String userId) throws ObjectNotFoundException, UserNotAuthorizedException {
        Session session = sessionRepository.findOne(sessionId);
        if(session == null) throw new ObjectNotFoundException(sessionId);
        for (String s : session.getPlayers()) {
            if (s.equals(userId)){
                return session;
            }
        }
        throw new UserNotAuthorizedException("User not authorized to view this session");
    }

    @Override
    public void deleteSession(String sessionId, String userId) throws ObjectNotFoundException, UserNotAuthorizedException {
        Session session = getSession(sessionId, userId);
        if (session.getOrganiser().equalsIgnoreCase(userId)){
            sessionRepository.delete(sessionId);
        }
        else{
            throw new UserNotAuthorizedException("User is not authorized to delete this Session");
        }
    }

    @Override
    public List<Session> getAllSessionsByUser(String userId) {
        List<Session> sessions = sessionRepository.findSessionsByPlayersContaining(userId);
        if (sessions == null || sessions.size() == 0) {
            return new LinkedList<>();
        }
        return sessions;
    }

    @Override
    public void addPlayerByToken(String currentUser, String token) throws ObjectNotFoundException, UserNotAuthorizedException, UserAlreadyExistsException {
        SessionInvitationToken invitationToken = tokenService.getSessionInvitationToken(token);
        String sessionId = invitationToken.getSessionId();
        String userId = invitationToken.getUserId();
        String organiser = invitationToken.getOrganiser();

        Session session = getSession(sessionId, organiser);
        List<String> users = session.getPlayers();

        if (userId.toLowerCase().equals(currentUser.toLowerCase())) {
            if (users.stream().noneMatch(s -> s.equalsIgnoreCase(userId))) {
                users.add(userId);
                sessionRepository.save(session);
            }
            else throw new UserAlreadyExistsException("User already present in Session");
        }
        else {
            throw new UserNotAuthorizedException("User does not have access to this token");
        }
    }

    @Override
    public SessionState getSessionState(String username, String sessionId) throws ObjectNotFoundException, UserNotAuthorizedException {
        return new SessionState(getSession(sessionId,username));
    }

    @Override
    public SessionState getSessionStateByDate(String username, String sessionId, Date date) throws ObjectNotFoundException, UserNotAuthorizedException {
        Session session = getSession(sessionId, username);
        List<Turn> newTurns = session.getTurns().stream().filter(x -> x.getTimestamp().after(date)).collect(Collectors.toList());
        session.setTurns(newTurns);
        return new SessionState(session);
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
    public Session addTurnToSession(Turn turn, String username, String sessionId) throws UserNotAuthorizedException, ObjectNotFoundException {
        if(!turn.getUserId().equalsIgnoreCase(username)) throw new UserNotAuthorizedException("Unable to add turn for another user");
        SessionState state = getSessionState(username, sessionId);
        if(!state.getNextPlayer().equalsIgnoreCase(username)) throw new UserNotAuthorizedException("It is not your turn");
        Session session = getSession(sessionId, username);
        List<Turn> turns = session.getTurns() == null ? new ArrayList<>() :  session.getTurns();
        turns.add(turn);
        session.setTurns(turns);
        return sessionRepository.save(session);
    }

    @Override
    public void invitePlayers(List<String> players, String username, String sessionId, String appUrl, Locale locale) throws UserAlreadyExistsException, UserNotAuthorizedException, ObjectNotFoundException {
        Session session = getSession(sessionId, username);
        if (!session.getOrganiser().equalsIgnoreCase(username)) throw new UserNotAuthorizedException("User is not authorized to invite players");
        ApplicationUser user;
        for (String player : players) {
            try {
                user = userService.getUserByUsername(player);
                if (user.getEmail() != null) {
                    eventPublisher.publishEvent(new OnSessionInvitationCompleteEvent(user, appUrl, locale, player, username, sessionId, session.getSessionName()));

                }
            } catch (UsernameNotFoundException a) {
                ApplicationUser newUser = new ApplicationUser();
                newUser.setEmail(player);
                eventPublisher.publishEvent(new OnSessionInvitationCompleteEvent(userService.registerUser(newUser), appUrl, locale, username, player, sessionId, session.getSessionName()));
            }
        }
    }
}
