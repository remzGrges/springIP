package be.kdg.integratieproject2.api.dto;


//TODO Moet dit?
public class ThemeInvitationDto {
    private String themeId;
    private String username;

    public ThemeInvitationDto(String themeId, String username) {
        this.themeId = themeId;
        this.username = username;
    }

    public ThemeInvitationDto() {
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
