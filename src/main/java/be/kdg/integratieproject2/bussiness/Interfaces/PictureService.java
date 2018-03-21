package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Picture;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import org.springframework.stereotype.Service;

@Service
public interface PictureService {

    Picture addPicture(Picture picture, String userName);

    Picture getPicture(String pictureId, String userName) throws ObjectNotFoundException;

    void deletePicture(String pictureId, String userName) throws ObjectNotFoundException;

    Picture getPictureByUsername(String username) throws ObjectNotFoundException;
}
