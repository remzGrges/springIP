package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import org.springframework.stereotype.Service;

@Service
public class SubThemeServiceImpl implements SubThemeService {
    private ThemeService themeService;
    private UserService userService;

    public SubThemeServiceImpl(ThemeService themeService, UserService userService) {
        this.themeService = themeService;
        this.userService = userService;
    }

    @Override
    public SubTheme addSubTheme(SubTheme subTheme, String userId, String themeId) throws BadRequestException {
        ApplicationUser user = userService.getUserByUsername(userId);
        Theme theme;
        if(user.getThemes().stream().anyMatch(x -> x.equals(themeId)))
        {
            theme = themeService.getTheme(themeId);
        }
        else {
            throw new BadRequestException();
        }
        return  null;
    }
}
