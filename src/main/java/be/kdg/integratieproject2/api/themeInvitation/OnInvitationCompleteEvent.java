package be.kdg.integratieproject2.api.themeInvitation;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import be.kdg.integratieproject2.bussiness.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnInvitationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private String user;
    private String theme;


    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public OnInvitationCompleteEvent(ApplicationUser user, Locale locale, String appUrl, String  theme) {
        super(user);
        this.appUrl = appUrl;
        this.locale = locale;
        this.user = user.getEmail();
        this.theme = theme;
    }

    public String getAppUrl() {
        return appUrl;
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
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
