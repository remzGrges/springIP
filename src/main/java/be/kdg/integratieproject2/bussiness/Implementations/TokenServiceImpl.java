package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.verification.InvitationToken;
import be.kdg.integratieproject2.Domain.verification.SessionInvitationToken;
import be.kdg.integratieproject2.Domain.verification.Token;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import be.kdg.integratieproject2.bussiness.Interfaces.TokenService;
import be.kdg.integratieproject2.data.implementations.TokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    TokenRepository repository ;


    public TokenServiceImpl(TokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getTokenSort(String token) {
        Token token1 = repository.findByToken(token);

        if (token1 instanceof SessionInvitationToken) {
            return "Sessie";
        } else if(token1 instanceof InvitationToken) {
            return "organiser";
        } else if (token1 instanceof VerificationToken) {
            return "verificatie";
        }
        return "Van geen enkele";
    }


}
