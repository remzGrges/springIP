package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Application;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.bussiness.Interfaces.*;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.data.implementations.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    ThemeService themeService;
    SubThemeService subThemeService;
    CardService cardService;
    UserService userService;
    SessionRepository sessionRepository;

    public SessionServiceImpl(ThemeService themeService, SubThemeService subThemeService, CardService cardService, UserService userService, SessionRepository sessionRepository) {
        this.themeService = themeService;
        this.subThemeService = subThemeService;
        this.cardService = cardService;
        this.userService = userService;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session addSession(Session session, String userId) throws ObjectNotFoundException {
        ApplicationUser user = userService.getUserByUsername(userId);
        if (!user.getThemes().contains(session.getTheme().getId())) {
            throw new ObjectNotFoundException("Thema not authorized");
        }
        Session savedSession = sessionRepository.save(session);
        return savedSession;
    }

    @Override
    public Session getSession(String sessionId, String userId) throws ObjectNotFoundException {
        ApplicationUser user = userService.getUserByUsername(userId);
        Session session = sessionRepository.findOne(sessionId);
        for (ApplicationUser applicationUser : session.getPlayers()) {
            if (applicationUser.getId().equals(user.getId())) {
                return session;
            }
        }
        throw new ObjectNotFoundException(sessionId);
    }

    @Override
    public void deleteSession(String sessionId, String userId) throws ObjectNotFoundException {
        ApplicationUser user = userService.getUserByUsername(userId);
        List<Organiser> organisers = sessionRepository.findOne(sessionId).getTheme().getOrganisers();
        for (Organiser organiser : organisers) {
            if (organiser.getEmail().equals(user.getEmail()) && organiser.getEnabled()) {
                sessionRepository.delete(sessionId);
            }
        }
    }

    @Override
    public List<Session> getAllSessionsByUser(String userId) throws ObjectNotFoundException {
        ApplicationUser user = userService.getUserByUsername(userId);
        List<Session> sessions = sessionRepository.findSessionsByPlayersContaining(user);
        if (sessions == null || sessions.size() == 0) {
            return new LinkedList<>();
        }
        return sessions;
    }
}
