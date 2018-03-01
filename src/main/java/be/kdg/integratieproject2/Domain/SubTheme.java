package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class SubTheme extends Card {
    private boolean isSubTheme;
    private List<String> tags;
    private List<String> cards;

    public SubTheme() {
    }

    public boolean isSubTheme() {
        return isSubTheme;
    }

    public void setSubTheme(boolean subTheme) {
        isSubTheme = subTheme;
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
