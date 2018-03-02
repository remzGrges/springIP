package be.kdg.integratieproject2.Domain;

import java.util.List;

public class SubTheme extends Card {
    private List<String> tags;
    private List<Card> cards;

    public SubTheme() {
    }


    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
