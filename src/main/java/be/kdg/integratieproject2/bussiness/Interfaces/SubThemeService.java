package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubThemeService {

    SubTheme addSubTheme(SubTheme subTheme, String userId, String themeId) throws ObjectNotFoundException;

    void deleteSubTheme(String subThemeid, String userName) throws ObjectNotFoundException;

    SubTheme updateSubTheme(SubTheme subThemePosted, String userName) throws ObjectNotFoundException;

    SubTheme getSubTheme(String subThemeId, String userName) throws ObjectNotFoundException;

    List<SubTheme> getAllSubThemesTheme(String themeId, String userName) throws ObjectNotFoundException;

    Theme getThemeBySubThemeId(String subthemeId, String userName) throws ObjectNotFoundException;
}
