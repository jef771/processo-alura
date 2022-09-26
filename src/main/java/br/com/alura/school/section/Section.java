package br.com.alura.school.section;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

@Entity
public class Section {

    public static final String ID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_COURSE_ID = "course_id";

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

    @Deprecated
    public Section() {}
    public Section(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
