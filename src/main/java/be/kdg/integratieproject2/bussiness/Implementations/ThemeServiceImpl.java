package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.api.themeInvitation.OnInvitationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.TokenService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.InvalidTokenException;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import be.kdg.integratieproject2.data.implementations.ThemeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tim on 08/02/2018.
 */

@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public ThemeServiceImpl() { }

    @Override
    public Theme updateTheme(Theme theme, String username) throws UserNotAuthorizedException, ObjectNotFoundException {
        getTheme(theme.getId(), username); //throws exceptions if not organiser
        if (theme.getSubThemes() != null && theme.getSubThemes().stream().anyMatch(s -> s.getId() == null)) {
            theme.getSubThemes().stream().filter(s -> s.getId() == null).forEach(s -> s.setId(new ObjectId().toString()));
        }
        return themeRepository.save(theme);
    }

    @Override
    public boolean checkThemeExist(String themeID, String username) {

        try {

            if (themeID == null) {
                return false;
            }

            getTheme(themeID, username);
            return true;
        } catch (ObjectNotFoundException | UserNotAuthorizedException e) {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public Boolean isOrganiser(String username, String themeId) throws ObjectNotFoundException {
        Theme theme = getThemeNoAuth(themeId);
        return theme.getOrganisers().stream().anyMatch(s -> s.equalsIgnoreCase(username));
    }

    @Override
    public Theme addTheme(Theme theme, String username) {
        LinkedList<String> organisers = new LinkedList<>();
        organisers.add(username);
        theme.setOrganisers(organisers);
        return themeRepository.save(theme);
    }

    @Override
    public Theme getTheme(String themeId, String username) throws ObjectNotFoundException, UserNotAuthorizedException {
        Theme theme = themeRepository.findOne(themeId);
        if(theme == null) throw new ObjectNotFoundException(themeId);
        if(!isOrganiser(username, themeId)) throw new UserNotAuthorizedException("User does not have access to this Theme");
        return theme;
    }

    private Theme getThemeNoAuth(String themeId) throws ObjectNotFoundException {
        Theme theme = themeRepository.findOne(themeId);
        if(theme == null) throw new ObjectNotFoundException(themeId);
        return theme;
    }

    @Override
    public List<Theme> getThemesByUser(String username) {
        return themeRepository.getAllByOrganisersContaining(username);
    }

    @Override
    public void deleteTheme(String themeId, String username) throws ObjectNotFoundException, UserNotAuthorizedException {
        Theme theme = themeRepository.findOne(themeId);
        if(theme == null) throw new ObjectNotFoundException(themeId);
        if (!isOrganiser(username, themeId)) throw new UserNotAuthorizedException("User does not have access to this Theme");
        themeRepository.delete(themeId);
    }

    @Override
    public void addOrganiser(String username, String token) throws ObjectNotFoundException, InvalidTokenException, UserAlreadyExistsException {
        InvitationToken invitationToken = tokenService.getInvitationToken(token);
        tokenService.validateInvitationToken(invitationToken, username);
        String themeId = invitationToken.getThemeId();
        String organiserToAdd = invitationToken.getEmail();

        Theme theme = getThemeNoAuth(themeId);

        List<String> organisers;
        if (theme.getOrganisers() == null) organisers = new ArrayList<>();
        else {
            organisers = theme.getOrganisers();
            if (organisers.contains(organiserToAdd))throw new UserAlreadyExistsException("User is already an Organiser");
        }

        organisers.add(organiserToAdd);
        theme.setOrganisers(organisers);
        themeRepository.save(theme);
    }

    @Override
    public void inviteOrganiser(String themeId, String currentUser, String userToInvite, String appUrl, Locale locale) throws UserAlreadyExistsException, UserNotAuthorizedException, ObjectNotFoundException {
        if (!isOrganiser(currentUser, themeId)) throw new UserNotAuthorizedException("User is not authorized to invite new organisers");
        ApplicationUser user;
        String themeName = getThemeNoAuth(themeId).getName();
        try {
            user = userService.getUserByUsername(userToInvite);
            if (user.getEmail() != null) {
                eventPublisher.publishEvent(new OnInvitationCompleteEvent(user, locale, appUrl, themeId, themeName));
            }
        } catch (UsernameNotFoundException a) {
            ApplicationUser newUser = new ApplicationUser();
            newUser.setEmail(userToInvite);
            eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.registerUser(newUser), locale, appUrl, themeId, themeName));
        }
    }

}
