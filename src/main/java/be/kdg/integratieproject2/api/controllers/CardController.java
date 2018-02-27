package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.CardDto;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private ModelMapper modelMapper;
    private CardService cardService;

    public CardController(ModelMapper modelMapper, CardService cardService) {
        this.modelMapper = modelMapper;
        this.cardService = cardService;
    }

    @RequestMapping(value = "/create/{themeId}", method = RequestMethod.POST)
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto dto, Authentication authentication, @PathVariable String themeId) throws BadRequestException
    {
        Card card = modelMapper.map(dto, Card.class);
        CardDto mappedCard = modelMapper.map(cardService.addCard(card, authentication.getName(), themeId ), CardDto.class);
        return new ResponseEntity<CardDto>(mappedCard, HttpStatus.CREATED);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity deleteCard(Authentication authentication, @PathVariable String id)
    {
        try {
            cardService.deleteCard(id, authentication.getName());
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/getAllCardsTheme/{themeId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CardDto>> getAllCardsTheme(Authentication authentication, @PathVariable String themeId)
    {
        List<Card> cards = cardService.getCardsByTheme(themeId,authentication.getName());
        List<CardDto> cardDtos = new LinkedList<>();
        for (Card card : cards) {
            cardDtos.add(modelMapper.map(card, CardDto.class));
        }
        return new ResponseEntity<List<CardDto>>(cardDtos, HttpStatus.FOUND);
    }

    @RequestMapping(value="/getAll", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CardDto>> GetAll(Authentication authentication)
    {
        List<Card> cards = cardService.getAll();
        List<CardDto> cardDtos = new LinkedList<>();
        for (Card card : cards) {
            cardDtos.add(modelMapper.map(card, CardDto.class));
        }
        return new ResponseEntity<List<CardDto>>(cardDtos, HttpStatus.FOUND);
    }



}
