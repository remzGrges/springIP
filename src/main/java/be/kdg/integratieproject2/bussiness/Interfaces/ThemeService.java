package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.Domain.verification.Token;
import be.kdg.integratieproject2.bussiness.exceptions.InvalidTokenException;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * Created by Tim on 08/02/2018.
 */
@Service
public interface ThemeService {


    Theme addTheme(Theme theme, String userId);

    Theme getTheme(String id, String username) throws ObjectNotFoundException, UserNotAuthorizedException;

    List<Theme> getThemesByUser(String userName);

    void deleteTheme(String id, String username) throws ObjectNotFoundException, UserNotAuthorizedException;

    boolean checkThemeExist(String themeID);

    void addOrganiser(String ingelogdeGebruiker, String token) throws ObjectNotFoundException, UserAlreadyExistsException, UserNotAuthorizedException, InvalidTokenException;

    Theme updateTheme(Theme theme, String username) throws UserNotAuthorizedException, ObjectNotFoundException;

    Boolean isOrganiser(String loggedInUser, String themeId) throws ObjectNotFoundException, UserNotAuthorizedException;

    void inviteOrganiser(String themeId, String currentUser, String userToInvite, String appUrl, Locale locale) throws UserAlreadyExistsException, UserNotAuthorizedException, ObjectNotFoundException;
}