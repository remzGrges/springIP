package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.ProfilePicture;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.NoProfilePictureFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import be.kdg.integratieproject2.data.implementations.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private UserRepository userRepository;

    private TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public ApplicationUser registerUser(ApplicationUser applicationUser) throws UserAlreadyExistsException {
        ApplicationUser result = userRepository.findByEmail(applicationUser.getEmail());
        if (result != null) {
            throw new UserAlreadyExistsException("User Already Exists");
        }
        return userRepository.save(applicationUser);
    }

    @Override
    public void updateRegisteredUser(ApplicationUser applicationUser) {
        userRepository.save(applicationUser);
    }

    @Override
    public void deleteToken(VerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    }

    @Override
    public void createVerificationToken(ApplicationUser user, String token) {
        tokenRepository.save(new VerificationToken(user, token));
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return (VerificationToken) tokenRepository.findByToken(token);
    }

    @Override
    public ApplicationUser getUserByUsername(String s) throws UsernameNotFoundException {
        ApplicationUser applicationUser = userRepository.findByEmail(s);
        if (applicationUser == null)
            throw new UsernameNotFoundException(s);

        return applicationUser;
    }

    /*@Override
    public ApplicationUser updateRegisteredUserName(String user, String s) {
        return null;
    }

    @Override
    public ProfilePicture uploadProfilePicture(String username, ProfilePicture profilePicture) {
        return null;
    }

    @Override
    public ProfilePicture getProfilePicture(String username) throws NoProfilePictureFoundException {
        return null;
    }

    @Override
    public List<ApplicationUser> getAllUsers() {
        return null;
    }*/

    @Override
    public ApplicationUser updateRegisteredUserName(String username, String voornaam, String pictureId) {
        ApplicationUser user = this.getUserByUsername(username);
        user.setFirstName(voornaam);
        user.setPictureId(pictureId);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ApplicationUser applicationUser = userRepository.findByEmail(s);
        if (applicationUser == null)
            throw new UsernameNotFoundException(s);

        return new User(applicationUser.getEmail(),
                applicationUser.getPassword(),
                applicationUser.getEnabled(),
                true,
                true,
                true,
                Collections.emptyList());
    }
}