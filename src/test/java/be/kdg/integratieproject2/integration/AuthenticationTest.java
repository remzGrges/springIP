package be.kdg.integratieproject2.integration;

import be.kdg.integratieproject2.api.dto.UserRegistrationDto;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import be.kdg.integratieproject2.data.implementations.UserRepository;
import com.google.gson.Gson;
import org.junit.*;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class AuthenticationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private MockMvc mvc;

    private UserRegistrationDto userRegistrationDto;
    private ApplicationUser user;
    private LoginDto loginDto;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();

        loginDto = new LoginDto("indy.dewacker@gmail.com", "admin");
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("indy.dewacker@gmail.com");
        userRegistrationDto.setFirstName("Indy");
        userRegistrationDto.setLastName("De Wacker");
        userRegistrationDto.setPassword("admin");
        userRegistrationDto.setMatchingPassword("admin");

        user = modelMapper.map(userRegistrationDto, ApplicationUser.class);
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));
    }

    @After
    public void teardown(){
        userRepository.deleteByEmail(user.getEmail());
        tokenRepository.deleteAll();
    }

    @Test
    public void registerUser() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(userRegistrationDto, UserRegistrationDto.class);

        ResultActions result =
                mvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Succes"));

    }

    @Test
    public void registerSameUserAgain() throws Exception {
        userRepository.save(user);
        Gson gson = new Gson();
        String json = gson.toJson(userRegistrationDto, UserRegistrationDto.class);

        ResultActions result =
                mvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isOk())
                        .andExpect(content().string("User Already Exists"));

    }

    @Test
    public void registerUserNotMatchingPasswords() throws Exception {
        UserRegistrationDto wrongDto = userRegistrationDto;
        wrongDto.setMatchingPassword("wrongPassword");
        Gson gson = new Gson();
        String json = gson.toJson(wrongDto, UserRegistrationDto.class);

        ResultActions result =
                mvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Body not valid"));

    }

    @Test
    public void loginDisabledUser() throws Exception {
        user.setEnabled(false);
        userRepository.save(user);

        Gson gson = new Gson();
        String json = gson.toJson(loginDto, LoginDto.class);

        ResultActions result =
                mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().is(401))
                        .andExpect(status().reason("Authentication Failed: User is disabled"))
                        .andExpect(unauthenticated());

    }

    @Test
    public void loginEnabledUser() throws Exception {
        user.setEnabled(true);
        userRepository.save(user);

        Gson gson = new Gson();
        String json = gson.toJson(loginDto, LoginDto.class);

        ResultActions result =
                mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isOk());

    }

    private class LoginDto {
        private String email;
        private String password;

        public LoginDto() {
        }

        public LoginDto(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
