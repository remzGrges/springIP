package be.kdg.integratieproject2.api.controllers;


import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import be.kdg.integratieproject2.api.dto.UserRegistrationDto;
import be.kdg.integratieproject2.api.dto.UserInfoDto;
import be.kdg.integratieproject2.api.verification.OnRegistrationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
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
    public String register(@Valid @RequestBody UserRegistrationDto dto, BindingResult result, WebRequest request) throws UserAlreadyExistsException {
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
    public UserInfoDto confirmRegistration(@RequestParam("token") String token) {

       VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return new UserInfoDto();
        }

        ApplicationUser applicationUser = verificationToken.getApplicationUser();
        UserInfoDto dto = modelMapper.map(applicationUser, UserInfoDto.class);

        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new UserInfoDto();
        }

        applicationUser.setEnabled(true);
        userService.updateRegisteredUser(applicationUser);
        userService.deleteToken(verificationToken);
        return dto;
    }

    @PostMapping(value = "/update")
    public UserInfoDto changeName(Authentication authentication, @Valid @RequestBody UserInfoDto dto ) {
        String username = authentication.getName();

        ApplicationUser user = userService.updateRegisteredUserName(username, dto.getFirstName(),dto.getPictureId());
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

}
