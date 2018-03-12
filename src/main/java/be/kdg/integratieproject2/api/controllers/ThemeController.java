package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.ThemeDto;
import be.kdg.integratieproject2.api.invitation.OnInvitationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        return new ResponseEntity<ThemeDto>(mappedTheme, HttpStatus.OK);
    }

    @RequestMapping(value="/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<ThemeDto> getTheme(@PathVariable String id, Authentication authentication) throws BadRequestException
    {
        ThemeDto mappedTheme = null;
        try {
            mappedTheme = modelMapper.map(themeService.getTheme(id), ThemeDto.class);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<ThemeDto>(mappedTheme, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<ThemeDto> updateTheme(@RequestBody ThemeDto dto) throws BadRequestException
    {
        Theme theme = modelMapper.map(dto, Theme.class);
        ThemeDto mappedTheme = modelMapper.map(themeService.updateTheme(theme), ThemeDto.class);
        return new ResponseEntity<ThemeDto>(mappedTheme, HttpStatus.OK);
    }

    @RequestMapping(value="/getAll", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ThemeDto>> getTheme(Authentication authentication) throws ObjectNotFoundException {
        List<Theme> themes;
        try {
            themes = themeService.getThemesByUser(authentication.getName());
        }
        catch(ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        List<ThemeDto> themeDTOs = themes.stream().map(t -> modelMapper.map(t, ThemeDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(themeDTOs, HttpStatus.OK);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET    )
    public ResponseEntity<ThemeDto> deleteTheme(@PathVariable String id) throws ObjectNotFoundException{
        themeService.deleteTheme(id);

        return new ResponseEntity<ThemeDto>(new ThemeDto(), HttpStatus.OK);
    }

    @RequestMapping(value = "/inviteOrg" , method = RequestMethod.POST)
    public ResponseEntity inviteOrganiser(Authentication authentication, @RequestBody HashMap<String, String> themeInvitationDto, BindingResult result, WebRequest request) throws UserAlreadyExistsException {


        String userName = authentication.getName();
        //Theme theme = themeService.getTheme(themaId);
        //themeService.addOrganiser(themaId, userName, email);
        String appUrl = request.getContextPath();

        try {
            eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.getUserByUsername(themeInvitationDto.get("username")), request.getLocale(), appUrl, themeInvitationDto.get("themaId")));

        } catch (Exception e) {
            ApplicationUser user = new ApplicationUser();
            user.setEmail(themeInvitationDto.get("username"));
            eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.registerUser(user), request.getLocale(), appUrl, themeInvitationDto.get("themaId")));

        }


        return new ResponseEntity(HttpStatus.OK);
    }
}
