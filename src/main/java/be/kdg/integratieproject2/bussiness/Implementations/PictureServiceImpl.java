package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.Picture;
import be.kdg.integratieproject2.bussiness.Interfaces.PictureService;
import be.kdg.integratieproject2.data.implementations.PictureRepository;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl implements PictureService {
    private PictureRepository pictureRepository;

    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Picture addPicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public Picture getPicture(String pictureId) {
        return pictureRepository.findOne(pictureId);
    }

    @Override
    public void deletePicture(String pictureId) {
        pictureRepository.delete(pictureId);
    }
}
