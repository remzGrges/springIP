package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubThemeService {

    SubTheme addSubTheme(SubTheme subTheme, String userName, String themeId) throws ObjectNotFoundException;

    void deleteSubTheme(String subThemeid, Organiser userName) throws ObjectNotFoundException;

    SubTheme updateSubTheme(SubTheme subThemePosted, Organiser userName) throws ObjectNotFoundException;

    SubTheme getSubTheme(String subThemeId, Organiser userName) throws ObjectNotFoundException;

    List<SubTheme> getAllSubThemesTheme(String themeId) throws ObjectNotFoundException;

    Theme getThemeBySubThemeId(String subthemeId, String userName) throws ObjectNotFoundException;

    List<SubTheme> getSubThemesByUser(String userName) throws ObjectNotFoundException;
}
