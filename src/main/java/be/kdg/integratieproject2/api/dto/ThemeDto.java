package be.kdg.integratieproject2.api.dto;

import be.kdg.integratieproject2.Domain.Card;
import be.kdg.integratieproject2.Domain.SubTheme;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Tim on 09/02/2018.
 */
public class ThemeDto {
    @NotNull
    @NotEmpty
    private String id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String description;



    private List<String> organisers;

    private List<String> tags;
    private List<Card> cards;
    private List<SubTheme> subThemes;

    public ThemeDto() {
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getOrganisers() {
        return organisers;
    }

    public void setOrganisers(List<String> organisers) {
        this.organisers = organisers;
    }

    public List<SubTheme> getSubThemes() {
        return subThemes;
    }

    public void setSubThemes(List<SubTheme> subThemes) {
        this.subThemes = subThemes;
    }



}
