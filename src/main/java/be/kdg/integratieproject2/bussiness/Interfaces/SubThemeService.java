package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.SubTheme;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubThemeService {

    SubTheme addSubTheme(SubTheme subTheme, String userId, String themeId);

    void deleteSubTheme(String subThemeid, String userName);

    SubTheme updateSubTheme(SubTheme subThemePosted, String userName);

    SubTheme getSubTheme(String subThemeId, String userName);

    List<SubTheme> getAllSubThemesTheme(String themeId, String userName) throws ObjectNotFoundException;

    Theme getThemeBySubThemeId(String subthemeId, String userName) throws ObjectNotFoundException;
    List<SubTheme> getThemesByUser(String userName);

    List<SubTheme> getAllSubThemesTheme(String themeId, String userName);
}
