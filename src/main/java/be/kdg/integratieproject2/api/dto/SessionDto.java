package be.kdg.integratieproject2.api.dto;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.SessionType;
import be.kdg.integratieproject2.Domain.Theme;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class SessionDto {

    @NotNull
    @NotEmpty
    private String sessionId;
    @NotNull
    @NotEmpty
    private Theme theme;
    @NotNull
    @NotEmpty
    private List<ApplicationUser> players;
    private int numberOfRounds;
    @NotNull
    @NotEmpty
    private SessionType sessionType;
    @NotNull
    @NotEmpty
    private boolean canComment;
    @NotNull
    @NotEmpty
    private boolean addCardUser;
    @NotNull
    @NotEmpty
    private int timeUserRound;
    @NotNull
    @NotEmpty
    private Date startTime;

    public SessionDto() {
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

    public List<ApplicationUser> getPlayers() {
        return players;
    }

    public void setPlayers(List<ApplicationUser> players) {
        this.players = players;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
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
