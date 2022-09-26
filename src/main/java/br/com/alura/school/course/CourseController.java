package br.com.alura.school.course;

import br.com.alura.school.section.SectionDTO;
import br.com.alura.school.section.SectionRepository;
import br.com.alura.school.video.VideoDTO;
import br.com.alura.school.video.VideoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class CourseController {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final VideoRepository videoRepository;

    public CourseController(CourseRepository courseRepository, SectionRepository sectionRepository, VideoRepository videoRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.videoRepository = videoRepository;
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

    /*Retorna Curso Aulas e Vídeos*/
    /*@GetMapping("/courses/{code}")
    ResponseEntity<CompleteCourseResponse> courseByCode(@PathVariable("code") String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));

        List<SectionDTO> sections = sectionRepository.findAllByCourseId(course.getId());

        sections.forEach(s -> {
            List<VideoDTO> videos = videoRepository.findAllBySectionCode(s.getCode());
            s.setVideos(videos);
        });

        CourseDTO courseDTO = new CourseDTO(course, sections);

        return ResponseEntity.ok(new CompleteCourseResponse(courseDTO));
    }*/

    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        courseRepository.save(newCourseRequest.toEntity());
        URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
        return ResponseEntity.created(location).build();
    }
}
