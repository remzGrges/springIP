package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.SubThemeDto;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
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

    public SubThemeController(ModelMapper modelMapper, SubThemeService subThemeService) {
        this.modelMapper = modelMapper;
        this.subThemeService = subThemeService;
    }

    @RequestMapping(value = "/create/{themedId}", method = RequestMethod.POST)
    public ResponseEntity<SubThemeDto> createSubTheme(@RequestBody SubThemeDto dto, Authentication authentication, @PathVariable String themeId) throws BadRequestException
    {
        SubTheme subTheme = modelMapper.map(dto, SubTheme.class);
        SubThemeDto mappedCard = null;
        try {
            mappedCard = modelMapper.map(subThemeService.addSubTheme(subTheme, authentication.getName(), themeId ), SubThemeDto.class);
        } catch (ObjectNotFoundException e) {
           throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<SubThemeDto>(mappedCard, HttpStatus.CREATED);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity deleteSubTheme(Authentication authentication, @PathVariable String id)
    {
        try {
            subThemeService.deleteSubTheme(id, authentication.getName());
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/get/{subThemeId}", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<SubThemeDto> GetSubTheme(Authentication authentication, @PathVariable String subThemeId){
        SubTheme subTheme = null;
        try {
            subTheme = subThemeService.getSubTheme(subThemeId,authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        SubThemeDto subThemeDto = modelMapper.map(subTheme, SubThemeDto.class);
        return new ResponseEntity<SubThemeDto>(subThemeDto, HttpStatus.OK);
    }

    @RequestMapping(value="/getAllSubThemesTheme/{subThemeId}", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<List<SubThemeDto>> GetAllSubThemesTheme(Authentication authentication, @PathVariable String subThemeId){
        List<SubTheme> subThemes = null;
        try {
            subThemes = subThemeService.getAllSubThemesTheme(subThemeId, authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        List<SubThemeDto> subThemeDtos = new LinkedList<>();
        for (SubTheme subTheme : subThemes) {
            subThemeDtos.add(modelMapper.map(subTheme, SubThemeDto.class));
        }
          return new ResponseEntity<List<SubThemeDto>>(subThemeDtos, HttpStatus.OK);
    }

    @RequestMapping(value="/getAllByUser", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<SubThemeDto>> getSubTheme(Authentication authentication)
    {
        List<SubTheme> subthemes = subThemeService.getThemesByUser(authentication.getName());
        List<SubThemeDto> subthemeDTOs = new LinkedList<>();
        for(SubTheme subtheme : subthemes)
        {
            subthemeDTOs.add(modelMapper.map(subtheme, SubThemeDto.class));
        }
        return new ResponseEntity<List<SubThemeDto>>(subthemeDTOs, HttpStatus.OK);
    }
}
