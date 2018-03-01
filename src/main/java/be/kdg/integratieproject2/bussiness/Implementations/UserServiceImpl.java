package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.ProfilePicture;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.NoProfilePictureFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserAlreadyExistsException;
import be.kdg.integratieproject2.data.implementations.ProfilePictureRepository;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import be.kdg.integratieproject2.data.implementations.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private UserRepository userRepository;

    private TokenRepository tokenRepository;

    private ProfilePictureRepository profilePictureRepository;

    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, ProfilePictureRepository profilePictureRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.profilePictureRepository = profilePictureRepository;
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
        return tokenRepository.findByToken(token);
    }

    @Override
    public ApplicationUser getUserByUsername(String s) throws UsernameNotFoundException
    {
        ApplicationUser applicationUser = userRepository.findByEmail(s);
        if (applicationUser == null)
            throw new UsernameNotFoundException(s);

        return applicationUser;
    }

    @Override
    public void updateRegisteredUserName(ApplicationUser user, String voornaam) {
        user.setFirstName(voornaam);
        userRepository.save(user);
    }

    @Override
    public ProfilePicture uploadProfilePicture(String username, ProfilePicture profilePicture) {
        ApplicationUser user = this.getUserByUsername(username);
        ProfilePicture existingProfilePicture = profilePictureRepository.findByUserId(user.getId());

        if (existingProfilePicture != null){
            existingProfilePicture.setFilename(profilePicture.getFilename());
            existingProfilePicture.setFiletype(profilePicture.getFiletype());
            existingProfilePicture.setValue(profilePicture.getValue());
            existingProfilePicture = profilePictureRepository.save(existingProfilePicture);
        } else {
            profilePicture.setUserId(user.getId());
            existingProfilePicture = profilePictureRepository.save(profilePicture);
        }

        return existingProfilePicture;
    }

    @Override
    public ProfilePicture getProfilePicture(String username) throws NoProfilePictureFoundException {
        ApplicationUser user = this.getUserByUsername(username);

        ProfilePicture existingProfilePicture = profilePictureRepository.findByUserId(user.getId());
        if (existingProfilePicture == null) {
            throw new NoProfilePictureFoundException("This user does not have a profile picture.");
        }

        return existingProfilePicture;

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
