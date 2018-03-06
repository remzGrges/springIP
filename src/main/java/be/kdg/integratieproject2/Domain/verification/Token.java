package be.kdg.integratieproject2.Domain.verification;

import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Token {

    private static final int EXPIRATION = 60 * 24;

    @Id
    private String id;

    private String token;
    private Date expiryDate;


    public Token( String token) {

        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }


}
