package br.com.alura.school.video;

import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.lecture.Lecture;
import br.com.alura.school.lecture.LectureRepository;
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

@RestController
public class VideoController {

    private final VideoRepository videoRepository;
    private final LectureRepository lectureRepository;
    private final CourseRepository courseRepository;

    public VideoController(VideoRepository videoRepository, LectureRepository lectureRepository, CourseRepository courseRepository) {
        this.videoRepository = videoRepository;
        this.lectureRepository = lectureRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/courses/{courseCode}/sections/{sectionCode}")
    public ResponseEntity<Void> newVideo(@PathVariable("courseCode") String courseCode,
                                         @PathVariable("sectionCode") String sectionCode,
                                         @RequestBody @Valid NewVideoRequest newVideoRequest) {

        if(StringUtils.isBlank(newVideoRequest.getVideo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Please inform video"));
        }
        Optional<Lecture> lecture = lectureRepository.findLectureByCode(sectionCode);
        if(videoRepository.existsVideoByVideoAndLecture(newVideoRequest.getVideo(), lecture.get())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Video %s already in lecture %s", newVideoRequest.getVideo(), lecture.get().getTitle()));
        }
        Video newVideo = newVideoRequest.toEntity();
        newVideo.setLecture(lecture.get());
        videoRepository.save(newVideo);
        URI location = URI.create(format(""));

        return ResponseEntity.created(location).build();
    }
}
