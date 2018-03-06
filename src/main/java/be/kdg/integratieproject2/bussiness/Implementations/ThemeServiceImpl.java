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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        this.userService = userService;
    }

    @Override
    public InvitationToken getInvitationToken(String token) {
        return null;
    }

    @Override
    public void createInvitationToken(ApplicationUser email, String themeId, String token) {
        tokenRepository.save(new InvitationToken(token,email,themeId));
    }

    @Override
    public Theme addTheme(Theme theme, String userName) {
        ApplicationUser user = userService.getUserByUsername(userName);
        List<Organiser> organisers = theme.getOrganisers();
        if (organisers == null) organisers = new ArrayList<>();
        if (!organisers.stream().anyMatch(x -> x.getEmail().equals(userName)))
            organisers.add(new Organiser(true, userName));
        theme.setOrganisers(organisers);
        Theme savedTheme = themeRepository.save(theme);
        List<String> themes = user.getThemes();
        if (themes == null) themes = new LinkedList<>();
        if (!themes.contains(savedTheme.getId())) {
            themes.add(savedTheme.getId());
        }
        user.setThemes(themes);
        userService.updateRegisteredUser(user);
        return savedTheme;
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
    public List<Theme> getThemesByUser(String userName) throws ObjectNotFoundException {
        LinkedList<Theme> themes = new LinkedList<>();
        ApplicationUser user = userService.getUserByUsername(userName);
        if (user.getThemes() != null) {
            for (String id : user.getThemes()
                    ) {
                themes.add(themeRepository.findOne(id));
            }
        }

        return themes;
    }

    @Override
    public void deleteTheme(String id) throws ObjectNotFoundException {
        Theme theme = getTheme(id);
        for (Organiser organiser : theme.getOrganisers()) {
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
    public Theme updateTheme(Theme theme) {
        return themeRepository.save(theme);
    }


    @Override
    public Organiser addOrganiser(String themeId, String currentOrganiser, String newOrgMail) throws ObjectNotFoundException, UserAlreadyExistsException {
        Theme theme = getTheme(themeId);
        Organiser curOrganiser = getOrganiser(themeId, currentOrganiser);
        if (theme.getOrganisers() != null) {
            if (theme.getOrganisers().contains(curOrganiser)) {
                List<Organiser> organisers = theme.getOrganisers();

                try {
                    ApplicationUser newOrganiserUser = userService.getUserByUsername(newOrgMail);


                    List<String> themes = newOrganiserUser.getThemes();

                    if (themes == null) {
                        themes = new ArrayList<>();
                    }
                    for (String s : themes) {
                        if (s.equals(themeId)) {

                            //throw new OrganiserAlreadyExistException("Organiser bestaat al");
                            return null;

                        }
                    }
                    themes.add(themeId);
                    newOrganiserUser.setThemes(themes);
                    Organiser newOrganiser =  new Organiser(false, newOrgMail, themeId);

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
                    user.setEmail(newOrgMail);
                    user.setEnabled(false);





                    return new Organiser(false,themeId, newOrgMail);


                }

            }
        }
        return null;
    }

    @Override
    public Boolean isOrganiser(String loggedInUser, String themeId) throws ObjectNotFoundException {
        Theme theme = getTheme(themeId);
        ApplicationUser user = userService.getUserByUsername(loggedInUser);

        if (theme.getOrganisers() != null) {
            if (user.getThemes().contains(themeId)) {
                return true;
            };
        }
        return false;

    }



    @Override
    public Organiser getOrganiser(String theme, String username) throws ObjectNotFoundException {
        Organiser currentOrganiser = null;
        List<Theme> themes = getThemesByUser(username);
        Theme theme1  = getTheme(theme);

        if (theme1 == null) {
            throw new ObjectNotFoundException(theme);
        }

        for (Organiser organiser : theme1.getOrganisers()) {
            if (organiser != null && organiser.getEmail().equals(username)) {
                currentOrganiser = organiser;
            }
        }



        return currentOrganiser;
    }

    @Override
    public Organiser updateExistingOrganiser(Organiser organiser) throws ObjectNotFoundException {

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
        Theme theme = getTheme(organiser.getThemeID());

       List<Organiser> organiserList = theme.getOrganisers();

               //.removeIf();
        organiserList.removeIf(x -> x.getEmail().equals(organiser.getEmail()));
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
    public Organiser deleteOrganiser(String themeId, String username) throws ObjectNotFoundException {
        Theme theme = getTheme(themeId);
        Organiser organiser= getOrganiser(themeId, username);
        theme.getOrganisers().remove(organiser);
        ApplicationUser user = userService.getUserByUsername(username);

        user.getThemes().remove(themeId);

        return organiser;
    }


}
