package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.data.implementations.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    ThemeService themeService;
    UserService userService;
    SessionRepository sessionRepository;

    public SessionServiceImpl(ThemeService themeService, UserService userService, SessionRepository sessionRepository) {
        this.themeService = themeService;
        this.userService = userService;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session addSession(Session session, String userId) throws ObjectNotFoundException {
        List<String> users = new LinkedList<>();
        users.add(userId);
        session.setPlayers(users);
        session.setOrganiser(userId);
       /* if (!user.getThemes().contains(session.getTheme().getId())) {
            throw new ObjectNotFoundException("Thema not authorized");
        }*/
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
}
