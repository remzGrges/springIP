package be.kdg.integratieproject2.api.dto;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CardDto {

    @NotNull
    @NotEmpty
    private String Id;
    @NotNull
    @NotEmpty
    private String text;
    @NotNull
    @NotEmpty
    private String userId;

    public CardDto() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
