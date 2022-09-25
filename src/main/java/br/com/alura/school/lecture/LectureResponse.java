package br.com.alura.school.lecture;

import br.com.alura.school.course.Course;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class LectureResponse {

    @JsonProperty
    private final String code;

    @JsonProperty
    private final String title;

    @JsonProperty
    private final String courseName;

    @JsonProperty
    private final String authorName;

    public LectureResponse(Lecture lecture) {
        this.code = lecture.getCode();
        this.title = lecture.getTitle();
        this.courseName = lecture.getCourse().getName();
        this.authorName = lecture.getAuthor().getUsername();
    }
}
