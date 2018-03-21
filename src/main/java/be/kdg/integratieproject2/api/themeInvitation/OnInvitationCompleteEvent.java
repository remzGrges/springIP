package be.kdg.integratieproject2.api.themeInvitation;

import be.kdg.integratieproject2.Domain.ApplicationUser;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnInvitationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private String user;
    private String themeId;
    private String themeName;

    public OnInvitationCompleteEvent(ApplicationUser user, Locale locale, String appUrl, String  themeId, String themeName) {
        super(user);
        this.appUrl = appUrl;
        this.locale = locale;
        this.user = user.getEmail();
        this.themeId = themeId;
        this.themeName = themeName;
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

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}
