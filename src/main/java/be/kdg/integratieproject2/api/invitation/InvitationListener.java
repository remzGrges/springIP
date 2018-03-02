package be.kdg.integratieproject2.api.invitation;
import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InvitationListener implements ApplicationListener<OnInvitationCompleteEvent>{
    private UserService userService;
    private ThemeService themeService;
    private JavaMailSender mailSender;


    public InvitationListener(UserService userService, ThemeService themeService, JavaMailSender mailSender) {
        this.userService = userService;
        this.themeService = themeService;
        this.mailSender = mailSender;
       // this.themeId = themeId;
    }

    @Override
    public void onApplicationEvent(OnInvitationCompleteEvent event) {
        try {
            this.confirmInvitation(event);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void confirmInvitation(OnInvitationCompleteEvent event) throws ObjectNotFoundException {
        String user = event.getUser();
        String themeId = event.getTheme();
        Theme theme = themeService.getTheme(themeId);
        ApplicationUser applicationUser = userService.getUserByUsername(user);
        String subject = "Je bent uitgenodigd om het thema " + theme.getName() + " te bewerken" ;
        if (userService.getUserByUsername(user).getPassword() == null) {
            String token = UUID.randomUUID().toString();
            userService.createVerificationToken(applicationUser, token);
            String confirmationUrl = event.getAppUrl() + "/themes/register?themeId=" + themeId;
            SimpleMailMessage verificationEmail = new SimpleMailMessage();
            verificationEmail.setTo(user);
            verificationEmail.setSubject(subject);
            verificationEmail.setText("http://localhost:8080" + confirmationUrl);
            mailSender.send(verificationEmail);
        } else {

            String confirmationUrl = event.getAppUrl() + "/theme/acceptOrganiserInvite";
            SimpleMailMessage verificationEmail = new SimpleMailMessage();
            verificationEmail.setTo(user);
            verificationEmail.setSubject(subject);
            verificationEmail.setText("http://localhost:8080" + confirmationUrl);
            mailSender.send(verificationEmail);

        }
    }


}
