package be.kdg.integratieproject2.BL.Implementations;

import be.kdg.integratieproject2.BL.Interfaces.ThemeService;
import be.kdg.integratieproject2.DAL.Implementations.ThemeRepository;
import be.kdg.integratieproject2.Domain.Theme;
import org.springframework.stereotype.Service;

/**
 * Created by Tim on 08/02/2018.*/

@Service
public class ThemeServiceImpl implements ThemeService {

    private ThemeRepository themeRepository;

    public ThemeServiceImpl(ThemeRepository themeRepository) {
    }

    @Override
    public void addTheme(Theme theme) {
        themeRepository.save(theme);
    }

    @Override
    public Theme getTheme(String id) {
        return themeRepository.findOne(id);
    }
}
