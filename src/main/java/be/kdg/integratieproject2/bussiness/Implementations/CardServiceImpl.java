package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Application;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.data.implementations.CardRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;
    private UserService userService;
    private ThemeService themeService;

    public CardServiceImpl(CardRepository cardRepository, UserService userService, ThemeService themeService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.themeService = themeService;
    }

    @Override
    public Card addCard(Card card, String userId, String themeId) {
        ApplicationUser user = userService.getUserByUsername(userId);
        if (user.getThemes().contains(themeId)) {
            card.setUserId(userId);
            cardRepository.save(card);
            Theme theme = themeService.getTheme(themeId);
            List<Card> cards = theme.getCards();
            if (cards == null) {
                cards = new LinkedList<>();
            }
            cards.add(card);
            theme.setCards(cards);
            themeService.updateTheme(theme);
        } else {
            throw new BadRequestException();
        }
        return card;
    }


    @Override
    public Card getCard(String cardId) {
        return cardRepository.findOne(cardId);
    }

    @Override
    public List<Card> getCardsByTheme(String themeId, String userId) {
        Theme theme = themeService.getTheme(themeId);
        if (theme != null) {
            ApplicationUser user = userService.getUserByUsername(userId);
            List<String> themes = user.getThemes();
            if (themes.contains(theme.getId()))
                if (themeService.getTheme(themeId).getCards() == null) {
                    return new LinkedList<>();
                } else {
                    return themeService.getTheme(themeId).getCards();
                }
        }
        throw new BadRequestException();
    }

    @Override
    public List<Card> getAll() {
        return cardRepository.findAll();
    }


    @Override
    public void deleteCard(String id, String userId) {
        Card card = getCard(id);
        ApplicationUser user = userService.getUserByUsername(userId);

        List<Theme> themes = themeService.getThemesByUser(userId);
        if (themes == null || themes.size() == 0) {
            throw new BadRequestException();
        }
        for (Theme theme : themes) {
            if (theme != null) {
                if (theme.getCards() != null) {
                    List<Card> cards = theme.getCards();
                    cards.removeIf(x -> x.getId().equals(card.getId()));
                    theme.setCards(cards);
                    themeService.updateTheme(theme);
                }
            }

        }
        cardRepository.delete(card);
    }

}
