package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.domain.verification.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<VerificationToken, String> {
    public VerificationToken findByToken(String s);
}
