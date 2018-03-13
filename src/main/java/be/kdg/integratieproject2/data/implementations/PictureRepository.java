package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.Domain.Picture;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PictureRepository extends MongoRepository<Picture, String> {
}
