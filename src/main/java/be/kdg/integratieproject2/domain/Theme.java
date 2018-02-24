package be.kdg.integratieproject2.domain;


import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by Tim on 08/02/2018.
 */
public class Theme {
    @Id
    private String id;
    private String name;
    private String description;
    private List<String> tags;
    private List<String> organisers;

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

    public List<String> getOrganisers() {
        return organisers;
    }

    public void setOrganisers(List<String> organisers) {
        this.organisers = organisers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
