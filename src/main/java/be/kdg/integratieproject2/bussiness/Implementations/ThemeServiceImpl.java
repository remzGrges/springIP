package be.kdg.integratieproject2.bussiness.Implementations;

import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.data.implementations.ThemeRepository;
import be.kdg.integratieproject2.domain.Theme;
import org.springframework.stereotype.Service;

/**
 * Created by Tim on 08/02/2018.*/

@Service
public class ThemeServiceImpl implements ThemeService {

    private ThemeRepository themeRepository;

    public ThemeServiceImpl(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public Theme addTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    @Override
    public Theme getTheme(String id) {
        return themeRepository.findOne(id);
    }
}
