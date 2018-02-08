package be.kdg.integratieproject2.API.Verification;

import be.kdg.integratieproject2.BL.Interfaces.UserService;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private UserService userService;
    private JavaMailSender mailSender;

    public RegistrationListener(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        ApplicationUser user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String email = user.getEmail();
        String subject = "Confirm registration";
        String confirmationUrl = event.getAppUrl() + "/users/registrationConfirm?token=" + token;

        SimpleMailMessage verificationEmail = new SimpleMailMessage();
        verificationEmail.setTo(email);
        verificationEmail.setSubject(subject);
        verificationEmail.setText("http://localhost:8080" + confirmationUrl);
        mailSender.send(verificationEmail);
    }
}
