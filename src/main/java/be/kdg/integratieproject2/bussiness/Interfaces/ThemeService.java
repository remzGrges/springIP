package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.domain.Theme;
import org.springframework.stereotype.Service;

/**
 * Created by Tim on 08/02/2018.
 */
@Service
public interface ThemeService {
    Theme addTheme(Theme theme);
    Theme getTheme(String id);
}
