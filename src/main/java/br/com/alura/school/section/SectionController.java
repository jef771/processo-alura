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
import java.util.List;

import static java.lang.String.format;

@RestController
public class SectionController {

    private final SectionService service;


    public SectionController(SectionService service) {
        this.service = service;
    }

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<Void> newSection(@PathVariable("code") String code,
                                    @RequestBody @Valid NewSectionRequest newSectionRequest) {

        Section newSection = service.newSection(code, newSectionRequest);
        URI location = URI.create(format("/courses/%s/sections/%s", code, newSection.getCode()));

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/sectionByVideosReport")
    ResponseEntity<List<SectionByVideosReportResponse>> report() {
        List<SectionByVideosReportResponse> reportResponse = service.report();

        return ResponseEntity.ok(reportResponse);
    }
}
