package be.kdg.integratieproject2.api.sessionInvitation;

import be.kdg.integratieproject2.Domain.Session;
import be.kdg.integratieproject2.bussiness.Interfaces.SessionService;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import be.kdg.integratieproject2.bussiness.exceptions.ObjectNotFoundException;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionInvitationListener implements ApplicationListener<OnSessionInvitationCompleteEvent> {

    private UserService userService;
    private SessionService sessionService;
    private JavaMailSender mailSender;

    public SessionInvitationListener(UserService userService, SessionService sessionService, JavaMailSender mailSender) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnSessionInvitationCompleteEvent onSessionInvitationCompleteEvent) {
        try {
            this.invitePlayer(onSessionInvitationCompleteEvent);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void invitePlayer(OnSessionInvitationCompleteEvent event) throws ObjectNotFoundException {
        String user = event.getUser();
        String currentUser = event.getCurrentUser();
        String sessionId = event.getSession();
        Session session = sessionService.getSession(sessionId, currentUser);

        String subject = "Je bent uitgenodigd aan de sessie " + session.getSessionName() + " deel te nemen";
        String token = UUID.randomUUID().toString();
        String confirmationUrl;
        SimpleMailMessage inviteEmail;
        sessionService.createSessionInvitationToken(user,sessionId, token, event.getCurrentUser());

        if (userService.getUserByUsername(user).getPassword() == null) {
            confirmationUrl = event.getAppUrl() + "/register/" + token;
        } else {
            confirmationUrl = event.getAppUrl() + "/acceptSessionInvite/" + token;
        }

        inviteEmail = new SimpleMailMessage();
        inviteEmail.setTo(user);
        inviteEmail.setSubject(subject);
        inviteEmail.setText("https://kandoe-webclient.herokuapp.com" + confirmationUrl);
        mailSender.send(inviteEmail);

    }
}
