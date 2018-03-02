package be.kdg.integratieproject2.api.controllers;


import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Verification.VerificationToken;
import be.kdg.integratieproject2.api.dto.UserRegistrationDto;
import be.kdg.integratieproject2.api.dto.UserInfoDto;
import be.kdg.integratieproject2.api.dto.UserRegistrationDto;
import be.kdg.integratieproject2.api.verification.OnRegistrationCompleteEvent;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mongodb.util.JSON;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/update")
    public String changeName(Authentication authentication, @Valid @RequestBody String newName ) throws JSONException {
        String username = authentication.getName();

        final JSONObject obj = new JSONObject(newName);
        userService.updateRegisteredUserName(userService.getUserByUsername(username), obj.getString("newName"));

        return "changed name succesfully";
    }

   @RequestMapping(value = "/currentuser", method = RequestMethod.GET)
    public ResponseEntity<UserInfoDto> getCurrentUserName(Authentication authentication) {
        String userName = authentication.getName();
        ApplicationUser user2 = userService.getUserByUsername(userName);
        UserInfoDto userDto = modelMapper.map(user2, UserInfoDto.class);

        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }



}
