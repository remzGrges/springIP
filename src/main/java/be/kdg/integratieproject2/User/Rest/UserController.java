package be.kdg.integratieproject2.User.Rest;

import be.kdg.integratieproject2.User.Domain.ApplicationUser;
import be.kdg.integratieproject2.User.Domain.ApplicationUserDto;
import be.kdg.integratieproject2.User.Service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ModelMapper modelMapper;

    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ApplicationUserDto register(@RequestBody ApplicationUserDto dto) {
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        ApplicationUser user = modelMapper.map(dto, ApplicationUser.class);
        user = userService.registerUser(user);
        dto = modelMapper.map(user, ApplicationUserDto.class);
        return dto;
    }
}
