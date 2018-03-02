package be.kdg.integratieproject2.integration;


import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.Domain.Theme;
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
    private Organiser organiser;
    private ApplicationUser user1;
    private Session postedSession = null;

    @Before
    public void setup() {
        this.testTheme1 = new Theme();
        testTheme1.setDescription("Test");
        testTheme1.setName("testTheme1");
        List<Organiser> organisers1 = new ArrayList<>();
        organiser = new Organiser(true, "leander.coevoet@student.kdg.be", testTheme1.getId());
        organisers1.add(organiser);
        this.testTheme1.setOrganisers(organisers1);
        this.testTheme1 = this.themeService.addTheme(testTheme1, organiser);

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

    @Test
    public void testGetAllSessionsUser() throws ObjectNotFoundException {
        this.postedSession = this.sessionService.addSession(session, "leander.coevoet@student.kdg.be");
        List<Session> sessions = sessionService.getAllSessionsByUser("leander.coevoet@student.kdg.be");
        Assert.assertTrue(sessions.size() == 1);
        Assert.assertTrue(sessions.get(0).getSessionId().equals(postedSession.getSessionId()));
        Assert.assertTrue(sessions.get(0).getTheme().getId().equals(postedSession.getTheme().getId()));
        Assert.assertTrue(sessions.get(0).getNumberOfRounds() == postedSession.getNumberOfRounds());
    }

    @Test
    public void testDeleteSession() throws ObjectNotFoundException {
        this.postedSession = this.sessionService.addSession(session, "leander.coevoet@student.kdg.be");
        session.setCanComment(true);
        Session postedSession2 = this.sessionService.addSession(session, "leander.coevoet@student.kdg.be");
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
        Assert.assertTrue(postedSession == session);
    }
}
