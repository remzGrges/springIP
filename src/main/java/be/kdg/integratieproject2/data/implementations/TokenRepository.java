package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.Domain.verification.Token;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {
    public Token findByToken(String s);
}
