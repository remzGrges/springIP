package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.Picture;
import be.kdg.integratieproject2.bussiness.Interfaces.PictureService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
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
    public Picture getPicture(String pictureId) throws ObjectNotFoundException {
        if ( pictureId == null ||pictureId.equals("null")){
            throw new ObjectNotFoundException("null");
        }else{
            return pictureRepository.findOne(pictureId);
        }

    }

    @Override
    public void deletePicture(String pictureId) {
        pictureRepository.delete(pictureId);
    }
}
