package be.kdg.integratieproject2.api.controllers;

import be.kdg.integratieproject2.Domain.Picture;
import be.kdg.integratieproject2.api.dto.PictureDto;
import be.kdg.integratieproject2.bussiness.Interfaces.PictureService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/pictures")
public class    PictureController {
    ModelMapper modelMapper;
    PictureService pictureService;

    public PictureController(ModelMapper modelMapper, PictureService pictureService) {
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public PictureDto createPicture(@Valid @RequestBody PictureDto dto, Authentication authentication) {
        Picture picture = modelMapper.map(dto, Picture.class);
        picture = pictureService.addPicture(picture);
        dto = modelMapper.map(picture, PictureDto.class);
        return dto;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<PictureDto> deleteTheme(@PathVariable String id) throws ObjectNotFoundException {
        pictureService.deletePicture(id);
        return new ResponseEntity<PictureDto>(new PictureDto(), HttpStatus.OK);
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public PictureDto getPicture(@PathVariable String id) throws ObjectNotFoundException {
        if (id == null) {
            throw new ObjectNotFoundException("null");
        }
        Picture picture = pictureService.getPicture(id);
        return modelMapper.map(picture, PictureDto.class);
    }

    @RequestMapping(value = "/getByUsername/{username:.+}", method = RequestMethod.GET)
    public PictureDto getPictureByUsername(@PathVariable String username) throws ObjectNotFoundException {
        Picture picture = pictureService.getPictureByUsername(username);
        return modelMapper.map(picture, PictureDto.class);
    }
}

