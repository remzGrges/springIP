package be.kdg.integratieproject2.User.Service;

import be.kdg.integratieproject2.User.Domain.ApplicationUser;
import be.kdg.integratieproject2.User.Domain.ApplicationUserDto;

public interface UserService {
    public ApplicationUser registerUser(ApplicationUser applicationUser);
}
