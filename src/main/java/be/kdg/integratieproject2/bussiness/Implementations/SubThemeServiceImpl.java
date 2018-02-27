package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Application;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.data.implementations.CardRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SubThemeServiceImpl implements SubThemeService {

    private CardRepository cardRepository;
    private ThemeService themeService;
    private UserService userService;

    public SubThemeServiceImpl(ThemeService themeService, UserService userService, CardRepository cardRepository) {
        this.themeService = themeService;
        this.userService = userService;
        this.cardRepository = cardRepository;
    }

    @Override
    public SubTheme addSubTheme(SubTheme subTheme, String userId, String themeId) throws BadRequestException {
        ApplicationUser user = userService.getUserByUsername(userId);
        Theme theme;
        SubTheme subTheme1;
        if(user.getThemes().stream().anyMatch(x -> x.equals(themeId)))
        {
            subTheme.setUserId(userId);
            subTheme1= cardRepository.save(subTheme);
            theme = themeService.getTheme(themeId);
            List<Card> cards = theme.getCards();
            if (cards ==null){
                cards =new LinkedList<>();
            }
            cards.add(subTheme1);
            theme.setCards(cards);
            themeService.updateTheme(theme);
        }
        else {
            throw new BadRequestException();
        }
        return subTheme1;
    }

    @Override
    public void deleteSubTheme(String subthemeId, String userName) {
        ApplicationUser user = userService.getUserByUsername(userName);

        if (themeService.getThemesByUser(userName) == null || themeService.getThemesByUser(userName).size() == 0) {
            throw new BadRequestException();
        }
        List<Theme> themes = themeService.getThemesByUser(userName);
        for (Theme theme : themes) {
            if (theme != null) {
                if (theme.getCards() != null) {
                    List<Card> cards = theme.getCards();
                    cards.removeIf(x -> x.getId().equals(subthemeId));
                    theme.setCards(cards);
                    themeService.updateTheme(theme);
                }
            }
        }
        Card subTheme = cardRepository.findOne(subthemeId);
        cardRepository.delete(subTheme);

    }

    @Override
    public SubTheme updateSubTheme(SubTheme subThemePosted, String userName) {
        ApplicationUser user = userService.getUserByUsername(userName);
        return cardRepository.save(subThemePosted);
    }

    @Override
    public SubTheme getSubTheme(String subThemeId, String userName) {
        ApplicationUser user = userService.getUserByUsername(userName);
        return (SubTheme)cardRepository.findOne(subThemeId);
    }

}

