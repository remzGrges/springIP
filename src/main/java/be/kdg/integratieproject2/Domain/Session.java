package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class Session {
    @Id
    private String sessionId;
    private String sessionName;
    private String themeId;
    private String organiser;
    private List<String> players;
    private int numberOfRounds;
    private boolean canComment;
    private boolean addCardUser;
    private int timeUserRound;
    private Date startTime;
    private List<OutputMessage> messages;
    private List<SubTheme> subThemes;
    private List<Card> cards;
    private List<Card> suggestedCards;
    private List<String> readyPlayers;
    private boolean active;
    private List<Turn> turns;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSessionName() {
        return sessionName;
    }

    public List<String> getReadyPlayers() {
        return readyPlayers;
    }


    public void setReadyPlayers(List<String> readyPlayers) {
        this.readyPlayers = readyPlayers;
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

    public String getOrganiser() {
        return organiser;
    }

    public void setOrganiser(String organiser) {
        this.organiser = organiser;
    }

    public List<Card> getSuggestedCards() {
        return suggestedCards;
    }

    public void setSuggestedCards(List<Card> suggestedCards) {
        this.suggestedCards = suggestedCards;
    }

    public List<OutputMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<OutputMessage> messages) {
        this.messages = messages;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }
}
