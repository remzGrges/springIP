package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Picture;
import be.kdg.integratieproject2.bussiness.Interfaces.PictureService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.data.implementations.PictureRepository;
import be.kdg.integratieproject2.data.implementations.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl implements PictureService {
    private PictureRepository pictureRepository;
    private UserRepository userRepository;

    public PictureServiceImpl(PictureRepository pictureRepository, UserRepository userRepository) {
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Picture addPicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public Picture getPicture(String pictureId) throws ObjectNotFoundException {
        if (pictureId != null){
            return pictureRepository.findOne(pictureId);
        }else{
            throw new ObjectNotFoundException("null");
        }

    }

    @Override
    public void deletePicture(String pictureId) {
        pictureRepository.delete(pictureId);
    }

    @Override
    public Picture getPictureByUsername(String username) throws ObjectNotFoundException {
        ApplicationUser user = userRepository.findByEmail(username);
        return this.getPicture(user.getPictureId());
    }
}
