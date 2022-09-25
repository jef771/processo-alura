package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;


    public EnrollmentController(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/courses/{courseCode}/enroll")
    ResponseEntity<Void> enroll(@PathVariable("courseCode") String courseCode,
                                @RequestBody @Valid NewEnrollmentRequest enrollmentRequest) {
        Optional<User> userOpt = userRepository.findByUsername(enrollmentRequest.getUsername());
        if(userOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, format("User with username %s not found",
                    enrollmentRequest.getUsername()));
        }
        Optional<Course> courseOpt = courseRepository.findByCode(courseCode);
        if(courseOpt.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", courseCode));
        }

        User user = userOpt.get();
        Course course = courseOpt.get();

        if(enrollmentRepository.existsEnrollmentByUserAndCourse(user, course)) {
            throw new ResponseStatusException(BAD_REQUEST, format("User with username %s already enrolled", user.getUsername()));
        }

        EnrollmentKey key = new EnrollmentKey(user.getId(), course.getId());

        Enrollment enrollment = new Enrollment(key, user, course, LocalDate.now());

        enrollmentRepository.save(enrollment);

        return ResponseEntity.created(URI.create("")).build();
    }

}
