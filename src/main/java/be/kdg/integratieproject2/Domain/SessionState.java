package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * Created by Tim on 14/03/2018.
 */
public class SessionState {
    @Id
    private String id;
    private String sessionId;
    private String lastPlayer;
    private String nextPlayer;
    private Map<String, Integer> votes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLastPlayer() {
        return lastPlayer;
    }

    public void setLastPlayer(String lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public String getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(String nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public Map<String, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<String, Integer> votes) {
        this.votes = votes;
    }
}
