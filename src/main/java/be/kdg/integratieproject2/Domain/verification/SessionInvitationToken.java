package be.kdg.integratieproject2.Domain.verification;

public class SessionInvitationToken extends Token {
    String sessionId;
    String userId;
    private String organiser;

    
    public SessionInvitationToken(String token, String sessionId, String userId, String organiser) {
        super(token);
        this.sessionId = sessionId;
        this.userId= userId;
        this.organiser = organiser;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganiser() {
        return organiser;
    }

    public void setOrganiser(String organiser) {
        this.organiser = organiser;
    }
}
