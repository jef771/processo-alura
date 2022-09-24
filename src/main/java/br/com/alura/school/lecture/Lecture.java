package br.com.alura.school.lecture;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

@Entity
public class Lecture {

    public static final String ID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_VIDEO_ID = "video_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min=5)
    @NotBlank
    @Column(name = COLUMN_CODE, nullable = false, unique = true)
    private String code;

    @Size(min = 5)
    @NotBlank
    @Column(name = COLUMN_TITLE, nullable = false, unique = true)
    private String title;

    @ManyToOne
    @JoinColumn(
            name = COLUMN_USER_ID,
            referencedColumnName = "id",
            nullable = false
    )
    private User author;

    @ManyToOne
    @JoinColumn(
            name = COLUMN_COURSE_ID,
            referencedColumnName = "id",
            nullable = false
    )
    private Course course;

}
