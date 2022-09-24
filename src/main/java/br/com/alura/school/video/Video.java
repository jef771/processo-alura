package br.com.alura.school.video;

import br.com.alura.school.lecture.Lecture;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Video {

    public static final String ID = "id";
    public static final String COLUMN_VIDEO = "video";
    public static final String COLUMN_LECTURE_ID = "lecture_id";


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
    private Lecture lecture;
}
