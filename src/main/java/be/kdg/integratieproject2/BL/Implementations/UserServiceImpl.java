package be.kdg.integratieproject2.BL.Implementations;

import be.kdg.integratieproject2.BL.Interfaces.UserService;
import be.kdg.integratieproject2.Domain.Verification.VerificationToken;
import be.kdg.integratieproject2.DAL.Implementations.TokenRepository;
import be.kdg.integratieproject2.DAL.Implementations.UserRepository;
import be.kdg.integratieproject2.Domain.ApplicationUser;
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

    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public ApplicationUser registerUser(ApplicationUser applicationUser) {
        ApplicationUser result = userRepository.findByEmail(applicationUser.getEmail());
        if (result != null) {
            return null;
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
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ApplicationUser applicationUser = userRepository.findByEmail(s);
        if (applicationUser == null)
            throw new UsernameNotFoundException(s);

        return new User(applicationUser.getEmail(),
                applicationUser.getPassword(),
                true,
                true,
                true,
                true,
                Collections.emptyList());
    }
}
