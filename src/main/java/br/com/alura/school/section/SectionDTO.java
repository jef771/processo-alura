package br.com.alura.school.section;

import br.com.alura.school.user.User;
import br.com.alura.school.video.VideoDTO;

import java.util.List;

public class SectionDTO {

    private String code;

    private String title;

    private String authorUsername;

    private List<VideoDTO> videos;

    @Deprecated
    public SectionDTO() {}

    public SectionDTO(String code, String title, String authorUsername) {
        this.code = code;
        this.title = title;
        this.authorUsername = authorUsername;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public List<VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDTO> videos) {
        this.videos = videos;
    }
}
