package be.kdg.integratieproject2.integration.controller;

import be.kdg.integratieproject2.api.dto.ThemeDto;
import be.kdg.integratieproject2.data.implementations.SessionRepository;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class TestThemeController {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TokenRepository token;
    @Autowired
    private UserRepository userRepository;

    private MockMvc mvc;
    private ThemeDto themeDto;
    private HttpHeaders httpHeaders = new HttpHeaders();

    @Before
    public void setup() {
        httpHeaders.add("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsZWFuZGVyLmNvZXZvZXRAc3R1ZGVudC5rZGcuYmUiLCJleHAiOjE1MjE4MTc4OTR9._1V6c2QlsNEc_OmhL4GvxYlFucGSZ6kZWGoSsdCABst9VoJQFKo4CkYYyU4rGJpWVCNWwRh-CKR92PNFERjXXg");
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();

        themeDto = new ThemeDto();
        themeDto.setId("1");
        themeDto.setName("test");
        themeDto.setDescription("test desc");
        List<String> orgs = new LinkedList<>();
        orgs.add("leander.coevoet@student.kdg.be");
        themeDto.setOrganisers(orgs);
    }

    @After
    public void clean() {
        sessionRepository.delete(themeDto.getId());
        token.deleteAll();
        userRepository.deleteByEmail("fake@mail.be");
    }

    @Test
    public void createTheme() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(themeDto, ThemeDto.class);

        mvc.perform(post("/themes/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void getTheme() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(themeDto, ThemeDto.class);

        mvc.perform(post("/themes/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        mvc.perform(get("/themes/get/" + themeDto.getId())
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTheme() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(themeDto, ThemeDto.class);

        mvc.perform(post("/themes/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        themeDto.setName("Updated test");
        String json2 = gson.toJson(themeDto, ThemeDto.class);
        mvc.perform(post("/themes/update")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json2))
                .andExpect(status().isOk());
    }

    @Test
    public void getAll() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(themeDto, ThemeDto.class);

        mvc.perform(post("/themes/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        mvc.perform(get("/themes/getAll")
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTheme() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(themeDto, ThemeDto.class);

        mvc.perform(post("/themes/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        mvc.perform(get("/themes/delete/" + themeDto.getId())
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void inviteOrg() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(themeDto, ThemeDto.class);

        mvc.perform(post("/themes/create")
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));

        String json2 = gson.toJson("fake@mail.be");
        mvc.perform(post("/themes/inviteOrg/" + themeDto.getId())
                .headers(httpHeaders)
                .accept("application/json;charset=UTF-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json2))
                .andExpect(status().isOk());
    }

    @Test
    public void acceptOrganiserInviteBadToken() throws Exception {
        mvc.perform(get("/themes/acceptOrganiserInvite/blabla")
                .headers(httpHeaders))
                .andExpect(status().isNotFound());
    }

    @Test
    public void acceptOrganiserInviteNonBadToken() throws Exception {
        mvc.perform(get("/themes/acceptOrganiserInviteNon/blabla")
                .headers(httpHeaders)
                .param("email", "fake@mail.be"))
                .andExpect(status().isNotFound());
    }

}
