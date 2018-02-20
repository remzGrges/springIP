package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Theme;
import org.springframework.stereotype.Service;

/**
 * Created by Tim on 08/02/2018.
 */
@Service
public interface ThemeService {
    void addTheme(Theme theme, String userId);

    Theme getTheme(String id);
}
