package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.Domain.verification.SessionInvitationToken;
import be.kdg.integratieproject2.Domain.verification.Token;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import be.kdg.integratieproject2.bussiness.Interfaces.TokenService;
import be.kdg.integratieproject2.bussiness.exceptions.InvalidTokenException;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenRepository tokenRepository;

    @Override
    public String getTokenKind(String token) {
        Token token1 = tokenRepository.findByToken(token);

        if (token1 instanceof SessionInvitationToken) {
            return "Sessie";
        } else if(token1 instanceof InvitationToken) {
            return "organiser";
        } else if (token1 instanceof VerificationToken) {
            return "verificatie";
        }
        return "Van geen enkele";
    }

    @Override
    public boolean validateInvitationToken(InvitationToken token, String username) throws InvalidTokenException {
        Calendar cal = Calendar.getInstance();
        if ((token.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new InvalidTokenException(token.getExpiryDate());
        } else if (!token.getEmail().equalsIgnoreCase(username)){
            throw new InvalidTokenException(username);
        }
        return true;
    }

    @Override
    public InvitationToken getInvitationToken(String token) throws ObjectNotFoundException {
        InvitationToken invitationToken =  (InvitationToken) tokenRepository.findByToken(token);
        if (invitationToken == null) throw new ObjectNotFoundException(token);
        return invitationToken;
    }

    @Override
    public void createInvitationToken(String email, String themeId, String token) {
        tokenRepository.save(new InvitationToken(token, email, themeId));
    }

    @Override
    public SessionInvitationToken getSessionInvitationToken(String token) throws ObjectNotFoundException {
        SessionInvitationToken sessionInvitationToken = (SessionInvitationToken) tokenRepository.findByToken(token);
        if(sessionInvitationToken == null) throw new ObjectNotFoundException(token);
        return sessionInvitationToken;
    }


    @Override
    public void createSessionInvitationToken(String email, String sessionId, String token, String organiser) {
        tokenRepository.save(new SessionInvitationToken(token, sessionId,email, organiser));
    }
}
