package be.kdg.integratieproject2.API.Controllers;

import be.kdg.integratieproject2.API.Verification.OnRegistrationCompleteEvent;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.API.Dto.UserRegistrationDto;
import be.kdg.integratieproject2.BL.Interfaces.UserService;
import be.kdg.integratieproject2.Domain.Verification.VerificationToken;
import com.fasterxml.jackson.core.JsonParser;
import com.mongodb.util.JSON;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import springfox.documentation.spring.web.json.Json;

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
            return "Failure type 2";
        }

        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        ApplicationUser user = modelMapper.map(dto, ApplicationUser.class);

        user = userService.registerUser(user);

        if (user == null){
            return "Failure type 1";
        }

        try {
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

    @PostMapping("/update")
    public String changeName(Authentication authentication, @Valid @RequestBody String newName ) {
        String username = authentication.getName();


        userService.updateRegisteredUserName(userService.getUserByUsername(username), newName);
        return "changed name succesfully";
    }





}
