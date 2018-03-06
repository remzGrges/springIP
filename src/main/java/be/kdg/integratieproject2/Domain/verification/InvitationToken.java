package be.kdg.integratieproject2.Domain.verification;

import be.kdg.integratieproject2.Application;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class InvitationToken extends VerificationToken{
    private static final int EXPIRATION = 60 * 24;

    @Id
    private String id;

    private String token;
    private ApplicationUser email;
    private String themeId;
    private Date expiryDate;

    public InvitationToken(String token, ApplicationUser email, String themeId) {
        super(email, token);
        this.themeId = themeId;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(ApplicationUser email) {
        this.email = email;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public ApplicationUser getEmail() {
        return email;
    }

    public String getThemeId() {
        return themeId;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
