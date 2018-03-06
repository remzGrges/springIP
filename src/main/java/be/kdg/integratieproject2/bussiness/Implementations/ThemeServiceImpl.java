package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.data.implementations.ThemeRepository;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.io.Console;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Tim on 08/02/2018.
 */

@Service
public class ThemeServiceImpl implements ThemeService {

    private ThemeRepository themeRepository;
    private UserService userService;
    private TokenRepository tokenRepository;
    private JavaMailSender mailSender;

    public ThemeServiceImpl(ThemeRepository themeRepository, UserService userService, JavaMailSender mailSender, TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        this.themeRepository = themeRepository;
    }

    @Override
    public Theme updateTheme(Theme theme) {

        if (theme.getSubThemes() != null && theme.getSubThemes().stream().anyMatch(s -> s.getId() == null)) {
            theme.getSubThemes().stream().filter(s -> s.getId() == null).forEach(s -> s.setId(new ObjectId().toString()));
        }

        return themeRepository.save(theme);
    }

    @Override
    public InvitationToken getInvitationToken(String token) {
        return (InvitationToken) tokenRepository.findByToken(token);
    }

    @Override
    public void createInvitationToken(String email, String themeId, String token) {
        tokenRepository.save(new InvitationToken(token,email,themeId));
    }

    @Override
    public Theme addTheme(Theme theme, String username) {
        LinkedList<String> organisers = new LinkedList<>();
        organisers.add(username);
        theme.setOrganisers(organisers);
        return themeRepository.save(theme);
    }

    @Override
    public Theme getTheme(String id) throws ObjectNotFoundException {
        try {
            return themeRepository.findOne(id);
        } catch (Exception e) {
            throw new ObjectNotFoundException(id);

        }
    }

    @Override
    public List<Theme> getThemesByUser(String username) {
        return themeRepository.getAllByOrganisersContaining(username);
    }

    @Override
    public void deleteTheme(String id) throws ObjectNotFoundException {
        Theme theme = getTheme(id);
        for (String organiser : theme.getOrganisers()) {
            ApplicationUser user = userService.getUserByUsername(organiser.getEmail());
            List<String> themes = user.getThemes();
            if (themes != null) {
                themes.removeIf(x -> x.equals(id));
                user.setThemes(themes);
                userService.updateRegisteredUser(user);
            }
            themeRepository.delete(id);
        }
    }


    @Override
    public void addOrganiser (String themeId, String newOrganiser) throws ObjectNotFoundException {
        Theme theme = getTheme(themeId);
        if (theme.getOrganisers() != null) {
           /* if (theme.getOrganisers().contains(organiser)) {
                try{
                    ApplicationUser newOrganiserUser = userService.getUserByUsername(newOrganiser);
                    List<String> themes = newOrganiserUser.getThemes();
                    if(themes == null)
                    {
                        themes = new ArrayList<>();
                    }
                    themes.add(themeId);
                    newOrganiserUser.setThemes(themes);
                    userService.updateRegisteredUser(newOrganiserUser);
                }
                catch (Exception e)
                {
                    //email sturen
                }
                List<String> organisers = theme.getOrganisers();
                organisers.add(newOrganiser);
                theme.setOrganisers(organisers);
                updateTheme(theme);
            }*/
        }
    }

    @Override
    public Boolean isOrganiser (String loggedInUser, String themeId) throws ObjectNotFoundException {
        Theme theme = getTheme(themeId);

        if (theme.getOrganisers() != null) {

        }
        return null;

    }

    @Override
    public Organiser getOrganiser(Theme theme, String username) {
        Organiser currentOrganiser = null;
        for (Organiser organiser : theme.getOrganisers()) {
            if (organiser.getEmail().equals(username)) {
                currentOrganiser = organiser;
            }
        }
        return currentOrganiser;
    }
}
