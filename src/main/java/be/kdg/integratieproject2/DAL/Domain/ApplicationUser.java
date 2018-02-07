package be.kdg.integratieproject2.DAL.Domain;

import org.springframework.data.annotation.Id;

public class ApplicationUser {

    @Id
    private String id;

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Boolean enabled;

    public ApplicationUser() {
        this.enabled = false;
    }

    public String getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {

        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
