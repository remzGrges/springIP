package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.Theme;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */
@Service
public interface ThemeService {
    Theme addTheme(Theme theme, String userId);

    Theme getTheme(String id);

    List<Theme> getThemesByUser(String userName);

    void deleteTheme(String id);

    void updateTheme(Theme theme);


    void addOrganiser(String theme, String organiser, String newOrganiser);
}
