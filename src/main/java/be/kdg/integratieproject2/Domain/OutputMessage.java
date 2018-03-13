package be.kdg.integratieproject2.Domain;

import java.util.Date;

public class OutputMessage {
    private String from;
    private String text;
    private String timestamp;

    public OutputMessage() {
    }

    public OutputMessage(String from, String text, String timestamp) {
        this.from = from;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
