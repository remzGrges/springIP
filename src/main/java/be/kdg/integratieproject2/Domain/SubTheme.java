package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class SubTheme extends Card {
    @Id
    private String subThemeId;
    private List<String> tags;
    private List<String> cards;

    public SubTheme() {
    }


    public String getSubThemeId() {
        return subThemeId;
    }

    public void setSubThemeId(String subThemeId) {
        this.subThemeId = subThemeId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }
}
