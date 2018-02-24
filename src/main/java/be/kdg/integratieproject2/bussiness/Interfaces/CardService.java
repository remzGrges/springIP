package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.domain.Card;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CardService {

    Card addCard(Card card, String userId);

    Card getCard(String cardId);

    List<Card> getCardsByTheme(String themeId);

    List<Card> getAll();

    void deleteCard(String id);

    List<String> getThemesByCard(String cardid);
}
