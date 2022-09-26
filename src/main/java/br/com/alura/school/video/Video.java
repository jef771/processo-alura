package br.com.alura.school.video;

import br.com.alura.school.section.Section;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Video {

    public static final String ID = "id";
    public static final String COLUMN_VIDEO = "video";
    public static final String COLUMN_LECTURE_ID = "section_id";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = COLUMN_VIDEO, nullable = false, unique = true)
    private String video;

    @ManyToOne
    @JoinColumn(
            name = COLUMN_LECTURE_ID,
            referencedColumnName = "id",
            nullable = false
    )
    private Section section;

    public Video() {}

    public Video(String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
