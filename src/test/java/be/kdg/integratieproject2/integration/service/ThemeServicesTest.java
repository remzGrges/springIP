package be.kdg.integratieproject2.integration.service;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.Implementations.ThemeServiceImpl;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import be.kdg.integratieproject2.data.implementations.ThemeRepository;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class ThemeServicesTest {

    @Mock
    private ThemeRepository themeRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ThemeService themeService = new ThemeServiceImpl();

    private Theme themeToGet;
    private Theme testTheme2;
    private Theme themeWithoutId;
    private String organiser1;
    private String organiser2;
    private ApplicationUser user1;
    private ApplicationUser user2;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.themeToGet = new Theme();
        this.themeToGet.setId(new ObjectId().toString());
        this.themeToGet.setName("themeToGet");
        this.themeToGet.setDescription("This is a test class");
        List<String> organisers1 = new ArrayList<>();
        organiser1 = "tim.vanaelst@student.kdg.be";
        organisers1.add(organiser1);
        this.themeToGet.setOrganisers(organisers1);

        this.testTheme2 = new Theme();
        this.testTheme2.setId(new ObjectId().toString());
        this.testTheme2.setName("testTheme2");
        this.testTheme2.setDescription("This is a second test class");
        this.testTheme2.setOrganisers(organisers1);

        this.themeWithoutId = new Theme();
        this.themeWithoutId.setId(new ObjectId().toString());
        this.themeWithoutId.setName("themeWithoutId");
        this.themeWithoutId.setDescription("This is a third test class");
        List<String> organisers2 = new ArrayList<>();
        organiser2 = "indy.dewacker@student.kdg.be";
        organisers2.add(organiser2);
        this.themeWithoutId.setOrganisers(organisers2);

        this.user1 = new ApplicationUser();
        this.user1.setEmail(organiser1);
        this.user1.setEnabled(true);
        this.user1.setFirstName("Tim");
        this.user1.setLastName("Van Aelst");

        this.user2 = new ApplicationUser();
        this.user2.setEmail(organiser2);
        this.user2.setEnabled(true);
        this.user2.setFirstName("Indy");
        this.user2.setLastName("De Wacker");

        Mockito.when(themeRepository.findOne(this.themeToGet.getId())).thenReturn(themeToGet);
        Mockito.when(themeRepository.findOne(this.testTheme2.getId())).thenReturn(testTheme2);
        Mockito.when(themeRepository.findOne(this.themeToGet.getId())).thenReturn(themeToGet);

        Mockito.when(themeRepository.save(this.themeWithoutId)).then(new AddThemeAnswer());
    }

    @Test
    public void getTheme() throws ObjectNotFoundException, UserNotAuthorizedException {
        Theme theme = themeService.getTheme(themeToGet.getId(), organiser1);
        Assert.assertTrue(theme.getName().equals("themeToGet"));
        Assert.assertTrue(theme.getId().equals(themeToGet.getId()));
        Assert.assertTrue(theme.getDescription().equals(themeToGet.getDescription()));
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getThemeBadId() throws ObjectNotFoundException, UserNotAuthorizedException {
        themeService.getTheme("WAHt", organiser1);
    }

    @Test
    public void testCreateTheme() throws ObjectNotFoundException, UserNotAuthorizedException {
        Theme theme = themeService.addTheme(themeWithoutId, organiser2);
        Assert.assertTrue(theme.getName().equals(themeWithoutId.getName()));
        Assert.assertTrue(theme.getId() != null);
        Assert.assertTrue(theme.getDescription().equals(themeWithoutId.getDescription()));
    }

    @Test
    public void isOrganiser() throws ObjectNotFoundException, UserNotAuthorizedException {
        boolean isOrg = themeService.isOrganiser(organiser1, themeToGet.getId());
        Assert.assertTrue(isOrg);
    }

    @Test
    public void isNotOrganiser() throws ObjectNotFoundException, UserNotAuthorizedException {
        boolean isOrg = themeService.isOrganiser(organiser2, themeToGet.getId());
        Assert.assertFalse(isOrg);
    }

    class AddThemeAnswer implements Answer<Theme> {
        @Override
        public Theme answer(InvocationOnMock invocation) throws Throwable {
            Theme theme = invocation.getArgumentAt(0, Theme.class);
            theme.setId(new ObjectId().toString());
            return theme;
        }
    }

}
