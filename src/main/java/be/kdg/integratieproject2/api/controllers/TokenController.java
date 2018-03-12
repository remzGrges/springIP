package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.bussiness.Interfaces.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {

    TokenService service;

    public TokenController(TokenService service) {
        this.service = service;
    }

    @RequestMapping(value = "/create/{token}", method = RequestMethod.GET)
    public ResponseEntity<String> getToken( Authentication authentication, @PathVariable("token") String token ) throws BadRequestException
    {

        String soort = service.getTokenSort(token);
        return new ResponseEntity<String>( soort, HttpStatus.OK);

    }
}


