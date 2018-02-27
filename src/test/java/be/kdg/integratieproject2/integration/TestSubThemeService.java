package be.kdg.integratieproject2.integration;


import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class TestSubThemeService {
    @Autowired
    private SubThemeService subThemeService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private CardService cardService;

    private Theme testTheme1;
    private Theme postedTheme1;
    private SubTheme subTheme;
    private SubTheme postedSubTheme;


    @After
    public void delete() {
        themeService.deleteTheme(this.postedTheme1.getId());
        if (postedSubTheme != null) {
            subThemeService.deleteSubTheme(postedSubTheme.getId(), "tim.vanaelst@student.kdg.be");
        }
    }

    @Before
    public void setup() {
        subTheme = new SubTheme();
        subTheme.setText("test subtheme");

        this.testTheme1 = new Theme();
        testTheme1.setDescription("Test");
        testTheme1.setName("testTheme1");
        List<String> organisers1 = new ArrayList<>();
        organisers1.add("tim.vanaelst@student.kdg.be");
        this.testTheme1.setOrganisers(organisers1);
        postedTheme1 = this.themeService.addTheme(testTheme1, "tim.vanaelst@student.kdg.be");
    }

    @Test
    public void testCreateSubTheme() {
        postedSubTheme = subThemeService.addSubTheme(subTheme, "tim.vanaelst@student.kdg.be", postedTheme1.getId());
        Assert.assertTrue(postedSubTheme.getText().equals(subTheme.getText()));
        Assert.assertTrue(postedSubTheme.getUserId().equals("tim.vanaelst@student.kdg.be"));
    }

    @Test(expected = BadRequestException.class)
    public void testCreateSubThemeWrongTheme() {
        postedSubTheme = subThemeService.addSubTheme(subTheme, "tim.vanaelst@student.kdg.be", "wrong");
    }

    @Test
    public void testDeleteSubTheme() {
        SubTheme subThemePosted = subThemeService.addSubTheme(subTheme, "tim.vanaelst@student.kdg.be", postedTheme1.getId());
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(), "tim.vanaelst@student.kdg.be").size() == 1);
        subThemeService.deleteSubTheme(subThemePosted.getId(), "tim.vanaelst@student.kdg.be");
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(), "tim.vanaelst@student.kdg.be").size() == 0);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testDeleteSubThemeWrongUser() {
        postedSubTheme = subThemeService.addSubTheme(subTheme, "tim.vanaelst@student.kdg.be", postedTheme1.getId());
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(), "tim.vanaelst@student.kdg.be").size() == 1);
        subThemeService.deleteSubTheme(postedSubTheme.getId(), "false@student.kdg.be");
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteSubThemeNoTheme() {
        postedSubTheme = subThemeService.addSubTheme(subTheme, "tim.vanaelst@student.kdg.be", postedTheme1.getId());
        subThemeService.deleteSubTheme(postedSubTheme.getId(), "leander-coevoet@hotmail.com");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testCreateWrongCredentials() {
        subThemeService.addSubTheme(subTheme, "false@student.kdg.be", postedTheme1.getId());
    }

    @Test
    public void testUpdateSubTheme() {
        postedSubTheme = subThemeService.addSubTheme(subTheme, "tim.vanaelst@student.kdg.be", postedTheme1.getId());
        Assert.assertTrue(cardService.getCard(postedSubTheme.getId()).getText().equals("test subtheme"));
        postedSubTheme.setText("Nieuwe tekst");
        SubTheme updatedSubTheme = subThemeService.updateSubTheme(postedSubTheme, "tim.vanaelst@student.kdg.be");
        Assert.assertTrue(updatedSubTheme.getText().equals(postedSubTheme.getText()));
    }

    @Test
    public void testGetSubTheme() {
        postedSubTheme = subThemeService.addSubTheme(subTheme, "tim.vanaelst@student.kdg.be", postedTheme1.getId());
        SubTheme getSubTheme = subThemeService.getSubTheme(subTheme.getId(), "tim.vanaelst@student.kdg.be");
        Assert.assertTrue(postedSubTheme.getText().equals(getSubTheme.getText()));
        Assert.assertTrue(postedSubTheme.getId().equals(getSubTheme.getId()));
        Assert.assertTrue(postedSubTheme.getUserId().equals(getSubTheme.getUserId()));
    }
}
