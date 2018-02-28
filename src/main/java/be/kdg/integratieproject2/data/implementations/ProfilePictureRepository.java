package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.Domain.ProfilePicture;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePictureRepository extends MongoRepository<ProfilePicture, String> {
}
