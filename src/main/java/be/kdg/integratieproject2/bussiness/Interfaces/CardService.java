package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Card;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CardService {

    Card addCard(Card card, String userId, String themeId);

    Card getCard(String cardId);

    List<Card> getCardsByTheme(String themeId, String userId);

    List<Card> getAll();

    void deleteCard(String id, String userId);

}
