package be.kdg.integratieproject2.DAL.Implementations;

import be.kdg.integratieproject2.DAL.Domain.Verification.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<VerificationToken, String> {
    public VerificationToken findByToken(String s);
}
