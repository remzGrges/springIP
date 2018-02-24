package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.Domain.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {
}
