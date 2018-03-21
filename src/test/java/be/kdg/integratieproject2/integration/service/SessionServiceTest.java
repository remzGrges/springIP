package be.kdg.integratieproject2.integration.service;


import be.kdg.integratieproject2.Domain.*;
import be.kdg.integratieproject2.Domain.verification.SessionInvitationToken;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import be.kdg.integratieproject2.data.implementations.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class SessionServiceTest {
    @Autowired
    SessionService sessionService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    ThemeService themeService;
    @Autowired
    UserService userService;

    Session session;
    private ApplicationUser user1;
    private Session postedSession = null;

    @Before
    public void setup() {
        user1 = userService.getUserByUsername("leander.coevoet@student.kdg.be");
        List<String> players = new LinkedList<>();
        players.add(user1.getEmail());

        session = new Session();
        session.setThemeId("");
        session.setAddCardUser(false);
        session.setCanComment(false);
        session.setNumberOfRounds(0);
        session.setPlayers(players);
        session.setTimeUserRound(5);
        session.setStartTime(new Timestamp(2018, 3, 12, 12, 0, 0, 0));
    }

    @After
    public void delete() throws UserNotAuthorizedException {
        tokenRepository.deleteAll();
        userRepository.deleteByEmail("fake@mail.be");
        try {
            if (postedSession != null) {
                sessionService.deleteSession(postedSession.getSessionId(), "leander.coevoet@student.kdg.be");
            }

        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateSession() throws ObjectNotFoundException {
        this.postedSession = this.sessionService.addSession(session, "leander.coevoet@student.kdg.be");
        Assert.assertTrue(postedSession.getNumberOfRounds() == session.getNumberOfRounds());
        Assert.assertTrue(postedSession.getPlayers().size() == session.getPlayers().size());
        Assert.assertTrue(postedSession.getStartTime() == session.getStartTime());
        Assert.assertTrue(postedSession.getTimeUserRound() == session.getTimeUserRound());
    }

    @Test
    public void testGetSession() throws ObjectNotFoundException, UserNotAuthorizedException {
        this.postedSession = this.sessionService.addSession(session, "leander.coevoet@student.kdg.be");
        Session session = this.sessionService.getSession(postedSession.getSessionId(), "leander.coevoet@student.kdg.be");
        Assert.assertTrue(postedSession.getNumberOfRounds() == session.getNumberOfRounds());
        Assert.assertTrue(postedSession.getSessionId().equals(session.getSessionId()));
        Assert.assertTrue(postedSession.getPlayers().size() == session.getPlayers().size());
    }

    @Test
    public void testUpdateSession() throws ObjectNotFoundException {
        this.postedSession = this.sessionService.addSession(session, "leander.coevoet@student.kdg.be");
        postedSession.setCanComment(true);
        Session updatedSession = this.sessionService.addSession(postedSession, "leander.coevoet@student.kdg.be");
        Assert.assertTrue(postedSession.getSessionId().equals(updatedSession.getSessionId()));
        Assert.assertTrue(postedSession.isCanComment() == updatedSession.isCanComment());
    }


}
