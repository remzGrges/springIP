package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Application;
import be.kdg.integratieproject2.Domain.*;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    private UserService userService;
    private ThemeService themeService;
    private SubThemeService subThemeService;

    public CardServiceImpl( UserService userService, ThemeService themeService, SubThemeService subThemeService) {
        this.userService = userService;
        this.themeService = themeService;
        this.subThemeService = subThemeService;
    }

    @Override
    public Card addCard(Card card, String userName, String themeId) throws ObjectNotFoundException {
        ApplicationUser user = userService.getUserByUsername(userName);
        if (user.getThemes().contains(themeId)) {
            card.setUserId(userName);
            card.setId(new ObjectId().toString());
            Theme theme = themeService.getTheme(themeId);
            List<Card> cards = theme.getCards();
            if (cards == null) {
                cards = new LinkedList<>();
            }
            cards.add(card);
            theme.setCards(cards);
            themeService.updateTheme(theme);
        } else {
            throw new ObjectNotFoundException(themeId);
        }
        return card;
    }

    @Override
    public Card addCardAtSubTheme(Card card, String userName, String subThemeId) throws ObjectNotFoundException {
        SubTheme subTheme = subThemeService.getSubTheme(subThemeId, userName);
        card.setUserId(userName);
        card.setId(new ObjectId().toString());
        List<Card> cards = subTheme.getCards();
        if(cards == null) cards = new ArrayList<>();
        cards.add(card);
        subTheme.setCards(cards);
        subThemeService.updateSubTheme(subTheme, userName);
        return card;
    }

    @Override
    public Card updateCard(Card card, String userName) throws ObjectNotFoundException {
        List<Theme> themes = themeService.getThemesByUser(userName);
        Optional cardOpt;
        for (Theme theme : themes)
        {
            if(theme.getCards() != null) {
                cardOpt = theme.getCards().stream().filter(x -> x.getId().equals(card.getId())).findFirst();
                if (cardOpt.isPresent()){
                    List<Card> cards = theme.getCards();
                    cards.remove(cardOpt.get());
                    cards.add(card);
                    theme.setCards(cards);
                    themeService.updateTheme(theme);
                    return card;
                }
            }
            if(theme.getSubThemes() != null) {
                for (SubTheme subTheme : theme.getSubThemes()) {
                    cardOpt = subTheme.getCards().stream().filter(x -> x.getId().equals(card.getId())).findFirst();
                    if (cardOpt.isPresent()){
                        List<Card> cards = subTheme.getCards();
                        cards.remove(cardOpt.get());
                        cards.add(card);
                        subTheme.setCards(cards);
                        subThemeService.updateSubTheme(subTheme, userName);
                        return card;
                    }
                }
            }
        }
        throw new ObjectNotFoundException(card.getId());
    }


    @Override
    public Card getCard(String cardId, String userId) throws ObjectNotFoundException {
        List<Theme> themes = themeService.getThemesByUser(userId);
        Optional card;
        for (Theme theme : themes)
        {
            if(theme.getCards() != null) {
                card = theme.getCards().stream().filter(x -> x.getId().equals(cardId)).findFirst();
                if (card.isPresent()) return (Card) card.get();
            }
            if(theme.getSubThemes() != null) {
                for (SubTheme subTheme : theme.getSubThemes()) {
                    card = subTheme.getCards().stream().filter(x -> x.getId().equals(cardId)).findFirst();
                    if (card.isPresent()) return (Card) card.get();
                }
            }
        }
        throw new ObjectNotFoundException(cardId);
    }

    @Override
    public List<Card> getCardsByTheme(String themeId, String userId) throws ObjectNotFoundException {
        Theme theme = themeService.getTheme(themeId);
        ApplicationUser user = userService.getUserByUsername(userId);
        List<String> themes = user.getThemes();
            if (themes.contains(theme.getId()))
                if (themeService.getTheme(themeId).getCards() == null) {
                    return new LinkedList<>();
                } else {
                    return themeService.getTheme(themeId).getCards();
                }
        throw new ObjectNotFoundException(themeId);
    }


    @Override
    public void deleteCard(String id, String userName) throws ObjectNotFoundException {
        List<Theme> themes = themeService.getThemesByUser(userName);
        Optional cardOpt;
        for (Theme theme : themes)
        {
            if(theme.getCards() != null) {
                cardOpt = theme.getCards().stream().filter(x -> x.getId().equals(id)).findFirst();
                if (cardOpt.isPresent()){
                    List<Card> cards = theme.getCards();
                    cards.remove(cardOpt.get());
                    theme.setCards(cards);
                    themeService.updateTheme(theme);
                    return;
                }
            }
            if(theme.getSubThemes() != null) {
                for (SubTheme subTheme : theme.getSubThemes()) {
                    cardOpt = subTheme.getCards().stream().filter(x -> x.getId().equals(id)).findFirst();
                    if (cardOpt.isPresent()){
                        List<Card> cards = subTheme.getCards();
                        cards.remove(cardOpt.get());
                        subTheme.setCards(cards);
                        subThemeService.updateSubTheme(subTheme, userName);
                        return;
                    }
                }
            }
        }
        throw new ObjectNotFoundException(id);
    }

    @Override
    public List<SubTheme> getAllCards(String userName) throws ObjectNotFoundException
    {
        List<SubTheme> subThemes = subThemeService.getSubThemesByUser(userName);
        List<Theme> themes = themeService.getThemesByUser(userName);
        SubTheme uncatagorizedCards = new SubTheme();
        uncatagorizedCards.setText("Uncategorized Cards");
        List<Card> cards = new ArrayList<>();
        for(Theme theme : themes)
        {
            if(theme.getCards() != null) {
                cards.addAll(theme.getCards());
            }
        }
        uncatagorizedCards.setCards(cards);
        if(uncatagorizedCards.getCards().size() > 0) subThemes.add(uncatagorizedCards);
        return subThemes;
    }



}
