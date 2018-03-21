package be.kdg.integratieproject2.integration.service;

import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class ThemeServicesTest {

    @Autowired
    private ThemeService themeService;

    private Theme testTheme1;
    private Theme testTheme2;
    private Theme testTheme3;
    private Theme postedTheme1;
    private Theme postedTheme2;
    private Theme postedTheme3;
    private String organiser1;
    private String organiser2;

    //private Object o;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.testTheme1 = new Theme();
        this.testTheme1.setName("testTheme1");
        this.testTheme1.setDescription("This is a test class");
        List<String> organisers1 = new ArrayList<>();
        organiser1 = "tim.vanaelst@student.kdg.be";
        organisers1.add(organiser1);
        this.testTheme1.setOrganisers(organisers1);

        this.testTheme2 = new Theme();
        this.testTheme2.setName("testTheme2");
        this.testTheme2.setDescription("This is a second test class");
        this.testTheme2.setOrganisers(organisers1);

        this.testTheme3 = new Theme();
        this.testTheme3.setName("testTheme3");
        this.testTheme3.setDescription("This is a third test class");
        List<String> organisers2 = new ArrayList<>();
        organiser2 = "indy.dewacker@student.kdg.be";
        organisers2.add(organiser2);
        this.testTheme3.setOrganisers(organisers2);


        // this.gson = new Gson();
        //this.o = new StringBuilder();
    }

    @Test
    public void getTheme() throws ObjectNotFoundException, UserNotAuthorizedException {
        this.postedTheme1 = this.themeService.addTheme(testTheme1, organiser1);
        Theme theme = null;
        theme = themeService.getTheme(postedTheme1.getId(), organiser1);
        Assert.assertTrue(theme.getName().equals("testTheme1"));
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getThemeBadId() throws ObjectNotFoundException, UserNotAuthorizedException {
        this.postedTheme1 = this.themeService.addTheme(testTheme1, organiser1);
        Theme theme = themeService.getTheme("WAHt", organiser1);
    }

    @Test
    public void testCreateTheme() throws ObjectNotFoundException, UserNotAuthorizedException {
        Theme postedTheme = this.themeService.addTheme(testTheme1, organiser1);
        Assert.assertTrue(postedTheme.getName().equals(testTheme1.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme1.getDescription()));
        this.postedTheme1 = postedTheme;

        Theme postedTheme1 = this.themeService.getTheme(this.postedTheme1.getId(), organiser1);
        Assert.assertTrue(postedTheme1.getName().equals(this.postedTheme1.getName()));
        Assert.assertTrue(postedTheme1.getDescription().equals(this.postedTheme1.getDescription()));
        for (String o : postedTheme1.getOrganisers()) {
            Assert.assertTrue(o.equals(organiser1));
        }
    }

    @Test
    public void testCreateSecondTheme() throws ObjectNotFoundException, UserNotAuthorizedException {
        Theme postedTheme = this.themeService.addTheme(testTheme2, organiser1);
        Assert.assertTrue(postedTheme.getName().equals(testTheme2.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme2.getDescription()));
        this.postedTheme2 = postedTheme;


        Theme postedTheme2 = this.themeService.getTheme(this.postedTheme2.getId(), organiser1);
        Assert.assertTrue(postedTheme2.getName().equals(this.postedTheme2.getName()));
        Assert.assertTrue(postedTheme2.getDescription().equals(this.postedTheme2.getDescription()));
        // Assert.assertTrue(postedTheme2.getOrganisers().contains(new Organiser(true , "tim.vanaelst@student.kdg.be", testTheme3.getId())));
        for (String o : postedTheme2.getOrganisers()) {
            Assert.assertTrue(o.equals(organiser1));


        }

    }

    @Test()
    public void testCreateThemeNoOrganisers() throws ObjectNotFoundException, UserNotAuthorizedException {
        testTheme1.setOrganisers(new LinkedList<>());
        Theme postedTheme = this.themeService.addTheme(testTheme1, organiser1);
        Assert.assertTrue(postedTheme.getName().equals(testTheme1.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme1.getDescription()));
        this.postedTheme1 = postedTheme;

        Theme postedTheme1 = this.themeService.getTheme(this.postedTheme1.getId(), organiser1);
        Assert.assertTrue(postedTheme1.getName().equals(this.postedTheme1.getName()));
        Assert.assertTrue(postedTheme1.getDescription().equals(this.postedTheme1.getDescription()));
        //Assert.assertTrue(postedTheme1.getOrganisers().contains(organiser1));
    }

    @Test
    public void testSeperateUser() throws ObjectNotFoundException, UserNotAuthorizedException {
        Theme postedTheme = this.themeService.addTheme(testTheme3, organiser2);
        Assert.assertTrue(postedTheme.getName().equals(testTheme3.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme3.getDescription()));
        this.postedTheme3 = postedTheme;


        Theme postedTheme3 = this.themeService.getTheme(this.postedTheme3.getId(), organiser2);
        Assert.assertTrue(postedTheme3.getName().equals(this.postedTheme3.getName()));
        Assert.assertTrue(postedTheme3.getDescription().equals(this.postedTheme3.getDescription()));

        //Organiser organiser = new Organiser(true , "indy.dewacker@student.kdg.be", testTheme3.getId());
        for (String o : postedTheme3.getOrganisers()) {


            Assert.assertTrue(o.equals(organiser2));


        }

    }

    @Test(expected = UsernameNotFoundException.class)
    public void testWrongUserName() {
        this.themeService.addTheme(testTheme1, "rezm@stud.kdg.be");
    }

    @Test
    public void isOrganiser() throws ObjectNotFoundException, UserNotAuthorizedException {
        this.postedTheme1 = this.themeService.addTheme(testTheme1, organiser1);
        boolean isOrg = themeService.isOrganiser("tim.vanaelst@student.kdg.be", this.postedTheme1.getId());
        Assert.assertTrue(isOrg);
    }

    @Test
    public void isNotOrganiser() throws ObjectNotFoundException, UserNotAuthorizedException {
        this.postedTheme1 = this.themeService.addTheme(testTheme1, organiser1);
        boolean isOrg = themeService.isOrganiser("leander.coevoet@student.kdg.be", this.postedTheme1.getId());
        Assert.assertFalse(isOrg);
    }


    @After
    public void deleteThemes() throws ObjectNotFoundException, UserNotAuthorizedException {
        if (this.postedTheme1 != null) themeService.deleteTheme(this.postedTheme1.getId(), organiser1);
        if (this.postedTheme2 != null) themeService.deleteTheme(this.postedTheme2.getId(), organiser1);
        if (this.postedTheme3 != null) themeService.deleteTheme(this.postedTheme3.getId(), organiser2);

        if (this.postedTheme1 != null && this.postedTheme2 != null && this.postedTheme3 != null) {
            List<Theme> themes = this.themeService.getThemesByUser("tim.vanaelst@student.kdg.be");
            Assert.assertTrue(themes.size() == 0);

            themes = this.themeService.getThemesByUser("indy.dewacker@student.kdg.be");
            Assert.assertTrue(themes.size() == 0);
        }
    }

}
