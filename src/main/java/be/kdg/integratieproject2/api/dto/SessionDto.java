package be.kdg.integratieproject2.api.dto;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.Theme;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class SessionDto {

    @NotNull
    @NotEmpty
    private String sessionId;
    @NotNull
    @NotEmpty
    private String sessionName;
    @NotNull
    @NotEmpty
    private String themeId;
    @NotNull
    @NotEmpty
    private List<String> players;
    private int numberOfRounds;
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

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
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
