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
    public void addTheme(Theme theme, String userName) {
        String id = themeRepository.save(theme).getId();
        ApplicationUser user = userService.getUserByUsername(userName);
        List<String> themes = user.getThemes();
        if (themes == null) themes = new LinkedList<>();
        themes.add(id);
        user.setThemes(themes);
        userService.updateRegisteredUser(user);
    }

    @Override
    public Theme getTheme(String id) {
        return themeRepository.findOne(id);
    }

    @Override
    public List<Theme> getThemesByUser(String userName)
    {
        ApplicationUser user = userService.getUserByUsername(userName);
        themeRepository.
    }
}
