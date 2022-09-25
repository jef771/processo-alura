package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class NewEnrollmentRequest {

    @NotBlank
    @JsonProperty
    private String username;

    public NewEnrollmentRequest() {}
    public NewEnrollmentRequest(String authorUsername) {
        this.username = authorUsername;
    }

    public String getUsername() {
        return username;
    }

    public Enrollment toEntity() {
        return new Enrollment();
    }
}
