package be.kdg.integratieproject2.bussiness.Interfaces;

import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.Domain.verification.SessionInvitationToken;
import be.kdg.integratieproject2.Domain.verification.Token;
import be.kdg.integratieproject2.bussiness.exceptions.InvalidTokenException;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;

public interface TokenService {
    String getTokenKind(String token);

    boolean validateInvitationToken(InvitationToken token, String username) throws InvalidTokenException;

    InvitationToken getInvitationToken(String token) throws ObjectNotFoundException;

    void createInvitationToken(String email, String themeId, String token);

    SessionInvitationToken getSessionInvitationToken(String token) throws ObjectNotFoundException;

    void createSessionInvitationToken(String email, String sessionId, String token, String organiser);
}
