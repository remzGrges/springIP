package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.Picture;
import be.kdg.integratieproject2.bussiness.Interfaces.PictureService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.data.implementations.PictureRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl implements PictureService {
    private PictureRepository pictureRepository;
    private UserService userService;

    public PictureServiceImpl(PictureRepository pictureRepository, UserService userService) {
        this.userService = userService;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Picture addPicture(Picture picture, String userName) {
        if (userService.getUserByUsername(userName).getEmail() != null) {
            return pictureRepository.save(picture);
        } else {
            throw new UsernameNotFoundException(userName);
        }
    }

    @Override
    public Picture getPicture(String pictureId, String userName) throws ObjectNotFoundException {
        if (pictureId == null || pictureId.equals("null")) {
            throw new ObjectNotFoundException("null");
        } else {
            if (userService.getUserByUsername(userName).getEmail() != null) {
                return pictureRepository.findOne(pictureId);
            } else {
                throw new UsernameNotFoundException(userName);
            }
        }

    }

    @Override
    public void deletePicture(String pictureId, String userName) throws ObjectNotFoundException {
        if (pictureId != null || !pictureId.equals("null")) {
            if (userService.getUserByUsername(userName).getEmail() != null) {
                pictureRepository.delete(pictureId);
            }else {
                throw new UsernameNotFoundException(userName);
            }
        }else{
            throw new ObjectNotFoundException(pictureId);
        }
    }
}
