package be.kdg.integratieproject2.BL.Interfaces;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Verification.VerificationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    public ApplicationUser registerUser(ApplicationUser applicationUser);

    void createVerificationToken(ApplicationUser user, String token);

    VerificationToken getVerificationToken(String token);

    void updateRegisteredUser(ApplicationUser applicationUser);

    void deleteToken(VerificationToken verificationToken);
    ApplicationUser getUserByUsername(String s) throws UsernameNotFoundException;

    void updateRegisteredUserName(ApplicationUser user, String s);
}
