package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Application;
import be.kdg.integratieproject2.Domain.*;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.data.implementations.ThemeRepository;
import org.springframework.stereotype.Service;

import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;


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
    private JavaMailSender mailSender;

    public ThemeServiceImpl(ThemeRepository themeRepository, UserService userService,JavaMailSender mailSender) {
        this.themeRepository = themeRepository;
        this.userService = userService;
    }

    @Override
    public Theme addTheme(Theme theme, String userName) {
        ApplicationUser user = userService.getUserByUsername(userName);
        if (theme.getOrganisers() == null || theme.getOrganisers().size() == 0) {
            LinkedList<Organiser> organisers = new LinkedList<>();
            organisers.add(new Organiser(true , userName, theme.getId()));
            theme.setOrganisers(organisers);
        }
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
    public Theme getTheme(String id) {
        return themeRepository.findOne(id);
    }

    @Override
    public List<Theme> getThemesByUser(String userName) {
        LinkedList<Theme> themes = new LinkedList<>();
        ApplicationUser user = userService.getUserByUsername(userName);
        for (String id : user.getThemes()
                ) {

            themes.add(themeRepository.findOne(id));
        }
        return themes;
    }

    @Override
    public void deleteTheme(String id) {
        Theme theme = getTheme(id);
        for (Organiser organiser : theme.getOrganisers()) {
            ApplicationUser user = userService.getUserByUsername(organiser.getEmail());
            List<String> themes = user.getThemes();
            if (themes != null) {
                themes.removeIf(x -> x.equals(id));
                user.setThemes(themes);
                userService.updateRegisteredUser(user);
            }
        }
        themeRepository.delete(id);
    }


    @Override
    public void updateTheme(Theme theme) {
        themeRepository.save(theme);
    }

    @Override
    public void addOrganiser(String themeId, Organiser newOrganiser) {
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
    public Boolean isOrganiser(String loggedInUser, String themeId) {
        Theme theme = getTheme(themeId);

        if (theme.getOrganisers() != null) {

        }
        return null;

    }


}
