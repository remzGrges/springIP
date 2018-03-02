package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Application;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
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
    public Card addCard(Card card, String userId, String themeId) throws ObjectNotFoundException {
        ApplicationUser user = userService.getUserByUsername(userId);
        if (user.getThemes().contains(themeId)) {
            card.setUserId(userId);
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
    public Card getCard(String cardId, String userId) throws ObjectNotFoundException {
        List<Theme> themes = themeService.getThemesByUser(userId);
        for (Theme theme : themes)
        {
            Optional card = theme.getCards().stream().filter(x -> x.getId().equals(cardId)).findFirst();
            if(card.isPresent()) return (Card) card.get();
            for (SubTheme subTheme : theme.getSubThemes())
            {
                card = subTheme.getCards().stream().filter(x -> x.getId().equals(cardId)).findFirst();
                if(card.isPresent()) return (Card) card.get();
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
    public void deleteCard(String id, String userId) throws ObjectNotFoundException {
        Card card = getCard(id, userId);
        List<Theme> themes = themeService.getThemesByUser(userId);
        for (Theme theme : themes) {
            if (theme.getCards() != null) {
                List<Card> cards = theme.getCards();
                cards.removeIf(x -> x.getId().equals(card.getId()));
                theme.setCards(cards);
                themeService.updateTheme(theme);
            }
        }
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
