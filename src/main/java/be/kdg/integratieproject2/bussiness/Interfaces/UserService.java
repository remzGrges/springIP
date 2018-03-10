package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.ProfilePicture;
import be.kdg.integratieproject2.Domain.verification.Token;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import be.kdg.integratieproject2.bussiness.exceptions.NoProfilePictureFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public ApplicationUser registerUser(ApplicationUser applicationUser) throws UserAlreadyExistsException;

    void createVerificationToken(ApplicationUser user, String token);

    VerificationToken getVerificationToken(String token);

    void updateRegisteredUser(ApplicationUser applicationUser);

    void deleteToken(VerificationToken verificationToken);

    ApplicationUser getUserByUsername(String s) throws UsernameNotFoundException;

//    ApplicationUser updateRegisteredUserName(String user, String s);
//
//    ProfilePicture uploadProfilePicture(String username, ProfilePicture profilePicture);
//
//    ProfilePicture getProfilePicture(String username) throws NoProfilePictureFoundException;
//
//    List<ApplicationUser> getAllUsers();
    ApplicationUser updateRegisteredUserName(String user, String s, String picId);
}
