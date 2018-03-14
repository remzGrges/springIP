package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;

public class Review {
    @Id
    private String id;
    private String userid;
    private String text;

    public Review() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }
}
