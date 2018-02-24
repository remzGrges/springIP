package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.domain.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {
}
