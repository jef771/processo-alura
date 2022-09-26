package br.com.alura.school.course;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompleteCourseResponse {

    @JsonProperty
    private final CourseDTO courseDTO;

    public CompleteCourseResponse(CourseDTO course) {
        this.courseDTO = course;
    }
}
