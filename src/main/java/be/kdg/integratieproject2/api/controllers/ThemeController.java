package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.ThemeDto;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.domain.Theme;
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
    public String createTheme(@RequestBody ThemeDto dto) throws BadRequestException
    {
        Theme theme = modelMapper.map(dto, Theme.class);
        return themeService.addTheme(theme).getId();
    }
    @RequestMapping(value="/gettheme/{themeId}", method = RequestMethod.GET, produces = "application/json")
    public ThemeDto getTheme(@PathVariable String themeId)
    {
        Theme theme = themeService.getTheme(themeId);
        return modelMapper.map(theme, ThemeDto.class);
    }
}