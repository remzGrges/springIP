package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */
@Service
public interface ThemeService {
    Theme addTheme(Theme theme, Organiser userId);

    Theme getTheme(String id) throws ObjectNotFoundException;

    List<Theme> getThemesByUser(String userName) throws ObjectNotFoundException;

    void deleteTheme(String id) throws ObjectNotFoundException;

    Theme updateTheme(Theme theme);


    void addOrganiser(String theme, Organiser newOrganiser) throws ObjectNotFoundException;;

    Boolean isOrganiser(String loggedInUser, String themeId) throws ObjectNotFoundException;

    Organiser getOrganiser(Theme theme, String username);
}
