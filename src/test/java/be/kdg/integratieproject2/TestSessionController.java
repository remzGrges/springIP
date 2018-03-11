package be.kdg.integratieproject2;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.dto.SessionDto;
import be.kdg.integratieproject2.api.dto.ThemeDto;
import be.kdg.integratieproject2.api.dto.UserRegistrationDto;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.data.implementations.SessionRepository;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import be.kdg.integratieproject2.data.implementations.UserRepository;
import be.kdg.integratieproject2.integration.AuthenticationTest;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class TestSessionController {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SessionService sessionService;

    private MockMvc mvc;

    private SessionDto sessionDto;
    //private Session session;
    private Theme theme;
    private HttpHeaders httpHeaders = new HttpHeaders();

   /* @Before
    public void setup() {
        httpHeaders.add("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsZWFuZGVyLmNvZXZvZXRAc3R1ZGVudC5rZGcuYmUiLCJleHAiOjE1MjA5MzIxNzN9.L8CUUmPR7Ceok1W8UMBc9X97n9dKunypzMRpoG_AxuM6ghcKbh5pZ9-juJ7Iy1SM_qi-GHdvuwmW-_Uu5PzSEA");
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                //.apply(springSecurity())
                .build();

        List<ApplicationUser> players = new LinkedList<>();
        players.add(userService.getUserByUsername("leander.coevoet@student.kdg.be"));
        theme = new Theme();
        theme.setId("1");
        theme.setName("test");
        theme.setDescription("test description");
        sessionDto = new SessionDto();
        sessionDto.setSessionId("11111");
        sessionDto.setCanComment(true);
        sessionDto.setAddCardUser(true);
        sessionDto.setTimeUserRound(24);
        sessionDto.setStartTime(new Date());
        sessionDto.setNumberOfRounds(10);
        sessionDto.setPlayers(players);
        sessionDto.setTheme(theme);
    }

    @After
    public void teardown() {
        sessionRepository.delete(sessionDto.getSessionId());
        tokenRepository.deleteAll();
    }

    @Test
    public void createSession() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto, SessionDto.class);

        ResultActions result =
                mvc.perform(post("/sessions/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .headers(httpHeaders)
                        .content(json))
                        .andExpect(status().isCreated());
    }*/



}
