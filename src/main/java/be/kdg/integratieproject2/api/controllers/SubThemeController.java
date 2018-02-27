package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.CardDto;
import be.kdg.integratieproject2.api.dto.SubThemeDto;
import be.kdg.integratieproject2.bussiness.Interfaces.SubThemeService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        SubThemeDto mappedCard = modelMapper.map(subThemeService.addSubTheme(subTheme, authentication.getName(), themeId ), SubThemeDto.class);
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
        SubTheme subTheme = subThemeService.getSubTheme(subThemeId,authentication.getName());
        SubThemeDto subThemeDto = modelMapper.map(subTheme, SubThemeDto.class);
        return new ResponseEntity<SubThemeDto>(subThemeDto, HttpStatus.FOUND);
    }
}