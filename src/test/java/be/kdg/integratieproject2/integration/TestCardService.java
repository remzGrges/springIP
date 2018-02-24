package be.kdg.integratieproject2.integration;


import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
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
public class TestCardService {

    @Autowired
    private CardService cardService;
    @Autowired
    private ThemeService themeService;


    private Card testCard1;
    private Card testCard2;
    private Card testCard3;
    private Theme testTheme1;
    private Theme postedTheme1;

    @After
    public void delete() {
        themeService.deleteTheme(this.postedTheme1.getId());
    }

    @Before
    public void setup() {
        List<String> themes = new LinkedList<>();
        this.testTheme1 = new Theme();
        testTheme1.setDescription("Test");
        testTheme1.setName("testTheme1");
        List<String> organisers1 = new ArrayList<>();
        organisers1.add("tim.vanaelst@student.kdg.be");
        this.testTheme1.setOrganisers(organisers1);
        postedTheme1 = this.themeService.addTheme(testTheme1, "tim.vanaelst@student.kdg.be");
        themes.add(postedTheme1.getId());

        this.testCard1 = new Card();
        this.testCard1.setText("Hallo");
        this.testCard1.setThemes(themes);

        this.testCard2 = new Card();
        this.testCard2.setText("Persoon");
        this.testCard2.setThemes(themes);

        this.testCard3 = new Card();
        this.testCard3.setText("Iemand");
        this.testCard3.setThemes(themes);

    }

    @Test
    public void testDeleteCard() {
        Card postedCard = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be");
        Card postedCard1 = this.cardService.addCard(testCard2, "tim.vanaelst@student.kdg.be");
        Card postedCard2 = this.cardService.addCard(testCard3, "tim.vanaelst@student.kdg.be");
        Assert.assertTrue(cardService.getAll().size() == 3);
        cardService.deleteCard(postedCard.getId());
        Assert.assertTrue(cardService.getAll().size() == 2);
        cardService.deleteCard(postedCard1.getId());
        Assert.assertTrue(cardService.getAll().size() == 1);
        cardService.deleteCard(postedCard2.getId());
        Assert.assertTrue(cardService.getAll().size() == 0);
    }

    @Test
    public void testCreateCard() {
        Card postedCard = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be");
        Assert.assertTrue(postedCard.getText().equals(testCard1.getText()));
        Assert.assertTrue(postedCard.getId().equals(testCard1.getId()));
        Assert.assertTrue(postedCard.getUserId().equals("tim.vanaelst@student.kdg.be"));
        this.cardService.deleteCard(postedCard.getId());
    }

    @Test
    public void testCreate2Cards() {
        Card postedCard = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be");
        Card postedCard2 = this.cardService.addCard(testCard2, "belcrowbar@gmail.com");
        Assert.assertTrue(postedCard.getText().equals(testCard1.getText()));
        Assert.assertTrue(postedCard.getId().equals(testCard1.getId()));
        Assert.assertTrue(postedCard.getUserId().equals("tim.vanaelst@student.kdg.be"));
        Assert.assertTrue(postedCard2.getText().equals(testCard2.getText()));
        Assert.assertTrue(postedCard2.getId().equals(testCard2.getId()));
        Assert.assertTrue(postedCard2.getUserId().equals("belcrowbar@gmail.com"));
        this.cardService.deleteCard(postedCard.getId());
        this.cardService.deleteCard(postedCard2.getId());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testCreateWrongCredentials() {
        this.cardService.addCard(testCard1, "false@student.kdg.be");
    }

    @Test
    public void testGetAllCardsThema() {
        Card postedCard = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be");
        Card postedCard2 = this.cardService.addCard(testCard2, "belcrowbar@gmail.com");
        Card postedCard3 = this.cardService.addCard(testCard3, "tim.vanaelst@student.kdg.be");

        Assert.assertTrue(cardService.getCardsByTheme(testTheme1.getId()).size() == 3);
        this.cardService.deleteCard(postedCard.getId());
        this.cardService.deleteCard(postedCard2.getId());
        Assert.assertTrue(cardService.getCardsByTheme(testTheme1.getId()).size() == 1);
        this.cardService.deleteCard(postedCard3.getId());
        Assert.assertTrue(cardService.getCardsByTheme(testTheme1.getId()).size() == 0);
    }

    @Test
    public void testGetAllCardsThemaNoCards() {
        Assert.assertTrue(cardService.getCardsByTheme(testTheme1.getId()).size() == 0);
    }

    @Test
    public void testGetThemeByCard() {
        Card postedCard = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be");

        Assert.assertTrue(cardService.getThemesByCard(postedCard.getId()).size() == 1);
        List<String> themes = cardService.getThemesByCard(postedCard.getId());
        Theme theme = themeService.getTheme(themes.get(0));
        Assert.assertTrue(theme.getDescription().equals(this.testTheme1.getDescription()));

        this.cardService.deleteCard(postedCard.getId());
    }


}
