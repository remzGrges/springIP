package be.kdg.integratieproject2.integration;

import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.controllers.ThemeController;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
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
import org.springframework.web.context.WebApplicationContext;

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
public class TestThemeServices {
    @Autowired
    private ThemeController themeController;

    @Autowired
    private WebApplicationContext wac;

    /*
    private MockMvc mvc;
    private Gson gson;
    @Autowired
    private ModelMapper modelMapper;
    */

    @Autowired
    private ThemeService themeService;

    private Theme testTheme1;
    private Theme testTheme2;
    private Theme testTheme3;
    private Theme postedTheme1;
    private Theme postedTheme2;
    private Theme postedTheme3;
    private Organiser organiser1;
    private Organiser organiser2;

    //private Object o;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.testTheme3 = new Theme();
        organiser2 = new Organiser(true, "indy.dewacker@student.kdg.be", testTheme3.getId());

        //this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.testTheme1 = new Theme();
        this.testTheme1.setName("testTheme1");
        this.testTheme1.setDescription("This is a test class");
        List<Organiser> organisers1 = new ArrayList<>();
        organiser1 = new Organiser(true, "tim.vanaelst@student.kdg.be", testTheme1.getId());
        organisers1.add(organiser1);
        organisers1.add(organiser2);
        this.testTheme1.setOrganisers(organisers1);

        this.testTheme2 = new Theme();
        this.testTheme2.setName("testTheme2");
        this.testTheme2.setDescription("This is a second test class");
        this.testTheme2.setOrganisers(organisers1);


        this.testTheme3.setName("testTheme3");
        this.testTheme3.setDescription("This is a third test class");
        List<Organiser> organisers2 = new ArrayList<>();
        organisers2.add(organiser2);
        this.testTheme3.setOrganisers(organisers2);
        postedTheme1 = this.themeService.addTheme(testTheme1, organiser1.getEmail());
        postedTheme1.setId("testId");

        // this.gson = new Gson();
        //this.o = new StringBuilder();
    }
    /*@Test
    public void testControllerCreateTheme() throws Exception {
       MvcResult result = mvc.perform(post("/themes/create")
                .content(gson.toJson(testTheme1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Theme postedTheme = gson.fromJson(result.getResponse().getContentAsString(), Theme.class);
        Assert.assertTrue(this.testTheme1.getName().equals(themeService.getTheme(postedTheme.getId()).getName()));
    }

    @Test
    public void testControllerWrongClass() throws Exception
    {
        mvc.perform(post("/themes/create")
                .content(gson.toJson(o))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }*/


    @Test
    public void testCreateTheme() throws ObjectNotFoundException {
        Theme postedTheme = this.themeService.addTheme(testTheme1, organiser1.getEmail());
        Assert.assertTrue(postedTheme.getName().equals(testTheme1.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme1.getDescription()));
        this.postedTheme1 = postedTheme;

        Theme postedTheme1 = this.themeService.getTheme(this.postedTheme1.getId());
        Assert.assertTrue(postedTheme1.getName().equals(this.postedTheme1.getName()));
        Assert.assertTrue(postedTheme1.getDescription().equals(this.postedTheme1.getDescription()));
        for (Organiser po : postedTheme1.getOrganisers()) {


            Assert.assertTrue(po.getEmail().equals(organiser1.getEmail()));


        }
    }

    @Test
    public void testCreateSecondTheme() throws ObjectNotFoundException {
        Theme postedTheme = this.themeService.addTheme(testTheme2, organiser1.getEmail());
        Assert.assertTrue(postedTheme.getName().equals(testTheme2.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme2.getDescription()));
        this.postedTheme2 = postedTheme;


        Theme postedTheme2 = this.themeService.getTheme(this.postedTheme2.getId());
        Assert.assertTrue(postedTheme2.getName().equals(this.postedTheme2.getName()));
        Assert.assertTrue(postedTheme2.getDescription().equals(this.postedTheme2.getDescription()));
        // Assert.assertTrue(postedTheme2.getOrganisers().contains(new Organiser(true , "tim.vanaelst@student.kdg.be", testTheme3.getId())));
        for (Organiser po : postedTheme2.getOrganisers()) {
            Assert.assertTrue(po.getEmail().equals(organiser1.getEmail()));


        }

    }


    @Test()
    public void testCreateThemeNoOrganisers() throws ObjectNotFoundException {
        testTheme1.setOrganisers(new LinkedList<>());
        Theme postedTheme = this.themeService.addTheme(testTheme1, organiser1.getEmail());
        Assert.assertTrue(postedTheme.getName().equals(testTheme1.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme1.getDescription()));
        this.postedTheme1 = postedTheme;

        Theme postedTheme1 = this.themeService.getTheme(this.postedTheme1.getId());
        Assert.assertTrue(postedTheme1.getName().equals(this.postedTheme1.getName()));
        Assert.assertTrue(postedTheme1.getDescription().equals(this.postedTheme1.getDescription()));
        //Assert.assertTrue(postedTheme1.getOrganisers().contains(organiser1));
    }

    @Test
    public void testSeperateUser() throws ObjectNotFoundException {
        Theme postedTheme = this.themeService.addTheme(testTheme3, organiser2.getEmail());
        Assert.assertTrue(postedTheme.getName().equals(testTheme3.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme3.getDescription()));
        this.postedTheme3 = postedTheme;


        Theme postedTheme3 = this.themeService.getTheme(this.postedTheme3.getId());
        Assert.assertTrue(postedTheme3.getName().equals(this.postedTheme3.getName()));
        Assert.assertTrue(postedTheme3.getDescription().equals(this.postedTheme3.getDescription()));

        //Organiser organiser = new Organiser(true , "indy.dewacker@student.kdg.be", testTheme3.getId());
        for (Organiser po : postedTheme3.getOrganisers()) {


            Assert.assertTrue(po.getEmail().equals(organiser2.getEmail()));


        }

    }

    @Test(expected = UsernameNotFoundException.class)
    public void testWrongUserName() {
        this.themeService.addTheme(testTheme1, new Organiser(false, "rezm@stud.kdg.be", testTheme1.getId()).getEmail());
    }

    @Test
    public void testGetAllThemes() throws ObjectNotFoundException {
        List<Theme> themes = this.themeService.getThemesByUser("tim.vanaelst@student.kdg.be");
        Assert.assertTrue(themes.size() == 1);
        Theme theme1 = themes.get(0);
//        Theme theme2 = themes.get(1);
        Assert.assertTrue(theme1.getName().equals("Tim's Thema"));
        Assert.assertTrue(theme1.getDescription().equals("tim zijn thema"));


    }



   /* @Test
    public void testAddOrganiser() throws ObjectNotFoundException {
        String themeId = "5a8d51c51525a03170a20be8";
        themeService.addOrganiser(themeId,new Organiser(true , "tim.vanaelst@student.kdg.be", testTheme3.getId()));
        List<Theme>  themes = themeService.getThemesByUser("indy.dewacker@student.kdg.be");
        Assert.assertTrue(themes.stream().anyMatch(x -> x.getId().equals(themeId)));
        Assert.assertTrue(themeService.getTheme(themeId).getOrganisers().contains(new Organiser(true , "indy.dewacker@student.kdg.be", testTheme3.getId())));

    }*/

/*
    @Test
    public void testAddOrganiser2() throws ObjectNotFoundException {
        String themeId = "5a8d51c51525a03170a20be8";
        themeService.addOrganiser(themeId,new Organiser(true , "indy.dewacker@student.kdg.be", testTheme3.getId()) );
        List<Theme>  themes = themeService.getThemesByUser("tim.vanaelst@student.kdg.be");
        Assert.assertTrue(themes.stream().anyMatch(x -> x.getId().equals(themeId)));
        Assert.assertTrue(themeService.getTheme(themeId).getOrganisers().contains(new Organiser(true , "indy.dewacker@student.kdg.be", testTheme3.getId())));
    }
*/

    @Test
    public void addandDeleteOrganiser() throws ObjectNotFoundException, UserAlreadyExistsException {
     String themId = "5a99536d1acf622da426d1e7";
     themeService.addOrganiser(themId, "tim.vanaelst@student.kdg.be", "remz.grges@student.kdg.be");
     List<Theme> themes = themeService.getThemesByUser("remz.grges@student.kdg.be");

        Assert.assertTrue(themes.size() == 1);

        Assert.assertTrue(themeService.getTheme(themId).getOrganisers().size() == 2);




    }

    @Test
    public void testAddExistingOrganiser() throws ObjectNotFoundException {

    }

    @Test
    public void testAddOrganiserWithoutRight() throws ObjectNotFoundException {

    }

    @Test
    public void testAddOrganiserWithoutAccount() throws ObjectNotFoundException {

    }

    @Test
    public void enableOrganiser() throws ObjectNotFoundException {

    }

   @After
    public void deleteThemes() throws ObjectNotFoundException {
        if (this.postedTheme1 != null) themeService.deleteTheme(this.postedTheme1.getId());
        if (this.postedTheme2 != null) themeService.deleteTheme(this.postedTheme2.getId());
        if (this.postedTheme3 != null) themeService.deleteTheme(this.postedTheme3.getId());

        if (this.postedTheme1 != null && this.postedTheme2 != null && this.postedTheme3 != null) {
            List<Theme> themes = this.themeService.getThemesByUser("tim.vanaelst@student.kdg.be");
            Assert.assertTrue(themes.size() == 0);

            themes = this.themeService.getThemesByUser("indy.dewacker@student.kdg.be");
            Assert.assertTrue(themes.size() == 0);
        }


    }

    @After
    public void deleteOrganisers() {

    }

}
