package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.Picture;
import org.springframework.stereotype.Service;

@Service
public interface PictureService {

    Picture addPicture(Picture picture);

    Picture getPicture(String pictureId);

    void deletePicture(String pictureId);

}
