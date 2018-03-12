package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.bussiness.Interfaces.TokenService;
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
}


