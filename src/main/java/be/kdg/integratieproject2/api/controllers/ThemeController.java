package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.ThemeDto;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

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
    public ResponseEntity<ThemeDto> createTheme(@RequestBody ThemeDto dto, Authentication authentication) throws BadRequestException
    {
        Theme theme = modelMapper.map(dto, Theme.class);
        ThemeDto mappedTheme = modelMapper.map(themeService.addTheme(theme, authentication.getName()), ThemeDto.class);
        return new ResponseEntity<ThemeDto>(mappedTheme, HttpStatus.CREATED);
    }
    @RequestMapping(value="/getAll", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ThemeDto>> getTheme(Authentication authentication)
    {
        List<Theme> themes;
        try {
            themes = themeService.getThemesByUser(authentication.getName());
        }
        catch(ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        List<ThemeDto> themeDTOs = new LinkedList<>();
        for(Theme theme : themes) {
            themeDTOs.add(modelMapper.map(theme, ThemeDto.class));
        }
        return new ResponseEntity<>(themeDTOs, HttpStatus.OK);
    }
    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity deleteTheme(Authentication authentication, @PathVariable String id) throws BadRequestException
    {
        try {
            themeService.deleteTheme(id);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
