package be.kdg.integratieproject2.api.dto;

import java.util.List;

//TODO Moet dit?
public class SessionInviteDto {
    List<String> users;


    public SessionInviteDto(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
