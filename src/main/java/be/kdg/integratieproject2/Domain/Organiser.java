package be.kdg.integratieproject2.Domain;

import java.util.List;

public class Organiser {
    private Boolean isEnabled;
    private String email;
    private String themeID;

    public String getThemeID() {
        return themeID;
    }

    public void setThemeID(String themeID) {
        this.themeID = themeID;
    }

    public Organiser(Boolean isEnabled, String email, String themeID) {
        this.isEnabled = isEnabled;
        this.email = email;
        this.themeID = themeID;
    }


    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
