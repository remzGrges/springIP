package be.kdg.integratieproject2.Domain.verification;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class VerificationToken extends Token {

    private ApplicationUser applicationUser;
    private Date expiryDate;

    public VerificationToken(ApplicationUser applicationUser, String token) {
        super(token);
        this.applicationUser = applicationUser;


    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }


}
