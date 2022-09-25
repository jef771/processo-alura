package br.com.alura.school.course;

import br.com.alura.school.lecture.Lecture;
import br.com.alura.school.lecture.LectureRepository;
import br.com.alura.school.lecture.LectureResponse;
import br.com.alura.school.lecture.NewLectureRequest;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class CourseController {

    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;

    CourseController(CourseRepository courseRepository,
                     LectureRepository lectureRepository,
                     UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> allCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> responses = new ArrayList<>();

        courses.forEach(course -> {
            responses.add(new CourseResponse(course));
        });

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/courses/{code}")
    ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        return ResponseEntity.ok(new CourseResponse(course));
    }

    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        courseRepository.save(newCourseRequest.toEntity());
        URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
        return ResponseEntity.created(location).build();
    }
}
