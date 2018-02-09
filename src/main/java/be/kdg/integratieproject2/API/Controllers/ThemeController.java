package be.kdg.integratieproject2.API.Controllers;

import be.kdg.integratieproject2.Domain.Theme;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tim on 08/02/2018.
 */
@RestController
@RequestMapping("/themes")
public class ThemeController {

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createTheme(@RequestBody Theme theme)
    {

    }
    @RequestMapping(value="/gettheme", method = RequestMethod.GET, produces = "application/json")
    public Theme getTheme()
    {
        Theme theme = new Theme();
        theme.setName("testTheme");
        return theme;
    }
}
