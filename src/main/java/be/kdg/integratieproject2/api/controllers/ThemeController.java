package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.api.dto.*;
import be.kdg.integratieproject2.api.themeInvitation.OnInvitationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.InvalidTokenException;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
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


    public ThemeController(ModelMapper modelMapper, ThemeService themeService, UserService userService) {
        this.modelMapper = modelMapper;
        this.themeService = themeService;
        this.userService = userService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ThemeDto> createTheme(@Valid @RequestBody ThemeDto dto, Authentication authentication)
    {
        Theme theme = modelMapper.map(dto, Theme.class);
        ThemeDto mappedTheme = modelMapper.map(themeService.addTheme(theme, authentication.getName()), ThemeDto.class);
        return new ResponseEntity<ThemeDto>(mappedTheme, HttpStatus.OK);
    }

    @RequestMapping(value="/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<ThemeDto> getTheme(@PathVariable String id, Authentication authentication) throws ObjectNotFoundException, UserNotAuthorizedException {
        ThemeDto mappedTheme;
        mappedTheme = modelMapper.map(themeService.getTheme(id,authentication.getName()), ThemeDto.class);
        return new ResponseEntity<ThemeDto>(mappedTheme, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<ThemeDto> updateTheme(@RequestBody ThemeDto dto)
    {
        Theme theme = modelMapper.map(dto, Theme.class);
        ThemeDto mappedTheme = modelMapper.map(themeService.updateTheme(theme), ThemeDto.class);
        return new ResponseEntity<ThemeDto>(mappedTheme, HttpStatus.OK);
    }

    @RequestMapping(value="/getAll", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ThemeDto>> getTheme(Authentication authentication){
        List<Theme> themes = themeService.getThemesByUser(authentication.getName());
        List<ThemeDto> themeDTOs = themes.stream().map(t -> modelMapper.map(t, ThemeDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(themeDTOs, HttpStatus.OK);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET    )
    public ResponseEntity<ThemeDto> deleteTheme(@PathVariable String id, Authentication authentication) throws ObjectNotFoundException, UserNotAuthorizedException {
        themeService.deleteTheme(id, authentication.getName());
        return new ResponseEntity<ThemeDto>(new ThemeDto(), HttpStatus.OK);
    }

    @RequestMapping(value = "/inviteOrg/{themeId}", method = RequestMethod.POST)
    public ResponseEntity inviteOrganiser(Authentication authentication, @RequestBody String email , @PathVariable String themeId, WebRequest request) throws UserAlreadyExistsException, ObjectNotFoundException, UserNotAuthorizedException {
        String appUrl = request.getContextPath();
        themeService.inviteOrganiser(themeId, authentication.getName(), email,appUrl, request.getLocale());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/acceptOrganiserInvite/{token}")
    public ResponseEntity acceptInvite(Authentication authentication, @PathVariable("token") String token) throws ObjectNotFoundException, UserAlreadyExistsException, UserNotAuthorizedException, InvalidTokenException {

        themeService.addOrganiser(authentication.getName(), token);
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping(value = "/acceptOrganiserInviteNon/{token}")
    public ResponseEntity register( @RequestParam("email") String email,@PathVariable("token") String token) throws UserAlreadyExistsException, ObjectNotFoundException, UserNotAuthorizedException, InvalidTokenException {
        themeService.addOrganiser(email, token);
        return new ResponseEntity(HttpStatus.OK);
    }
}
