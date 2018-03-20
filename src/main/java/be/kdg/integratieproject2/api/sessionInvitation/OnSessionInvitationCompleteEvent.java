package be.kdg.integratieproject2.api.sessionInvitation;

import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnSessionInvitationCompleteEvent extends ApplicationEvent {

        private String appUrl;
        private Locale locale;
        private String newUser;
        private String currentUser;
        private String session;




    public OnSessionInvitationCompleteEvent(Object source, String appUrl, Locale locale, String user, String currentUser, String session) {
        super(source);
        this.appUrl = appUrl;
        this.locale = locale;
        this.newUser = user;
        this.currentUser = currentUser;
        this.session = session;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String getNewUser() {
        return newUser;
    }

    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getUser() {
        return newUser;
    }

    public void setUser(String user) {
        this.newUser = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
