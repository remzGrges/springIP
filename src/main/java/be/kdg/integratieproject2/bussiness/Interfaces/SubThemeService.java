package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.SubTheme;
import org.springframework.stereotype.Service;

@Service
public interface SubThemeService {

    SubTheme addSubTheme(SubTheme subTheme, String userId, String themeId);


}
