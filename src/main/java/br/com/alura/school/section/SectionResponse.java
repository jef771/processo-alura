package br.com.alura.school.section;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SectionResponse {

    @JsonProperty
    private final String code;

    @JsonProperty
    private final String title;

    @JsonProperty
    private final String courseName;

    @JsonProperty
    private final String authorName;

    public SectionResponse(Section section) {
        this.code = section.getCode();
        this.title = section.getTitle();
        this.courseName = section.getCourse().getName();
        this.authorName = section.getAuthor().getUsername();
    }
}
