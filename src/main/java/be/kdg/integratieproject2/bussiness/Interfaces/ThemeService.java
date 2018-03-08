package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */
@Service
public interface ThemeService {
    Theme addTheme(Theme theme, String userId);

    Theme getTheme(String id) throws ObjectNotFoundException;

    List<Theme> getThemesByUser(String userName) throws ObjectNotFoundException;

    void deleteTheme(String id) throws ObjectNotFoundException;

    Theme updateTheme(Theme theme);
}
