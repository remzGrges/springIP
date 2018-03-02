package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CardService {

    Card addCard(Card card, String userId, String themeId) throws ObjectNotFoundException;

    Card getCard(String cardId, String userId) throws ObjectNotFoundException;

    List<Card> getCardsByTheme(String themeId, String userId) throws ObjectNotFoundException;

    void deleteCard(String id, String userId) throws ObjectNotFoundException;

    List<SubTheme> getAllCards(String userName) throws ObjectNotFoundException;
}
