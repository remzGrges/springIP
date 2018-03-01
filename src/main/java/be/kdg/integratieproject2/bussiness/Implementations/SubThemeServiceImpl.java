package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubThemeServiceImpl implements SubThemeService {

    private ThemeService themeService;
    private UserService userService;

    public SubThemeServiceImpl(ThemeService themeService, UserService userService) {
        this.themeService = themeService;
        this.userService = userService;
    }

    @Override
    public SubTheme addSubTheme(SubTheme subTheme, String userId, String themeId) throws ObjectNotFoundException {
        ApplicationUser user = userService.getUserByUsername(userId);
        Theme theme;
        if (user.getThemes().stream().anyMatch(x -> x.equals(themeId))) {
            subTheme.setUserId(userId);
            theme = themeService.getTheme(themeId);
            List<Card> cards = theme.getCards();
            cards.add(subTheme);
            theme.setCards(cards);
            themeService.updateTheme(theme);
        } else {
            throw new ObjectNotFoundException(themeId);
        }
        return subTheme;
    }

    @Override
    public void deleteSubTheme(String subthemeId, String userName) throws ObjectNotFoundException {
        //TODO: Cascading delete cards of niet
        List<Theme> themes = themeService.getThemesByUser(userName);
        for (Theme theme : themes) {
            if (theme.getCards() == null) theme.setCards(new ArrayList<>());
            List<Card> cards = theme.getCards();
            cards.removeIf(x -> x.getId().equals(subthemeId));
            theme.setCards(cards);
            themeService.updateTheme(theme);
        }
    }

    @Override
    public SubTheme updateSubTheme(SubTheme subThemePosted, String userName) throws ObjectNotFoundException {
        Theme theme = getThemeBySubThemeId(subThemePosted.getId(), userName);
        List<SubTheme> subThemes = theme.getSubThemes();
        subThemes.removeIf(x -> x.getId().equals(subThemePosted.getId()));
        subThemes.add(subThemePosted);
        theme.setSubThemes(subThemes);
        return subThemePosted;
    }

    @Override
    public SubTheme getSubTheme(String subThemeId, String userName) throws ObjectNotFoundException {
        Theme theme = getThemeBySubThemeId(subThemeId, userName);
        return theme.getSubThemes().stream().filter(x -> x.getId().equals(subThemeId)).findFirst().get();
    }

    @Override
    public List<SubTheme> getAllSubThemesTheme(String themeId, String userName) throws ObjectNotFoundException {
        ApplicationUser user = userService.getUserByUsername(userName);
        return themeService.getTheme(themeId).getSubThemes();
    }

    @Override
    public Theme getThemeBySubThemeId(String subthemeId, String userName) throws ObjectNotFoundException {
        Optional optTheme = themeService.getThemesByUser(userName).stream()
                .filter(x -> x.getSubThemes()
                        .stream()
                        .anyMatch(y -> y.getId().equals(subthemeId)))
                .findFirst();
        if(optTheme.isPresent()) return (Theme) optTheme.get();
        else throw new ObjectNotFoundException(subthemeId);
    }

}

