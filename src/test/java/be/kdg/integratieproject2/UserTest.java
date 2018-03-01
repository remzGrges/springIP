package be.kdg.integratieproject2;

import be.kdg.integratieproject2.api.controllers.UserController;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class UserTest {
    @Autowired
    private UserController userController;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;
    private Gson gson;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;


    private UserDetails details;

    private ApplicationUser user;
    private Object o;
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        this.user= this.userService.getUserByUsername("indy.dewacker@student.kdg.be");

        this.gson = new Gson();
        this.o = new StringBuilder();
    }

    @Test
    public void testChangeName() throws Exception {


        this.userService.updateRegisteredUserName(user.getEmail(), "superAdmin");

        Assert.assertTrue(this.userService.getUserByUsername("indy.dewacker@student.kdg.be").getFirstName().equals("superAdmin"));

    }





 /*   @Test
    public void testUpdateName() throws Exception {
        MvcResult result = mvc.perform(post("/users/update")
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

*//*
        Assert.assertTrue(this.userService.getUserByUsername("tim.vanaelst@student.kdg.be").getFirstName().equals("superAdmin"));
*//*


    }*/





}
