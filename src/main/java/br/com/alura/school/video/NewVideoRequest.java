package br.com.alura.school.video;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewVideoRequest {

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
