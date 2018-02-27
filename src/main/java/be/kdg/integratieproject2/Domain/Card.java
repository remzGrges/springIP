package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Card {
    @Id
    private String id;
    private String text;
    private String userId;
    private List<String> themes;

    public Card() {
    }

    public List<String> getThemes() {
        return themes;
    }

    public void setThemes(List<String> themes) {
        this.themes = themes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
