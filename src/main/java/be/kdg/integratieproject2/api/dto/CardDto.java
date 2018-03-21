package be.kdg.integratieproject2.api.dto;

import be.kdg.integratieproject2.Domain.Review;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CardDto {
    private String id;
    @NotNull
    @NotEmpty
    private String text;
    private String pictureId;
    private List<Review> reviews;
    private boolean chosen;

    public String getPictureId() {
        return pictureId;
    }


    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
