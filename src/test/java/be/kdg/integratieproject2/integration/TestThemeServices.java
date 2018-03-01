package be.kdg.integratieproject2.integration;

import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.controllers.ThemeController;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
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
    //private Object o;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        //this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.testTheme1 = new Theme();
        this.testTheme1.setName("testTheme1");
        this.testTheme1.setDescription("This is a test class");
        List<String> organisers1 = new ArrayList<>();
        organisers1.add("tim.vanaelst@student.kdg.be");
        this.testTheme1.setOrganisers(organisers1);

        this.testTheme2 = new Theme();
        this.testTheme2.setName("testTheme2");
        this.testTheme2.setDescription("This is a second test class");
        this.testTheme2.setOrganisers(organisers1);

        this.testTheme3 = new Theme();
        this.testTheme3.setName("testTheme3");
        this.testTheme3.setDescription("This is a third test class");
        List<String> organisers2 = new ArrayList<>();
        organisers2.add("indy.dewacker@student.kdg.be");
        this.testTheme3.setOrganisers(organisers2);


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
    public void testCreateTheme()
    {
        Theme postedTheme = this.themeService.addTheme(testTheme1, "tim.vanaelst@student.kdg.be");
        Assert.assertTrue(postedTheme.getName().equals(testTheme1.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme1.getDescription()));
        this.postedTheme1 = postedTheme;

        Theme postedTheme1 = this.themeService.getTheme(this.postedTheme1.getId());
        Assert.assertTrue(postedTheme1.getName().equals(this.postedTheme1.getName()));
        Assert.assertTrue(postedTheme1.getDescription().equals(this.postedTheme1.getDescription()));
        Assert.assertTrue(postedTheme1.getOrganisers().contains("tim.vanaelst@student.kdg.be"));

    }

    @Test
    public void testCreateSecondTheme()
    {
        Theme postedTheme = this.themeService.addTheme(testTheme2, "tim.vanaelst@student.kdg.be");
        Assert.assertTrue(postedTheme.getName().equals(testTheme2.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme2.getDescription()));
        this.postedTheme2 = postedTheme;


        Theme postedTheme2 = this.themeService.getTheme(this.postedTheme2.getId());
        Assert.assertTrue(postedTheme2.getName().equals(this.postedTheme2.getName()));
        Assert.assertTrue(postedTheme2.getDescription().equals(this.postedTheme2.getDescription()));
        Assert.assertTrue(postedTheme2.getOrganisers().contains("tim.vanaelst@student.kdg.be"));

    }


    @Test()
    public void testCreateThemeNoOrganisers(){
        testTheme1.setOrganisers(new LinkedList<>());
        Theme postedTheme = this.themeService.addTheme(testTheme1, "tim.vanaelst@student.kdg.be");
        Assert.assertTrue(postedTheme.getName().equals(testTheme1.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme1.getDescription()));
        this.postedTheme1 = postedTheme;

        Theme postedTheme1 = this.themeService.getTheme(this.postedTheme1.getId());
        Assert.assertTrue(postedTheme1.getName().equals(this.postedTheme1.getName()));
        Assert.assertTrue(postedTheme1.getDescription().equals(this.postedTheme1.getDescription()));
        Assert.assertTrue(postedTheme1.getOrganisers().contains("tim.vanaelst@student.kdg.be"));
    }

    @Test
    public void testSeperateUser()
    {
        Theme postedTheme = this.themeService.addTheme(testTheme3, "indy.dewacker@student.kdg.be");
        Assert.assertTrue(postedTheme.getName().equals(testTheme3.getName()));
        Assert.assertTrue(postedTheme.getDescription().equals(testTheme3.getDescription()));
        this.postedTheme3 = postedTheme;


        Theme postedTheme3 = this.themeService.getTheme(this.postedTheme3.getId());
        Assert.assertTrue(postedTheme3.getName().equals(this.postedTheme3.getName()));
        Assert.assertTrue(postedTheme3.getDescription().equals(this.postedTheme3.getDescription()));
        Assert.assertTrue(postedTheme3.getOrganisers().contains("indy.dewacker@student.kdg.be"));

    }

    @Test(expected = UsernameNotFoundException.class)
    public void testWrongUserName()
    {
        this.themeService.addTheme(testTheme1, "test@email.com");
    }

    @Test
    public void testGetAllThemes()
    {
        List<Theme> themes = this.themeService.getThemesByUser("test@student.kdg.be");
        Assert.assertTrue(themes.size() == 2);
        Theme theme1 = themes.get(0);
        Theme theme2 = themes.get(1);
        Assert.assertTrue(theme1.getName().equals("PermanentTheme2"));
        Assert.assertTrue(theme1.getDescription().equals("This is a permanent testTheme DO NOT DELETE"));

        Assert.assertTrue(theme2.getName().equals("PermanentTheme"));
        Assert.assertTrue(theme2.getDescription().equals("This is a permanent testTheme DO NOT DELETE"));
    }

    @After
    public void deleteThemes()
    {
        if(this.postedTheme1 != null) themeService.deleteTheme(this.postedTheme1.getId());
        if(this.postedTheme2 != null) themeService.deleteTheme(this.postedTheme2.getId());
        if(this.postedTheme3 != null) themeService.deleteTheme(this.postedTheme3.getId());

        if(this.postedTheme1 != null && this.postedTheme2 != null && this.postedTheme3 != null) {
            List<Theme> themes = this.themeService.getThemesByUser("tim.vanaelst@student.kdg.be");
            Assert.assertTrue(themes.size() == 0);

            themes = this.themeService.getThemesByUser("indy.dewacker@student.kdg.be");
            Assert.assertTrue(themes.size() == 0);
        }
    }

}
