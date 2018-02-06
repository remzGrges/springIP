package be.kdg.integratieproject2.DAL.Implementations;

import be.kdg.integratieproject2.DAL.Domain.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<ApplicationUser, String> {
    ApplicationUser findByUsername(String s);
}
