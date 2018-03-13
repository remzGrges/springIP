package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Picture;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface PictureService {

    Picture addPicture(Picture picture);

    Picture getPicture(String pictureId) throws ObjectNotFoundException;

    void deletePicture(String pictureId);

}
