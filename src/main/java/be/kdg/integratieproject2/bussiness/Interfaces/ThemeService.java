package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.Domain.verification.Token;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
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

    boolean checkThemeExist(String themeID);

    String addOrganiser(String ingelogdeGebruiker ,String token) throws ObjectNotFoundException, UserAlreadyExistsException;;

    Boolean isOrganiser(String loggedInUser, String themeId) throws ObjectNotFoundException;

    String getOrganiser(String  theme, String username) throws ObjectNotFoundException;

    InvitationToken getInvitationToken(String token);

    List<String> getOrganisersByThemeId(String themeId);

    void createInvitationToken(String email, String themeId, String token);

    String updateExistingOrganiser(String organiser, String themeId) throws ObjectNotFoundException;

    String deleteOrganiser(String themeId, String username) throws ObjectNotFoundException;
}
