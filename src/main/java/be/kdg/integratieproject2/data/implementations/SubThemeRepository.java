package be.kdg.integratieproject2.data.implementations;

import be.kdg.integratieproject2.Domain.SubTheme;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by arnoaddiers on 1/03/18.
 */
public interface SubThemeRepository extends MongoRepository<SubTheme,String> {
}
