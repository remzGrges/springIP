package be.kdg.integratieproject2.Domain;

public class Organiser {
    private Boolean isEnabled;
    private String email;

    public Organiser(Boolean isEnabled, String email) {
        this.isEnabled = isEnabled;
        this.email = email;
    }

    public Organiser() {
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
