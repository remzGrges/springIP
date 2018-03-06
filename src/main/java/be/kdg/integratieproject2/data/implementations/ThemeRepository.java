package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.Domain.Organiser;
import be.kdg.integratieproject2.Domain.Theme;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tim on 09/02/2018.
 */
@Repository
public interface ThemeRepository extends MongoRepository<Theme, String> {
    List<Theme> getAllByOrganisersContaining(String username);

}
