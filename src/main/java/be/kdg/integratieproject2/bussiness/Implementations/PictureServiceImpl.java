package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Picture;
import be.kdg.integratieproject2.bussiness.Interfaces.PictureService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import be.kdg.integratieproject2.data.implementations.PictureRepository;
import be.kdg.integratieproject2.data.implementations.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl implements PictureService {
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @Override
    public Picture addPicture(Picture picture, String userName) {
        if (userService.getUserByUsername(userName).getEmail() != null) return pictureRepository.save(picture);
        else throw new UsernameNotFoundException(userName);
    }

    @Override
    public Picture getPicture(String pictureId, String userName) throws ObjectNotFoundException {
        if (pictureId == null || pictureId.equals("null")) throw new ObjectNotFoundException("null");
        else return pictureRepository.findOne(pictureId);

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

    @Override
    public Picture getPictureByUsername(String username) throws ObjectNotFoundException {
        ApplicationUser user = userRepository.findByEmailIgnoreCase(username);
        if(user.getPictureId() == null) throw new ObjectNotFoundException("null"); //TODO: dit moet niet altijd gelogd worden
        return pictureRepository.findOne(user.getPictureId());
    }
}
