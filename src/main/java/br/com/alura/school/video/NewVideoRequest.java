package br.com.alura.school.video;

import br.com.alura.school.lecture.Lecture;
import br.com.alura.school.support.validation.Unique;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class NewVideoRequest {

    @Unique(entity = Video.class, field = "video")
    @NotBlank
    @JsonProperty("video")
    private String video;


    public NewVideoRequest() {}
    public NewVideoRequest(String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public Video toEntity() {
        return new Video(video);
    }
}
