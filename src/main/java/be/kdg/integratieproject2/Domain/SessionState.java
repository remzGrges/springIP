package be.kdg.integratieproject2.Domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private int passedRounds;

    public SessionState(Session session)
    {
        this.id = new ObjectId().toString();
        this.sessionId = session.getSessionId();
        if(session.getTurns() != null && session.getTurns().size() != 0) {
            List<Turn> turns = session.getTurns();
            Turn lastTurn = turns.get(turns.size() - 1);
            this.lastPlayer = lastTurn.getUserId();
            int nextNumber = session.getPlayers().indexOf(lastTurn.getUserId()) + 1;
            if (nextNumber == session.getPlayers().size()) {
                this.nextPlayer = session.getPlayers().get(0);
            } else {
                this.nextPlayer = session.getPlayers().get(nextNumber);
            }
            this.votes = new HashMap<>();
            session.getTurns().forEach(turn -> {
                if (this.votes.containsKey(turn.getCardId())) {
                    this.votes.replace(turn.getCardId(), this.votes.get(turn.getCardId()) + 1);
                } else {
                    this.votes.put(turn.getCardId(), 1);
                }
            });
            this.passedRounds = session.getTurns().size()/session.getPlayers().size();
        }
        else {
            this.nextPlayer = session.getPlayers().get(0);
        }
    }

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

    public int getPassedRounds() {
        return passedRounds;
    }

    public void setPassedRounds(int passedRounds) {
        this.passedRounds = passedRounds;
    }
}
