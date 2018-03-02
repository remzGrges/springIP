package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.SubTheme;
import be.kdg.integratieproject2.api.BadRequestException;
import be.kdg.integratieproject2.api.dto.CardDto;
import be.kdg.integratieproject2.api.dto.SubThemeDto;
import be.kdg.integratieproject2.bussiness.Interfaces.CardService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        CardDto mappedCard = null;
        try {
            mappedCard = modelMapper.map(cardService.addCard(card, authentication.getName(), themeId ), CardDto.class);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<>(mappedCard, HttpStatus.OK);
    }

    @RequestMapping(value="/createAtSubtheme/{subThemeId}", method=RequestMethod.POST)
    public ResponseEntity<CardDto> createCardAtSubTheme(@RequestBody CardDto dto, Authentication authentication, @PathVariable String subThemeId) throws BadRequestException
    {
        Card card = modelMapper.map(dto, Card.class);
        CardDto mappedCard = null;
        try {
            mappedCard = modelMapper.map(cardService.addCardAtSubTheme(card, authentication.getName(), subThemeId ), CardDto.class);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<>(mappedCard, HttpStatus.OK);
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public ResponseEntity<CardDto> updateCard(Authentication authentication, @RequestBody CardDto dto)
    {
        Card card = modelMapper.map(dto, Card.class);
        CardDto mappedCard = null;
        try {
            mappedCard = modelMapper.map(cardService.updateCard(card, authentication.getName()), CardDto.class);
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<>(mappedCard, HttpStatus.OK);
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity deleteCard(Authentication authentication, @PathVariable String id)
    {
        try {
            cardService.deleteCard(id, authentication.getName());
        }
        catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/getAllCardsTheme/{themeId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CardDto>> getAllCardsTheme(Authentication authentication, @PathVariable String themeId)
    {
        List<Card> cards;
        try {
            cards = cardService.getCardsByTheme(themeId,authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        List<CardDto> cardDtos = new LinkedList<>();
        for (Card card : cards) {
            cardDtos.add(modelMapper.map(card, CardDto.class));
        }
        return new ResponseEntity<>(cardDtos, HttpStatus.OK);
    }
    @RequestMapping(value="/getAll", method=RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<SubThemeDto>> getAllCards(Authentication authentication)
    {
        List<SubTheme> allCards;
        try {
            allCards = cardService.getAllCards(authentication.getName());
        } catch (ObjectNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        List<SubThemeDto> subThemeDtos = new ArrayList<>();
        for(SubTheme subTheme : allCards)
        {
            subThemeDtos.add(modelMapper.map(subTheme, SubThemeDto.class));
        }
        return new ResponseEntity<>(subThemeDtos, HttpStatus.OK);
    }
}
