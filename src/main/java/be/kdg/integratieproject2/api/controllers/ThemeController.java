package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.OrganiserDto;
import be.kdg.integratieproject2.api.dto.ThemeDto;
import be.kdg.integratieproject2.api.invitation.OnInvitationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import jdk.net.SocketFlow;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
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
    public ResponseEntity<ThemeDto> createTheme(@RequestBody ThemeDto dto, Authentication authentication) throws BadRequestException {
        Theme theme = modelMapper.map(dto, Theme.class);
        ThemeDto mappedTheme = modelMapper.map(themeService.addTheme(theme, authentication.getName()), ThemeDto.class);
        return new ResponseEntity<ThemeDto>(mappedTheme, HttpStatus.OK);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ThemeDto>> getTheme(Authentication authentication) throws ObjectNotFoundException {
        List<Theme> themes;
        try {
            themes = themeService.getThemesByUser(authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        List<ThemeDto> themeDTOs = new LinkedList<>();
        for (Theme theme : themes) {
            themeDTOs.add(modelMapper.map(theme, ThemeDto.class));
        }
        return new ResponseEntity<>(themeDTOs, HttpStatus.OK);
    }

    //TODO: GET = DELETE, geen DTO teruggeven
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<ThemeDto> deleteTheme(Authentication authentication, @PathVariable String id) throws BadRequestException, ObjectNotFoundException {
        try {
            themeService.deleteTheme(id);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        themeService.deleteTheme(id);
        return new ResponseEntity<ThemeDto>(new ThemeDto(), HttpStatus.OK);
    }

    @RequestMapping(value = "/inviteOrg", method = RequestMethod.POST)
    public ResponseEntity inviteOrganiser(Authentication authentication, @RequestBody OrganiserDto themeInvitationDto, BindingResult result, WebRequest request) throws UserAlreadyExistsException, ObjectNotFoundException {


        Organiser organiser = modelMapper.map(themeInvitationDto, Organiser.class);
        //OrganiserDto mappedOrganiser = modelMapper.map(themeService.addOrganiser(organiser.getThemeID(), authentication.getName(), organiser.getEmail()) , OrganiserDto.class);

        Organiser newOrganiser = themeService.addOrganiser(organiser.getThemeID(), authentication.getName(), organiser.getEmail());
/*
        String userName = authentication.getName();
*/
        //Theme theme = themeService.getTheme(themaId);
        //themeService.addOrganiser(themaId, userName, email);
        String appUrl = request.getContextPath();

        try {

            if (newOrganiser != null) {
                eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.getUserByUsername(themeInvitationDto.getEmail()), request.getLocale(), appUrl, themeInvitationDto.getThemeID()));
            }
        } catch (Exception e) {
            ApplicationUser user = new ApplicationUser();
            user.setEmail(themeInvitationDto.getEmail());
            eventPublisher.publishEvent(new OnInvitationCompleteEvent(userService.registerUser(user), request.getLocale(), appUrl, themeInvitationDto.getThemeID()));

        }


        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/acceptOrganiserInvite")
    public ResponseEntity acceptInvite(Authentication authentication, @RequestParam("token") String token) throws ObjectNotFoundException {
        InvitationToken verificationToken = themeService.getInvitationToken(token);
        if (verificationToken == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        String themeId = verificationToken.getThemeId();

        String email = verificationToken.getEmail();


        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        Organiser organiser = themeService.getOrganiser(themeId, authentication.getName());

        organiser.setEnabled(true);

        if (organiser.getEmail().equals(email)) {
            themeService.updateExistingOrganiser(organiser);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public ResponseEntity<OrganiserDto> register(@RequestHeader("token") String token, @RequestHeader("themeId") String id) throws ObjectNotFoundException {
        //InvitationToken invitationToken = themeService.ge
        return null;
    }




}
