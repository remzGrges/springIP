package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.ThemeDto;
import be.kdg.integratieproject2.api.dto.ThemeInvitationDto;
import be.kdg.integratieproject2.api.invitation.OnInvitationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import sun.rmi.runtime.Log;

import java.util.HashMap;
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
    private UserService userService;
    private ApplicationEventPublisher eventPublisher;


    public ThemeController(ModelMapper modelMapper, ThemeService themeService, UserService userService, ApplicationEventPublisher eventPublisher) {
        this.modelMapper = modelMapper;
        this.themeService = themeService;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
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
        List<Theme> themes = themeService.getThemesByUser(authentication.getName());
        List<ThemeDto> themeDTOs = new LinkedList<>();
        for(Theme theme : themes)
        {
            themeDTOs.add(modelMapper.map(theme, ThemeDto.class));
        }
        return new ResponseEntity<List<ThemeDto>>(themeDTOs, HttpStatus.OK);
    }
    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity deleteTheme(Authentication authentication, @PathVariable String id) throws BadRequestException
    {
        themeService.deleteTheme(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/inviteOrg" , method = RequestMethod.POST)
    public ResponseEntity inviteOrganiser(Authentication authentication, @RequestBody HashMap<String, String> themeInvitationDto, BindingResult result, WebRequest request) {


        String userName = authentication.getName();
        //Theme theme = themeService.getTheme(themaId);
        //themeService.addOrganiser(themaId, userName, email);
        String appUrl = request.getContextPath();

        eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.getUserByUsername(themeInvitationDto.get("email")),request.getLocale(), appUrl, themeInvitationDto.get("themaId")));



        return new ResponseEntity(HttpStatus.OK);
    }
}
