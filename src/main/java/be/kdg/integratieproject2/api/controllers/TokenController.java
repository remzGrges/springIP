package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.api.error.BadRequestException;
import be.kdg.integratieproject2.bussiness.Interfaces.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    TokenService service;
    ModelMapper modelMapper;

    public TokenController(TokenService service, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.service = service;
    }

    @RequestMapping(value = "/create/{token}", method = RequestMethod.GET, produces = "text/plain")
    public String getToken(@PathVariable("token") String token ) throws BadRequestException
    {
        String soort = service.getTokenSort(token);
        return String.format("\"%s\"", soort);
    }
}


