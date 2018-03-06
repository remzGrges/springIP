package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Card {

    @Id
    private String id;
    private String text;

    public Card() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
