package be.kdg.integratieproject2.Domain;


import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */
public class Theme {
    @Id
    private String id;
    private String name;
    private String description;
    private List<String> tags;
    private List<Organiser> organisers;
    private List<Card> cards;
    private List<SubTheme> subThemes;

    public Theme() {
    }

    public List<SubTheme> getSubThemes() {
        return subThemes;
    }

    public void setSubThemes(List<SubTheme> subThemes) {
        this.subThemes = subThemes;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Organiser> getOrganisers() {
        return organisers;
    }

    public void setOrganisers(List<Organiser> organisers) {
        this.organisers = organisers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
