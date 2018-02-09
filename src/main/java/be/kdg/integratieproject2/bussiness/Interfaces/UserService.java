package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.domain.ApplicationUser;
import be.kdg.integratieproject2.domain.verification.VerificationToken;

public interface UserService {
    public ApplicationUser registerUser(ApplicationUser applicationUser);

    void createVerificationToken(ApplicationUser user, String token);

    VerificationToken getVerificationToken(String token);

    void updateRegisteredUser(ApplicationUser applicationUser);

    void deleteToken(VerificationToken verificationToken);
}
