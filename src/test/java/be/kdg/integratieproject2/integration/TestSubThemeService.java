package be.kdg.integratieproject2.integration;


import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
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
    private SubTheme subTheme2;
    private SubTheme postedSubTheme2;
    private Organiser organiser1;
    private Organiser organiser2;


    @After
    public void delete() throws ObjectNotFoundException {
        themeService.deleteTheme(this.postedTheme1.getId());
        if (postedSubTheme != null) {
            subThemeService.deleteSubTheme(postedSubTheme.getId(), organiser1);
        }
        if (postedSubTheme2 != null) {
            subThemeService.deleteSubTheme(postedSubTheme2.getId(), organiser1);
        }
    }

    @Before
    public void setup() {
        postedSubTheme = null;
        postedSubTheme2 = null;
        subTheme = new SubTheme();
        subTheme.setText("test subtheme");

        subTheme2 = new SubTheme();
        subTheme2.setText("test subtheme2");


        this.testTheme1 = new Theme();
        testTheme1.setDescription("Test");
        testTheme1.setName("testTheme1");
        List<Organiser> organisers1 = new ArrayList<>();
        organiser2 = new Organiser(true , "leander.coevoet@hotmail.com" , postedSubTheme.getId());
        organiser1 = new Organiser(true, "tim.vanaelst@student.kdg.be" , testTheme1.getId());
        organisers1.add(organiser1);
        this.testTheme1.setOrganisers(organisers1);
        postedTheme1 = this.themeService.addTheme(testTheme1, organiser1.getEmail());
    }

    @Test
    public void testGetAllSubThemesTheme() throws ObjectNotFoundException {
        postedSubTheme = subThemeService.addSubTheme(subTheme, organiser1.getEmail(), postedTheme1.getId());
        postedSubTheme2 = subThemeService.addSubTheme(subTheme2, organiser1.getEmail(), postedTheme1.getId());

        List<SubTheme> subThemes = subThemeService.getAllSubThemesTheme(postedTheme1.getId());
        Assert.assertTrue(subThemes.size() == 2);
        Assert.assertTrue(subThemes.get(0).getText().equals(postedSubTheme.getText()));
        Assert.assertTrue(subThemes.get(1).getText().equals(postedSubTheme2.getText()));

        Card card = new Card();
        card.setText("cards");
        card = cardService.addCard(card, "tim.vanaelst@student.kdg.be", postedTheme1.getId());
        subThemes = subThemeService.getAllSubThemesTheme(postedTheme1.getId());
        Assert.assertTrue(subThemes.size() == 2);
        Assert.assertTrue(subThemes.get(0).getText().equals(postedSubTheme.getText()));
        Assert.assertTrue(subThemes.get(1).getText().equals(postedSubTheme2.getText()));
        cardService.deleteCard(card.getId(), "tim.vanaelst@student.kdg.be");
    }

    @Test
    public void testCreateSubTheme() throws ObjectNotFoundException {
        postedSubTheme = subThemeService.addSubTheme(subTheme, organiser1.getEmail(), postedTheme1.getId());
        Assert.assertTrue(postedSubTheme.getText().equals(subTheme.getText()));
        Assert.assertTrue(postedSubTheme.getUserId().equals("tim.vanaelst@student.kdg.be"));
    }

    @Test(expected = BadRequestException.class)
    public void testCreateSubThemeWrongTheme() throws ObjectNotFoundException {
        postedSubTheme = subThemeService.addSubTheme(subTheme, organiser1.getEmail(), "wrong");
    }

    @Test
    public void testDeleteSubTheme() throws ObjectNotFoundException {
        SubTheme subThemePosted = subThemeService.addSubTheme(subTheme, organiser1.getEmail(), postedTheme1.getId());
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(), "tim.vanaelst@student.kdg.be").size() == 1);
        subThemeService.deleteSubTheme(subThemePosted.getId(), organiser1);
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(), "tim.vanaelst@student.kdg.be").size() == 0);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testDeleteSubThemeWrongUser() throws ObjectNotFoundException {
        postedSubTheme = subThemeService.addSubTheme(subTheme, organiser1.getEmail(), postedTheme1.getId());
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(), "tim.vanaelst@student.kdg.be").size() == 1);
        subThemeService.deleteSubTheme(postedSubTheme.getId(), new Organiser(false, "blabla@student.kdg.be", postedSubTheme.getId()));
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteSubThemeNoTheme() throws ObjectNotFoundException {
        postedSubTheme = subThemeService.addSubTheme(subTheme, organiser1.getEmail(), postedTheme1.getId());
        subThemeService.deleteSubTheme(postedSubTheme.getId(), organiser2);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testCreateWrongCredentials() throws ObjectNotFoundException {
        subThemeService.addSubTheme(subTheme, "llaakzodkd@hotmail.be", postedTheme1.getId());
    }

    @Test
    public void testUpdateSubTheme() throws ObjectNotFoundException {
        postedSubTheme = subThemeService.addSubTheme(subTheme, organiser1.getEmail(), postedTheme1.getId());
        Assert.assertTrue(subThemeService.getSubTheme(postedSubTheme.getId(), organiser1).getText().equals("test subtheme"));
        postedSubTheme.setText("Nieuwe tekst");
        SubTheme updatedSubTheme = subThemeService.updateSubTheme(postedSubTheme, organiser1);
        Assert.assertTrue(updatedSubTheme.getText().equals(postedSubTheme.getText()));
    }

    @Test
    public void testGetSubTheme() throws ObjectNotFoundException {
        postedSubTheme = subThemeService.addSubTheme(subTheme, organiser1.getEmail(), postedTheme1.getId());
        SubTheme getSubTheme = subThemeService.getSubTheme(subTheme.getId(), organiser1);
        Assert.assertTrue(postedSubTheme.getText().equals(getSubTheme.getText()));
        Assert.assertTrue(postedSubTheme.getId().equals(getSubTheme.getId()));
        Assert.assertTrue(postedSubTheme.getUserId().equals(getSubTheme.getUserId()));
    }
}
