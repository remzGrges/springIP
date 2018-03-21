package be.kdg.integratieproject2.api.sessionInvitation;

import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.TokenService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import be.kdg.integratieproject2.bussiness.exceptions.UserNotAuthorizedException;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionInvitationListener implements ApplicationListener<OnSessionInvitationCompleteEvent> {

    private UserService userService;
    private TokenService tokenService;
    private JavaMailSender mailSender;

    public SessionInvitationListener(UserService userService, TokenService tokenService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.tokenService = tokenService;
    }

    @Override
    public void onApplicationEvent(OnSessionInvitationCompleteEvent onSessionInvitationCompleteEvent) {
        try {
            this.invitePlayer(onSessionInvitationCompleteEvent);
        } catch (ObjectNotFoundException | UserNotAuthorizedException e) {
            e.printStackTrace();
        }
    }

    private void invitePlayer(OnSessionInvitationCompleteEvent event) throws ObjectNotFoundException, UserNotAuthorizedException {
        String user = event.getUser();
        String currentUser = event.getCurrentUser();
        String sessionId = event.getSessionId();

        String subject = "Je bent uitgenodigd aan de sessie " + event.getSessionName() + " deel te nemen";
        String token = UUID.randomUUID().toString();
        String confirmationUrl;
        SimpleMailMessage inviteEmail;
        tokenService.createSessionInvitationToken(user,sessionId, token, event.getCurrentUser());

        if (userService.getUserByUsername(user).getPassword() == null) {
            confirmationUrl = event.getAppUrl() + "/register/" + token;
        } else {
            confirmationUrl = event.getAppUrl() + "/acceptSessionInvite/" + token;
        }

        inviteEmail = new SimpleMailMessage();
        inviteEmail.setTo(user);
        inviteEmail.setSubject(subject);
        inviteEmail.setText("https://kandoe-webclient.herokuapp.com/#/" + confirmationUrl);
        mailSender.send(inviteEmail);

    }
}
