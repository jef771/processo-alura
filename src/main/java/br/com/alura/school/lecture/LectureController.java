package br.com.alura.school.lecture;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class LectureController {

    private final LectureRepository lectureRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;


    public LectureController(LectureRepository lectureRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.lectureRepository = lectureRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<Void> newLecture(@PathVariable("code") String code,
                                    @RequestBody @Valid NewLectureRequest newLectureRequest) {
        if(StringUtils.isBlank(newLectureRequest.getCode()) ||
                StringUtils.isBlank(newLectureRequest.getTitle()) ||
                StringUtils.isBlank(newLectureRequest.getAuthorUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Please inform code, tittle, and author username"));
        }
        if(lectureRepository.existsLectureByCode(newLectureRequest.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Code %s already in use", newLectureRequest.getCode()));
        }

        Optional<Course> course = courseRepository.findByCode(code);
        Optional<User> user = userRepository.findByUsername(newLectureRequest.getAuthorUsername());
        if(course.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code));
        } else if(user.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, format("User with username %s not found",
                    newLectureRequest.getAuthorUsername()));
        }
        User author = user.get();
        if(!UserRole.INSTRUCTOR.equals(author.getRole())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("User must be an INSTRUCTOR"));
        }

        Lecture lecture = newLectureRequest.toEntity();
        lecture.setCourse(course.get());
        lecture.setAuthor(author);

        Lecture newLecture = lectureRepository.save(lecture);
        URI location = URI.create(format("/courses/%s/sections/%s", code, newLecture.getCode()));

        return ResponseEntity.created(location).build();
    }
}
