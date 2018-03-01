package be.kdg.integratieproject2.api.controllers;


import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.ProfilePicture;
import be.kdg.integratieproject2.api.dto.PictureDto;
import be.kdg.integratieproject2.api.dto.UserRegistrationDto;
import be.kdg.integratieproject2.Domain.verification.*;
import be.kdg.integratieproject2.api.dto.UserInfoDto;
import be.kdg.integratieproject2.api.verification.OnRegistrationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.NoProfilePictureFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Calendar;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ModelMapper modelMapper;
    private ApplicationEventPublisher eventPublisher;

    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody UserRegistrationDto dto, BindingResult result, WebRequest request) {
        if (result.hasErrors()){
            return "Body not valid";
        }

        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        ApplicationUser user = modelMapper.map(dto, ApplicationUser.class);

        try {
            user = userService.registerUser(user);
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Succes";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token) {

      VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return "No Such Token";
        }

        ApplicationUser applicationUser = verificationToken.getApplicationUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "Token expired";
        }

        applicationUser.setEnabled(true);
        userService.updateRegisteredUser(applicationUser);
        userService.deleteToken(verificationToken);
        return "User verified";
    }

    @PostMapping(value = "/update")
    public UserInfoDto changeName(Authentication authentication, @Valid @RequestBody UserInfoDto dto ) {
        String username = authentication.getName();

        ApplicationUser user = userService.updateRegisteredUserName(username, dto.getFirstName());
        dto = modelMapper.map(user, UserInfoDto.class);

        return dto;
    }

   @RequestMapping(value = "/currentuser", method = RequestMethod.GET)
    public UserInfoDto getCurrentUserName(Authentication authentication) {
        String username = authentication.getName();
        ApplicationUser user = userService.getUserByUsername(username);
        UserInfoDto dto = modelMapper.map(user, UserInfoDto.class);

        return dto;
    }

    @PostMapping(value = "/uploadProfilePicture")
    public PictureDto uploadProfilePicture(Authentication authentication, @Valid @RequestBody PictureDto dto ) {
        String username = authentication.getName();
        ProfilePicture profilePicture = modelMapper.map(dto, ProfilePicture.class);
        profilePicture = userService.uploadProfilePicture(username, profilePicture);
        dto = modelMapper.map(profilePicture, PictureDto.class);
        return dto;
    }

    @GetMapping(value = "/getProfilePicture")
    public PictureDto getProfilePicture(Authentication authentication){
        String username = authentication.getName();
        ProfilePicture profilePicture = null;
        try {
            profilePicture = userService.getProfilePicture(username);
        } catch (NoProfilePictureFoundException e) {
            return null;
        }
        return modelMapper.map(profilePicture, PictureDto.class);
    }
}
