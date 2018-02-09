package be.kdg.integratieproject2.integratie;

import be.kdg.integratieproject2.API.Controllers.ThemeController;
import be.kdg.integratieproject2.Domain.Theme;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private MockMvc mvc;
    private Gson gson;

    @Mock
    private Theme theme;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
/*        this.theme = new Theme();
        this.theme.setName("testTheme");
        this.theme.setDescription("test");
        this.theme.setOrganisers(new LinkedList<>());
        this.theme.setTags(new LinkedList<>());*/
        this.gson = new Gson();
    }
    @Test
    public void testCreateTheme() throws Exception {
        mvc.perform(post("/themes/create").content(gson.toJson(theme)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testGetThema() throws Exception {
        mvc.perform(get("/themes/gettheme")).andExpect(jsonPath("$.name", is("testTheme")));
    }
}
