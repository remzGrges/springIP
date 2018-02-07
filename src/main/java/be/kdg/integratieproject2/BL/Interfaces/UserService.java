package be.kdg.integratieproject2.BL.Interfaces;

import be.kdg.integratieproject2.DAL.Domain.ApplicationUser;
import be.kdg.integratieproject2.DAL.Domain.Verification.VerificationToken;

public interface UserService {
    public ApplicationUser registerUser(ApplicationUser applicationUser);

    void createVerificationToken(ApplicationUser user, String token);

    VerificationToken getVerificationToken(String token);

    void updateRegisteredUser(ApplicationUser applicationUser);

    void deleteToken(VerificationToken verificationToken);
}
