package be.kdg.integratieproject2.Domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class ApplicationUser {

    @Id
    private String id;

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Boolean enabled;
    private List<String> themes;



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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getThemes() {
        return themes;
    }

    public void setThemes(List<String> themes) {
        this.themes = themes;
    }
}
