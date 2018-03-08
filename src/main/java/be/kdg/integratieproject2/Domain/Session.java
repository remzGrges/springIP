package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;
import springfox.documentation.swagger2.mappers.LicenseMapper;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Session {
    @Id
    private String sessionId;
    private String sessionName;
    private Theme theme;
    private List<String> players;
    private int numberOfRounds;
    private boolean canComment;
    private boolean addCardUser;
    private int timeUserRound;
    private Date startTime;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public boolean isCanComment() {
        return canComment;
    }

    public void setCanComment(boolean canComment) {
        this.canComment = canComment;
    }

    public boolean isAddCardUser() {
        return addCardUser;
    }

    public void setAddCardUser(boolean addCardUser) {
        this.addCardUser = addCardUser;
    }

    public int getTimeUserRound() {
        return timeUserRound;
    }

    public void setTimeUserRound(int timeUserRound) {
        this.timeUserRound = timeUserRound;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
