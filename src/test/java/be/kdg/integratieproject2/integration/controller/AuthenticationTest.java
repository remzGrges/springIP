package be.kdg.integratieproject2.integration.controller;

import be.kdg.integratieproject2.api.dto.UserInfoDto;
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
import org.springframework.http.HttpHeaders;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private HttpHeaders httpHeaders = new HttpHeaders();

    @Before
    public void setup() {
        httpHeaders.add("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsZWFuZGVyLmNvZXZvZXRAc3R1ZGVudC5rZGcuYmUiLCJleHAiOjE1MjE4MTc4OTR9._1V6c2QlsNEc_OmhL4GvxYlFucGSZ6kZWGoSsdCABst9VoJQFKo4CkYYyU4rGJpWVCNWwRh-CKR92PNFERjXXg");

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
    public void teardown() {
        userRepository.deleteByEmail(user.getEmail());
        tokenRepository.deleteAll();
    }

    @Test
    public void registrationConfirm() throws Exception {
        mvc.perform(get("/users/registrationConfirm")
                .param("token", "blabla"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUser() throws Exception {
        Gson gson = new Gson();
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setEmail("leander.coevoet@student.kdg.be");
        userInfoDto.setFirstName("Leander");
        userInfoDto.setLastName("Coevoet");
        userInfoDto.setPictureId("5aa25cb7c671850ef0492fc2");
        String json = gson.toJson(userInfoDto, UserInfoDto.class);

        mvc.perform(post("/users/update")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void currentUser() throws Exception {
        mvc.perform(get("/users/currentuser")
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void createSessieToken() throws Exception {
        mvc.perform(get("/token/create/Sessie")
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void createOrganiserToken() throws Exception {
        mvc.perform(get("/token/create/organiser")
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void createVerificatieToken() throws Exception {
        mvc.perform(get("/token/create/verificatie")
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void createBadToken() throws Exception {
        mvc.perform(get("/token/create/blabla")
                .headers(httpHeaders))
                .andExpect(status().isOk());
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
