package be.kdg.integratieproject2.integration.service;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.Domain.verification.VerificationToken;
import be.kdg.integratieproject2.api.dto.UserRegistrationDto;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.data.implementations.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class TestUserService {

    @Autowired
    private UserService userService;
    private ApplicationUser applicationUser;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserDetailsService userDetailsService;

    @Before
    public void setup() {
        applicationUser = new ApplicationUser();
        applicationUser.setEnabled(true);
        applicationUser.setEmail("test@mail.be");
        applicationUser.setFirstName("TEst");
        applicationUser.setPassword("bla");
        userRepository.save(applicationUser);
    }

    @After
    public void after() {
        userRepository.deleteByEmail("test@mail.be");
    }

    @Test
    public void updateRegisteredUser() {
        applicationUser.setFirstName("Update");
        userService.updateRegisteredUser(applicationUser);
        ApplicationUser user = userService.getUserByUsername("test@mail.be");
        Assert.assertTrue(user.getFirstName().equals(applicationUser.getFirstName()));
    }

    @Test
    public void deleteToken() {
        VerificationToken verificationToken;
        userService.createVerificationToken(applicationUser, "bla");
        verificationToken = userService.getVerificationToken("bla");
        userService.deleteToken(verificationToken);
        verificationToken = userService.getVerificationToken("bla");
        Assert.assertTrue(verificationToken == null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameBadMail(){
       userDetailsService.loadUserByUsername("fake@mail.be");
    }
}
