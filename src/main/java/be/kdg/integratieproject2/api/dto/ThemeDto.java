package be.kdg.integratieproject2.api.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Tim on 09/02/2018.
 */
public class ThemeDto {
    @NotNull
    @NotEmpty
    private String Id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String description;
    private List<String> tags;
    @NotNull
    @NotEmpty
    private List<String> organisers;

    public ThemeDto() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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
}
