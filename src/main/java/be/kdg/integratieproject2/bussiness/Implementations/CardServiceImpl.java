package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.data.implementations.CardRepository;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;
    private UserService userService;

    public CardServiceImpl(CardRepository cardRepository, UserService userService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
        }

    @Override
    public Card addCard(Card card, String userId) {
        ApplicationUser user = userService.getUserByUsername(userId);
        card.setUserId(user.getEmail());
        return cardRepository.save(card);

    }


    @Override
    public Card getCard(String cardId) {
        return cardRepository.findOne(cardId);
    }

    @Override
    /*
    SHADY
     */
    public List<Card> getCardsByTheme(String themeId) {
        LinkedList<Card> cards = new LinkedList<>();

        for (Card card : cardRepository.findAll()) {
            if (card.getThemes().contains(themeId)){
                cards.add(card);
            }
        }
        return cards;
    }

    @Override
    public List<Card> getAll() {
        return cardRepository.findAll();
    }


    @Override
    public void deleteCard(String id) {
        Card card = getCard(id);
        cardRepository.delete(card);
    }

    @Override
    public List<String> getThemesByCard(String cardid) {
        List<String> themes = new LinkedList<>();

        Card card = cardRepository.findOne(cardid);
        themes.addAll(card.getThemes());
        return themes;
    }
}
