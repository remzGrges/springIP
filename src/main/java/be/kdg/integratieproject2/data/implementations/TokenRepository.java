package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.Domain.Verification.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<VerificationToken, String> {
    public VerificationToken findByToken(String s);
}
