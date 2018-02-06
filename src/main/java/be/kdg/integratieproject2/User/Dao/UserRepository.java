package be.kdg.integratieproject2.User.Dao;

import be.kdg.integratieproject2.User.Domain.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<ApplicationUser, String> {
    ApplicationUser findByUsername(String s);
}
