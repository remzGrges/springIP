package be.kdg.integratieproject2.API.Dto;

import org.springframework.data.annotation.Id;

public class UserInfoDto {


    private String email;
    private String firstName;
    private String lastName;

    public UserInfoDto() {
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
