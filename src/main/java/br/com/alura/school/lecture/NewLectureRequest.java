package br.com.alura.school.lecture;

import br.com.alura.school.support.validation.Unique;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewLectureRequest {

    @JsonProperty
    private final String code;

    @JsonProperty
    private final String title;

    @NotBlank
    @JsonProperty
    private final String authorUsername;


    public NewLectureRequest(String code, String title, String authorUsername) {
        this.code = code;
        this.title = title;
        this.authorUsername = authorUsername;
    }

    public Lecture toEntity() {
        return new Lecture(code, title);
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }
}
