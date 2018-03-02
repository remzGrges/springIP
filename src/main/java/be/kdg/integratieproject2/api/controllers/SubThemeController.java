package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.SubThemeDto;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/subthemes")
public class SubThemeController {

    private ModelMapper modelMapper;
    private SubThemeService subThemeService;
    private ThemeService themeService;

    public SubThemeController(ModelMapper modelMapper, SubThemeService subThemeService, ThemeService themeService) {
        this.modelMapper = modelMapper;
        this.subThemeService = subThemeService;
        this.themeService = themeService;
    }

    @RequestMapping(value = "/create/{themeId}", method = RequestMethod.POST)
    public ResponseEntity<SubThemeDto> createSubTheme(@RequestBody SubThemeDto dto, Authentication authentication, @PathVariable String themeId) throws BadRequestException
    {
        SubTheme subTheme = modelMapper.map(dto, SubTheme.class);
        Theme theme = null;
        try {
            theme = subThemeService.getThemeBySubThemeId(dto.getId() , authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        Organiser organiser = themeService.getOrganiser(theme,authentication.getName());
        SubThemeDto mappedCard = null;
        try {
            mappedCard = modelMapper.map(subThemeService.addSubTheme(subTheme, organiser, themeId ), SubThemeDto.class);
        } catch (ObjectNotFoundException e) {
           throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<SubThemeDto>(mappedCard, HttpStatus.CREATED);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity deleteSubTheme(Authentication authentication, @PathVariable String id) throws ObjectNotFoundException {
        Theme theme = subThemeService.getThemeBySubThemeId(id , authentication.getName());
        Organiser organiser = themeService.getOrganiser(theme,authentication.getName());
        try {
            subThemeService.deleteSubTheme(id, organiser);
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/get/{subThemeId}", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<SubThemeDto> GetSubTheme(Authentication authentication, @PathVariable String subThemeId) throws ObjectNotFoundException {
        SubTheme subTheme = null;
        Theme theme = subThemeService.getThemeBySubThemeId(subThemeId , authentication.getName());
        Organiser organiser = themeService.getOrganiser(theme,authentication.getName());
        try {
            subTheme = subThemeService.getSubTheme(subThemeId,organiser);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        SubThemeDto subThemeDto = modelMapper.map(subTheme, SubThemeDto.class);
        return new ResponseEntity<SubThemeDto>(subThemeDto, HttpStatus.OK);
    }

    @RequestMapping(value="/getAllSubThemesTheme/{subThemeId}", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<List<SubThemeDto>> GetAllSubThemesTheme(Authentication authentication, @PathVariable String subThemeId) throws ObjectNotFoundException {
        List<SubTheme> subThemes = null;
        Theme theme = subThemeService.getThemeBySubThemeId(subThemeId , authentication.getName());
        Organiser organiser = themeService.getOrganiser(theme,authentication.getName());
        try {
            subThemes = subThemeService.getAllSubThemesTheme(subThemeId, organiser);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        List<SubThemeDto> subThemeDtos = new LinkedList<>();
        for (SubTheme subTheme : subThemes) {
            subThemeDtos.add(modelMapper.map(subTheme, SubThemeDto.class));
        }
          return new ResponseEntity<>(subThemeDtos, HttpStatus.OK);
    }

    @RequestMapping(value="/getAllByUser", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<SubThemeDto>> getSubTheme(Authentication authentication)
    {
        List<SubTheme> subthemes = null;

        //Organiser organiser = themeService.getOrganiser(theme,authentication.getName());
        try {
            subthemes = subThemeService.getSubThemesByUser(authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        List<SubThemeDto> subthemeDTOs = new LinkedList<>();
        for(SubTheme subtheme : subthemes)
        {
            subthemeDTOs.add(modelMapper.map(subtheme, SubThemeDto.class));
        }
        return new ResponseEntity<>(subthemeDTOs, HttpStatus.OK);
    }
}
