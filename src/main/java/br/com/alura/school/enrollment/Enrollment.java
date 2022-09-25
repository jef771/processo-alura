package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
public class Enrollment {

    @EmbeddedId
    private EnrollmentKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name="course_id")
    private Course course;

    @NotBlank
    private LocalDate date;

    public Enrollment() {}

    public Enrollment(User user, Course course, LocalDate date) {
        this.user = user;
        this.course = course;
        this.date = date;
    }

    public Enrollment(EnrollmentKey id, User user, Course course, LocalDate date) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.date = date;
    }
}
