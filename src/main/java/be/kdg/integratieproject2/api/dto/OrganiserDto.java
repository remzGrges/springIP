package be.kdg.integratieproject2.api.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class OrganiserDto {

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String themeID;

/*    @NotNull
    @NotEmpty
    private boolean isEnabled; */

    public OrganiserDto() {
    }

    public OrganiserDto(String email, String themeID) {
        this.email = email;
        this.themeID = themeID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getThemeID() {
        return themeID;
    }

    public void setThemeID(String themeID) {
        this.themeID = themeID;
    }
}
