package be.kdg.integratieproject2;

import be.kdg.integratieproject2.API.Controllers.UserController;
import be.kdg.integratieproject2.BL.Interfaces.UserService;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.context.Theme;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.user = new ApplicationUser();


        this.gson = new Gson();
        this.o = new StringBuilder();
    }

    @Test
    public void testChangeName() throws Exception {
        /*MvcResult result = mvc.perform(post("/user/change")
                            .content(gson.toJson(user))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk()).andReturn();*/
        this.details = userDetailsService.loadUserByUsername("tim.vanaelst@student.kdg.be");

        this.user.setEnabled(details.isEnabled());
        this.user.setEmail(details.getUsername());
        this.user.setFirstName("superAdmin");
        this.userService.updateRegisteredUser(user);
       //Assert.assertTrue(this.userService.ge);

    }


}
