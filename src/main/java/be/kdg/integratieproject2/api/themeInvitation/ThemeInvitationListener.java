package be.kdg.integratieproject2.api.themeInvitation;

import be.kdg.integratieproject2.Domain.Theme;
import be.kdg.integratieproject2.bussiness.Interfaces.ThemeService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ThemeInvitationListener implements ApplicationListener<OnInvitationCompleteEvent> {
    private UserService userService;
    private ThemeService themeService;
    private JavaMailSender mailSender;


    public ThemeInvitationListener(UserService userService, ThemeService themeService, JavaMailSender mailSender) {
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
        String subject = "Je bent uitgenodigd om het thema " + theme.getName() + " te bewerken";
        String token = UUID.randomUUID().toString();
        String confirmationUrl;
        SimpleMailMessage inviteEmail;
        themeService.createInvitationToken(user,themeId, token);

        if (userService.getUserByUsername(user).getPassword() == null) {
            confirmationUrl = event.getAppUrl() + "/register/" + token;
        } else {
            confirmationUrl = event.getAppUrl() + "/acceptOrganiserInvite/" + token;
        }

        inviteEmail = new SimpleMailMessage();
        inviteEmail.setTo(user);
        inviteEmail.setSubject(subject);
        inviteEmail.setText("https://kandoe-webclient.herokuapp.com" + confirmationUrl);
        mailSender.send(inviteEmail);
    }


}
