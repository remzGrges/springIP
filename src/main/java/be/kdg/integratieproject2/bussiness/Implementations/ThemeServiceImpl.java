package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.data.implementations.ThemeRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */

@Service
public class ThemeServiceImpl implements ThemeService {

    private ThemeRepository themeRepository;

    public ThemeServiceImpl(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public Theme updateTheme(Theme theme) {

        if (theme.getSubThemes() != null && theme.getSubThemes().stream().anyMatch(s -> s.getId() == null)) {
            theme.getSubThemes().stream().filter(s -> s.getId() == null).forEach(s -> s.setId(new ObjectId().toString()));
        }

        return themeRepository.save(theme);
    }

    @Override
    public Theme addTheme(Theme theme, String username) {
        LinkedList<String> organisers = new LinkedList<>();
        organisers.add(username);
        theme.setOrganisers(organisers);
        return themeRepository.save(theme);
    }

    @Override
    public Theme getTheme(String id) throws ObjectNotFoundException {
        try {
            return themeRepository.findOne(id);
        } catch (Exception e) {
            throw new ObjectNotFoundException(id);
        }
    }

    @Override
    public List<Theme> getThemesByUser(String username) {
        return themeRepository.getAllByOrganisersContaining(username);
    }

    @Override
    public void deleteTheme(String id) {
        themeRepository.delete(id);
    }
}
