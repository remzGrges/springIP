package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.Domain.verification.Token;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.data.implementations.ThemeRepository;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import org.bson.types.ObjectId;
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
        this.userService=userService;
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
    public Boolean isOrganiser(String loggedInUser, String themeId) throws ObjectNotFoundException {
        Theme theme = getTheme(themeId);
        ApplicationUser user = userService.getUserByUsername(loggedInUser);

        if (theme.getOrganisers() != null) {
            if (getThemesByUser(loggedInUser).contains(theme)) {
                return true;
            };
        }
        return false;

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
    public void deleteTheme(String id) {
        themeRepository.delete(id);
    }


    @Override
    public String addOrganiser (String themeId, String currentOrganiser, String newOrganiser) throws ObjectNotFoundException, UsernameNotFoundException {
        Theme theme = getTheme(themeId);

        if (theme.getOrganisers() != null) {
            if (theme.getOrganisers().contains(currentOrganiser) ) {
                List<String> organisers = theme.getOrganisers();

                try {
                    /*if (userService.getUserByUsername(newOrganiser) == null) {
                        throw new UsernameNotFoundException("bestaat niet");
                    }*/
                    ApplicationUser newOrganiserUser = userService.getUserByUsername(newOrganiser);


                    List<Theme> themes = getThemesByUser(newOrganiser);


                    if (themes == null) {
                        themes = new ArrayList<>();
                    }
                    for (Theme s : themes) {
                        if (s.getId().equals(themeId)) {

                            //throw new OrganiserAlreadyExistException("Organiser bestaat al");
                            return null;

                        }
                    }
                    themes.add(theme);
                    /*newOrganiserUser.setThemes(themes);*/


                    if (organisers.contains(newOrganiser)) {
                        //throw new OrganiserAlreadyExistException("Organiser bestaal al");
                        return null;
                    }
                    organisers.add(newOrganiser);
                    userService.updateRegisteredUser(newOrganiserUser);
                    updateTheme(theme);

                    return newOrganiser;
                } catch (UsernameNotFoundException e) {
                    //email sturen
                    ApplicationUser user = new ApplicationUser();
                    user.setEmail(newOrganiser);
                    user.setEnabled(false);





                    return newOrganiser;


                }

            }
        }
        return null;
    }

    @Override
    public String getOrganiser(String theme, String username) throws ObjectNotFoundException {
        String currentOrganiser = null;
        List<Theme> themes = getThemesByUser(username);
        Theme theme1  = getTheme(theme);

        if (theme1 == null) {
            throw new ObjectNotFoundException(theme);
        }

        for (String organiser : theme1.getOrganisers()) {
            if (organiser != null && organiser.equals(username)) {
                currentOrganiser = organiser;
            }
        }



        return currentOrganiser;
    }

    @Override
    public String updateExistingOrganiser(String organiser, String themeId) throws ObjectNotFoundException {

     /*   Theme theme = getTheme(themeId);

        Organiser existingOrganiser = getOrganiser(themeId,username);




            //themeRepository.save(theme);
        if (existingOrganiser == null) {
            throw new ObjectNotFoundException(themeId);

        }
        existingOrganiser.setEnabled(true);

        if (!existingOrganiser.getEnabled()) {
            throw new ObjectNotFoundException(themeId);
        }*/
        Theme theme = getTheme(themeId);

        List<String> organiserList = theme.getOrganisers();

        //.removeIf();
        organiserList.removeIf(x -> x.equals(organiser));
        organiserList.add(organiser);
        theme.setOrganisers(organiserList);
        updateTheme(theme);
        return organiser;
        /* themeRepository.save(theme);*/
        // Theme theme = getThemeBySubThemeId(subThemePosted.getId(), userName);
     /*
        List<SubTheme> subThemes = theme.getSubThemes();
        subThemes.removeIf(x -> x.getId().equals(subThemePosted.getId()));
        subThemes.add(subThemePosted);
        theme.setSubThemes(subThemes);
        themeService.updateTheme(theme);
        return subThemePosted;

       repository.save(organiser);*/



    }

    @Override
    public String deleteOrganiser(String themeId, String username) throws ObjectNotFoundException {
        Theme theme = getTheme(themeId);
        String organiser= getOrganiser(themeId, username);
        theme.getOrganisers().remove(organiser);
        ApplicationUser user = userService.getUserByUsername(username);

        getThemesByUser(username).remove(theme);

        return organiser;

    }
}
