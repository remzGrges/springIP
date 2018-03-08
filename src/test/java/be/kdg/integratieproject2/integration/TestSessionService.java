package be.kdg.integratieproject2.integration;


import be.kdg.integratieproject2.Domain.*;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
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
public class TestSessionService {
    @Autowired
    SessionService sessionService;
    @Autowired
    ThemeService themeService;
    @Autowired
    UserService userService;

    Session session;
    private Theme testTheme1;
    private ApplicationUser user1;
    private Session postedSession = null;

    @Before
    public void setup() {
        this.testTheme1 = new Theme();
        testTheme1.setDescription("Test");
        testTheme1.setName("testTheme1");
        List<String> organisers1 = new ArrayList<>();
        organisers1.add("leander.coevoet@student.kdg.be");
        this.testTheme1.setOrganisers(organisers1);
        this.testTheme1 = this.themeService.addTheme(testTheme1, "leander.coevoet@student.kdg.be");

        user1 = userService.getUserByUsername("leander.coevoet@student.kdg.be");
        List<ApplicationUser> players = new LinkedList<>();
        players.add(user1);

        session = new Session();
        session.setTheme(testTheme1);
        session.setAddCardUser(false);
        session.setCanComment(false);
        session.setNumberOfRounds(0);
        session.setPlayers(players);
        session.setTimeUserRound(5);
        session.setStartTime(new Timestamp(2018, 3, 12, 12, 0, 0, 0));
    }

    @After
    public void delete() {
        try {
            themeService.deleteTheme(this.testTheme1.getId());

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
        Assert.assertTrue(postedSession.getTheme() == session.getTheme());
        Assert.assertTrue(postedSession.getTimeUserRound() == session.getTimeUserRound());
    }

    /*@Test
    public void testGetAllSessionsUser() throws ObjectNotFoundException {
        this.postedSession = this.sessionService.addSession(session, "leander.coevoet@student.kdg.be");
        List<Session> sessions = sessionService.getAllSessionsByUser("leander.coevoet@student.kdg.be");
        Assert.assertTrue(sessions.size() == 1);
        Session ses = this.sessionService.addSession(session, "leander.coevoet@student.kdg.be");
        sessions = sessionService.getAllSessionsByUser("leander.coevoet@student.kdg.be");
        //Assert.assertTrue(sessions.size() == 2); //TODO: sessions gaan niet altijd 2 zijn
        sessionService.deleteSession(ses.getSessionId(), "leander.coevoet@student.kdg.be");
        Assert.assertTrue(sessions.get(0).getSessionId().equals(postedSession.getSessionId()));
        Assert.assertTrue(sessions.get(0).getTheme().getId().equals(postedSession.getTheme().getId()));
        Assert.assertTrue(sessions.get(0).getNumberOfRounds() == postedSession.getNumberOfRounds());
    } */

    @Test
    public void testDeleteSession() throws ObjectNotFoundException {
        Session session2 = new Session();
        session2.setTheme(testTheme1);
        session2.setAddCardUser(false);
        session2.setNumberOfRounds(0);
        user1 = userService.getUserByUsername("leander.coevoet@student.kdg.be");
        List<ApplicationUser> players = new LinkedList<>();
        players.add(user1);
        session2.setPlayers(players);
        session2.setTimeUserRound(5);
        session2.setStartTime(new Timestamp(2018, 3, 12, 12, 0, 0, 0));
        session2.setCanComment(true);

        this.postedSession = this.sessionService.addSession(session, "leander.coevoet@student.kdg.be");
        Session postedSession2 = this.sessionService.addSession(session2, "leander.coevoet@student.kdg.be");
        List<Session> sessions = sessionService.getAllSessionsByUser("leander.coevoet@student.kdg.be");
        Assert.assertTrue(sessions.size() == 2);
        sessionService.deleteSession(postedSession2.getSessionId(), "leander.coevoet@student.kdg.be");
        sessions = sessionService.getAllSessionsByUser("leander.coevoet@student.kdg.be");
        Assert.assertTrue(sessions.size() == 1);
    }

    @Test
    public void testGetSession() throws ObjectNotFoundException {
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
