package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.domain.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<ApplicationUser, String> {
    ApplicationUser findByEmail(String s);
}
