package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */
@Service
public interface ThemeService {

    InvitationToken getInvitationToken(String token);

    void createInvitationToken(String email, String themeId, String token);

    Theme addTheme(Theme theme, String userId);

    Theme getTheme(String id) throws ObjectNotFoundException;

    List<Theme> getThemesByUser(String userName) throws ObjectNotFoundException;

    void deleteTheme(String id) throws ObjectNotFoundException;

    Theme updateTheme(Theme theme);


    Organiser addOrganiser(String theme, String curOrg ,String newOrganiser) throws ObjectNotFoundException, UserAlreadyExistsException;;

    Boolean isOrganiser(String loggedInUser, String themeId) throws ObjectNotFoundException;

    Organiser getOrganiser(String  theme, String username) throws ObjectNotFoundException;

    Organiser updateExistingOrganiser(Organiser organiser) throws ObjectNotFoundException;

    Organiser deleteOrganiser(String themeId, String username) throws ObjectNotFoundException;
}
