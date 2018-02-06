package be.kdg.integratieproject2.BL.Interfaces;

import be.kdg.integratieproject2.DAL.Domain.ApplicationUser;

public interface UserService {
    public ApplicationUser registerUser(ApplicationUser applicationUser);
}
