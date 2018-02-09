package be.kdg.integratieproject2.API.Controllers;

import be.kdg.integratieproject2.API.BadRequestException;
import be.kdg.integratieproject2.API.Dto.ThemeDto;
import be.kdg.integratieproject2.BL.Interfaces.ThemeService;
import be.kdg.integratieproject2.Domain.Theme;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Tim on 08/02/2018.
 */
@RestController
@RequestMapping("/themes")
public class ThemeController {

    private ModelMapper modelMapper;
    private ThemeService themeService;

    public ThemeController(ModelMapper modelMapper, ThemeService themeService) {
        this.modelMapper = modelMapper;
        this.themeService = themeService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createTheme(@RequestBody ThemeDto dto) throws BadRequestException
    {
        Theme theme = modelMapper.map(dto, Theme.class);
        themeService.addTheme(theme);
    }
    @RequestMapping(value="/gettheme/{themeId}", method = RequestMethod.GET, produces = "application/json")
    public ThemeDto getTheme(@PathVariable String themeId, @RequestHeader(value= "Authorization")String token)
    {
        Theme theme = themeService.getTheme(themeId);
        return modelMapper.map(theme, ThemeDto.class);
    }
}
