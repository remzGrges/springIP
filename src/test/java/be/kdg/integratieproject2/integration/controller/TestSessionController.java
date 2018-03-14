package be.kdg.integratieproject2.integration.controller;

import be.kdg.integratieproject2.api.dto.SessionDto;
import be.kdg.integratieproject2.data.implementations.SessionRepository;
import be.kdg.integratieproject2.data.implementations.UserRepository;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
public class TestSessionController {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    private MockMvc mvc;
    private SessionDto sessionDto;
    private HttpHeaders httpHeaders = new HttpHeaders();

    @Before
    public void setup() {
        httpHeaders.add("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsZWFuZGVyLmNvZXZvZXRAc3R1ZGVudC5rZGcuYmUiLCJleHAiOjE1MjE4MTc4OTR9._1V6c2QlsNEc_OmhL4GvxYlFucGSZ6kZWGoSsdCABst9VoJQFKo4CkYYyU4rGJpWVCNWwRh-CKR92PNFERjXXg");
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();

        sessionDto = new SessionDto();
        sessionDto.setSessionId("1");
        sessionDto.setThemeId("45454");
        sessionDto.setSessionName("add");
        List<String> players = new LinkedList<>();
        players.add("arno.addiers@student.kdg.be");
        sessionDto.setPlayers(players);
        sessionDto.setCanComment(false);
        sessionDto.setAddCardUser(false);
        sessionDto.setTimeUserRound(24);
        sessionDto.setOrganiser("leander.coevoet@student.kdg.be");
    }

    @After
    public void clean() {
        sessionRepository.delete(sessionDto.getSessionId());
        userRepository.deleteByEmail("fake@mail.be");
        userRepository.deleteByEmail("real@mail.be");
    }

    @Test
    public void createSession() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto, SessionDto.class);

        mvc.perform(post("/sessions/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void getSession() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto, SessionDto.class);

        mvc.perform(post("/sessions/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        mvc.perform(get("/sessions/getSession/" + sessionDto.getSessionId())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"sessionId\":\"1\",\"sessionName\":\"add\",\"themeId\":\"45454\",\"players\":[\"leander.coevoet@student.kdg.be\"],\"organiser\":\"leander.coevoet@student.kdg.be\",\"numberOfRounds\":0,\"canComment\":false,\"addCardUser\":false,\"timeUserRound\":24,\"startTime\":null,\"subThemes\":null,\"cards\":null,\"suggestedCards\":null}"));
    }

    @Test
    public void deleteSession() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto, SessionDto.class);

        mvc.perform(post("/sessions/create")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        mvc.perform(get("/sessions/delete/" + sessionDto.getSessionId())
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void updateSession() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto, SessionDto.class);

        mvc.perform(post("/sessions/create")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        MvcResult mvcResult = mvc.perform(get("/sessions/getSession/" + sessionDto.getSessionId())
                .headers(httpHeaders)).andReturn();


        sessionDto.setAddCardUser(true);
        json = gson.toJson(sessionDto, SessionDto.class);
        mvc.perform(post("/sessions/update")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void getAll() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto, SessionDto.class);

        mvc.perform(post("/sessions/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        mvc.perform(get("/sessions/getAll")
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void invitePlayers() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(sessionDto, SessionDto.class);

        mvc.perform(post("/sessions/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        List<String> players = new LinkedList<>();
        players.add("fake@mail.be");
        players.add("real@mail.be");
        String json2 = gson.toJson(players);
        mvc.perform(post("/sessions/invitePlayers/" + sessionDto.getSessionId())
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json2))
                .andExpect(status().isOk());
    }

    @Test
    public void acceptInviteBadToken() throws Exception {
        mvc.perform(get("/sessions/acceptInvite/blabla")
                .headers(httpHeaders))
                .andExpect(status().isNotFound());
    }

    @Test
    public void acceptInviteNonBadToken() throws Exception {
        mvc.perform(get("/sessions/acceptSessionInviteNon/blabla")
                .headers(httpHeaders)
                .param("email", "fake@mail.be"))
                .andExpect(status().isNotFound());
    }
}
