package be.kdg.integratieproject2;

import be.kdg.integratieproject2.API.Controllers.UserController;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.context.Theme;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class UserTest {
    @Autowired
    private UserController userController;

    @Autowired
    private WebApplicationContext wac;

 /*   private MockMvc mvc;
    private Gson gson;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ThemeService themeService;*/

    private Theme theme;
    private Object o;
 /*   @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.theme.setName("postedTheme");
        this.gson = new Gson();
        this.o = new StringBuilder();
    }*/
}
