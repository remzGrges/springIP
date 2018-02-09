package be.kdg.integratieproject2.Domain;


import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */
public class Theme {
    private String name;
    private String description;
    private List<String> tags;
    private List<ApplicationUser> organisers;

    public Theme() {
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ApplicationUser> getOrganisers() {
        return organisers;
    }

    public void setOrganisers(List<ApplicationUser> organisers) {
        this.organisers = organisers;
    }
}
