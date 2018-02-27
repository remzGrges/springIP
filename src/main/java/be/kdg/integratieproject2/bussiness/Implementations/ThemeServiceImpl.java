package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.data.implementations.ThemeRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tim on 08/02/2018.*/

@Service
public class ThemeServiceImpl implements ThemeService {

    private ThemeRepository themeRepository;
    private UserService userService;

    public ThemeServiceImpl(ThemeRepository themeRepository, UserService userService) {
        this.themeRepository = themeRepository;
        this.userService = userService;
    }

    @Override
    public Theme addTheme(Theme theme, String userName) {
        ApplicationUser user = userService.getUserByUsername(userName);
        if(theme.getOrganisers() == null || theme.getOrganisers().size() == 0)
        {
            LinkedList<String> organisers =  new LinkedList<>();
            organisers.add(userName);
            theme.setOrganisers(organisers);
        }
        Theme savedTheme = themeRepository.save(theme);
        List<String> themes = user.getThemes();
        if (themes == null) themes = new LinkedList<>();
        if(!themes.contains(savedTheme.getId())) {
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
    public List<Theme> getThemesByUser(String userName)
    {
        LinkedList<Theme> themes = new LinkedList<>();
        ApplicationUser user = userService.getUserByUsername(userName);
        for (String id: user.getThemes()
             ) {
            themes.add(themeRepository.findOne(id));
        }
        return themes;
    }

    @Override
    public void deleteTheme(String id){
        Theme theme = getTheme(id);
        for(String organiser : theme.getOrganisers())
        {
            ApplicationUser user = userService.getUserByUsername(organiser);
            List<String> themes = user.getThemes();
            if(themes != null) themes.removeIf(x -> x.equals(id));
            user.setThemes(themes);
            userService.updateRegisteredUser(user);
        }
        themeRepository.delete(id);
    }

    @Override
    public void updateTheme(Theme theme) {
        themeRepository.save(theme);
    }


}
