package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by Tim on 14/03/2018.
 */
public class Turn {
    @Id
    private String id;
    private String userId;
    private String cardId;
    private Date timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
