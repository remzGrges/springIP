package be.kdg.integratieproject2.Domain.verification;

import be.kdg.integratieproject2.Application;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class InvitationToken extends Token{



    private String email;
    private String themeId;

    public InvitationToken(String token, String email, String themeId) {
        super(token);
        this.email = email;

        this.themeId = themeId;


    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }


}
