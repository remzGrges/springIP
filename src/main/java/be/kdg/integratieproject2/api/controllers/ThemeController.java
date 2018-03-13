package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.*;
import be.kdg.integratieproject2.api.themeInvitation.OnInvitationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @RequestMapping(value = "/inviteOrg/{themeId}", method = RequestMethod.POST)
    public ResponseEntity inviteOrganiser(Authentication authentication, @RequestBody String email , @PathVariable String themeId, BindingResult result, WebRequest request) throws UserAlreadyExistsException, ObjectNotFoundException {

        ApplicationUser user;
        String appUrl = request.getContextPath();

        try {
            user = userService.getUserByUsername(email);
            if (user.getEmail() != null) {
                eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.getUserByUsername(email), request.getLocale(), appUrl, themeId));
            }
        } catch (UsernameNotFoundException a) {
            ApplicationUser newUser = new ApplicationUser();
            newUser.setEmail(email)
;            eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.registerUser(newUser), request.getLocale(), appUrl, themeId));

        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/acceptOrganiserInvite/{token}")
    public ResponseEntity acceptInvite(Authentication authentication, @PathVariable("token") String token) throws ObjectNotFoundException, UserAlreadyExistsException {

        themeService.addOrganiser(authentication.getName(), token);
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping(value = "/acceptOrganiserInviteNon/{token}")
    public ResponseEntity register( @RequestParam("email") String email,@PathVariable("token") String token) throws UserAlreadyExistsException, ObjectNotFoundException {
        InvitationToken invitationToken = themeService.getInvitationToken(token);
        themeService.addOrganiser(email, token);
        return new ResponseEntity(HttpStatus.OK);
    }




}
