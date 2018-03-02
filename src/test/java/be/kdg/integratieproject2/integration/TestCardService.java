package be.kdg.integratieproject2.integration;


import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.junit.After;
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
    private Card postedCard1;
    private Organiser organiser1;

    @After
    public void delete() throws ObjectNotFoundException {
        themeService.deleteTheme(this.postedTheme1.getId());
        if (postedCard1 != null){
            cardService.deleteCard(postedCard1.getId(), "tim.vanaelst@student.kdg.be");
        }
    }

    @Before
    public void setup() {
        postedCard1 =null;

        List<String> themes = new LinkedList<>();
        this.testTheme1 = new Theme();
        testTheme1.setDescription("Test");
        testTheme1.setName("testTheme1");
        List<Organiser> organisers1 = new ArrayList<>();
        organiser1 = new Organiser(true , "tim.vanaelst@student.kdg.be" , testTheme1.getId());
        organisers1.add(organiser1);
        this.testTheme1.setOrganisers(organisers1);
        postedTheme1 = this.themeService.addTheme(testTheme1, organiser1.getEmail());
        themes.add(postedTheme1.getId());

        this.testCard1 = new Card();
        this.testCard1.setText("Hallo");

        this.testCard2 = new Card();
        this.testCard2.setText("Persoon");

        this.testCard3 = new Card();
        this.testCard3.setText("Iemand");

    }

   /* @Test
    public void testDeleteCard() {
        Card postedCard = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be",postedTheme1.getId());
        Card postedCard1 = this.cardService.addCard(testCard2, "tim.vanaelst@student.kdg.be",postedTheme1.getId());
        Card postedCard2 = this.cardService.addCard(testCard3, "tim.vanaelst@student.kdg.be",postedTheme1.getId());
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(),"tim.vanaelst@student.kdg.be" ).size() == 3);
        cardService.deleteCard(postedCard.getId(),"tim.vanaelst@student.kdg.be");
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(),"tim.vanaelst@student.kdg.be").size() == 2);
        cardService.deleteCard(postedCard1.getId(),"tim.vanaelst@student.kdg.be");
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(),"tim.vanaelst@student.kdg.be").size() == 1);
        cardService.deleteCard(postedCard2.getId(),"tim.vanaelst@student.kdg.be");
        Assert.assertTrue(cardService.getCardsByTheme(postedTheme1.getId(),"tim.vanaelst@student.kdg.be").size() == 0);
    }*/

   /* @Test
    public void testCreateCard() {
    @Test
    public void testCreateCard() throws ObjectNotFoundException {
        Card postedCard = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be",postedTheme1.getId());
        Assert.assertTrue(postedCard.getText().equals(testCard1.getText()));
        Assert.assertTrue(postedCard.getId().equals(testCard1.getId()));
        Assert.assertTrue(postedCard.getUserId().equals("tim.vanaelst@student.kdg.be"));
        this.cardService.deleteCard(postedCard.getId(),"tim.vanaelst@student.kdg.be");
    }*/

  /*  @Test
    public void testCreate2Cards() {
    @Test
    public void testCreate2Cards() throws ObjectNotFoundException {
        Card postedCard = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be",postedTheme1.getId());
        Card postedCard2 = this.cardService.addCard(testCard2, "tim.vanaelst@student.kdg.be",postedTheme1.getId());
        Assert.assertTrue(postedCard.getText().equals(testCard1.getText()));
        Assert.assertTrue(postedCard.getId().equals(testCard1.getId()));
        Assert.assertTrue(postedCard.getUserId().equals("tim.vanaelst@student.kdg.be"));
        Assert.assertTrue(postedCard2.getText().equals(testCard2.getText()));
        Assert.assertTrue(postedCard2.getId().equals(testCard2.getId()));
        Assert.assertTrue(postedCard2.getUserId().equals("tim.vanaelst@student.kdg.be"));
        this.cardService.deleteCard(postedCard.getId(), "tim.vanaelst@student.kdg.be");
        this.cardService.deleteCard(postedCard2.getId(),"tim.vanaelst@student.kdg.be");
    }*/

    @Test (expected = BadRequestException.class)
    public void testCreateCardWrongNoThema() throws ObjectNotFoundException {
        Card postedCard = this.cardService.addCard(testCard2, "tim.vanaelst@student.kdg.be","wrong");
    }

    @Test (expected = UsernameNotFoundException.class)
    public void testCreateCardWrongUser() throws ObjectNotFoundException {
        Card postedCard = this.cardService.addCard(testCard2, "false@student.kdg.be",postedTheme1.getId());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testCreateWrongCredentials() throws ObjectNotFoundException {
        this.cardService.addCard(testCard1, "false@student.kdg.be",postedTheme1.getId());
    }

/*    @Test
    public void testGetAllCardsThema() {
        Card postedCard = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be",postedTheme1.getId());
        Card postedCard2 = this.cardService.addCard(testCard2, "tim.vanaelst@student.kdg.be",postedTheme1.getId());
        Card postedCard3 = this.cardService.addCard(testCard3, "tim.vanaelst@student.kdg.be",postedTheme1.getId());

        Assert.assertTrue(cardService.getCardsByTheme(testTheme1.getId(),"tim.vanaelst@student.kdg.be").size() == 3);
        this.cardService.deleteCard(postedCard.getId(),"tim.vanaelst@student.kdg.be");
        this.cardService.deleteCard(postedCard2.getId(),"tim.vanaelst@student.kdg.be");
        Assert.assertTrue(cardService.getCardsByTheme(testTheme1.getId(),"tim.vanaelst@student.kdg.be").size() == 1);
        this.cardService.deleteCard(postedCard3.getId(),"tim.vanaelst@student.kdg.be");
        Assert.assertTrue(cardService.getCardsByTheme(testTheme1.getId(),"tim.vanaelst@student.kdg.be").size() == 0);
    }*/

   /* @Test (expected = BadRequestException.class)
    public void testGetCardsByThemeWrongTheme() throws ObjectNotFoundException {
      cardService.getCardsByTheme("wrong","tim.vanaelst@student.kdg.be");
    }

    @Test
    public void testGetAllCardsThemaNoCards() throws ObjectNotFoundException {
        Assert.assertTrue(cardService.getCardsByTheme(testTheme1.getId(),"tim.vanaelst@student.kdg.be").size() == 0);
    }


    @Test (expected = BadRequestException.class)
    public void testDeleteCardBadUser() throws ObjectNotFoundException {
        postedCard1 = this.cardService.addCard(testCard1, "tim.vanaelst@student.kdg.be",postedTheme1.getId());
        cardService.deleteCard(postedCard1.getId(), "leander-coevoet@hotmail.com");
    }*/
}
