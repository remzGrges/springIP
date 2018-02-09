package be.kdg.integratieproject2.DAL.Implementations;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<ApplicationUser, String> {
    ApplicationUser findByEmail(String s);
}
