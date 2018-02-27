package be.kdg.integratieproject2.api.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SubThemeDto {
    @NotNull
    @NotEmpty
    private String subThemeId;
    private List<String> tags;
    private List<String> cards;

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
