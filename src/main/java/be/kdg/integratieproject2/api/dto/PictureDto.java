package be.kdg.integratieproject2.api.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class PictureDto {

    @NotNull
    @NotEmpty
    private String filename;

    @NotNull
    @NotEmpty
    private String filetype;

    @NotNull
    @NotEmpty
    private String value;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
