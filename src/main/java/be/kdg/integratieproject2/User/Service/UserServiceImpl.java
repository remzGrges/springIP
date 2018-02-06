package be.kdg.integratieproject2.User.Service;

import be.kdg.integratieproject2.User.Dao.UserRepository;
import be.kdg.integratieproject2.User.Domain.ApplicationUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ApplicationUser registerUser(ApplicationUser applicationUser) {
        return userRepository.save(applicationUser);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ApplicationUser applicationUser = userRepository.findByUsername(s);
        if (applicationUser == null)
            throw new UsernameNotFoundException(s);

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), Collections.emptyList());
    }
}
