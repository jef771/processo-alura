package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.reports.SectionByVideosReport;
import br.com.alura.school.reports.SectionByVideosReportResponse;
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
public class SectionController {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;


    public SectionController(SectionRepository sectionRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<Void> newSection(@PathVariable("code") String code,
                                    @RequestBody @Valid NewSectionRequest newSectionRequest) {
        if(StringUtils.isBlank(newSectionRequest.getCode()) ||
                StringUtils.isBlank(newSectionRequest.getTitle()) ||
                StringUtils.isBlank(newSectionRequest.getAuthorUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Please inform code, tittle, and author username"));
        }

        if(StringUtils.length(newSectionRequest.getTitle()) < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Title must have more than 5"));
        }
        if(sectionRepository.existsSectionByCode(newSectionRequest.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Code %s already in use", newSectionRequest.getCode()));
        }

        Optional<Course> course = courseRepository.findByCode(code);
        Optional<User> user = userRepository.findByUsername(newSectionRequest.getAuthorUsername());
        if(course.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code));
        } else if(user.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, format("User with username %s not found",
                    newSectionRequest.getAuthorUsername()));
        }
        User author = user.get();
        if(!UserRole.INSTRUCTOR.equals(author.getRole())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("User must be an INSTRUCTOR"));
        }

        Section section = newSectionRequest.toEntity();
        section.setCourse(course.get());
        section.setAuthor(author);

        Section newSection = sectionRepository.save(section);
        URI location = URI.create(format("/courses/%s/sections/%s", code, newSection.getCode()));

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/sectionByVideosReport")
    ResponseEntity<List<SectionByVideosReportResponse>> report() {
        List<SectionByVideosReport> report = sectionRepository.report();
        List<SectionByVideosReportResponse> reportResponse = new ArrayList<>();

        report.forEach(r -> {
            reportResponse.add(new SectionByVideosReportResponse(r));
        });

        return ResponseEntity.ok(reportResponse);
    }
}
