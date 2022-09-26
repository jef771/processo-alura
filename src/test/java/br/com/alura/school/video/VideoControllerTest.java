package br.com.alura.school.video;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.lecture.Lecture;
import br.com.alura.school.lecture.LectureRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VideoControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    private Lecture lecture;

    @BeforeEach
    void init() {
        Course newCourse = courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        User newUser = new User("ana", "ana@email.com");
        newUser.setRole(UserRole.INSTRUCTOR);
        userRepository.save(newUser);
        Lecture newLecture = new Lecture("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas");
        newLecture.setCourse(newCourse);
        newLecture.setAuthor(newUser);
        lecture = lectureRepository.save(newLecture);
    }

    @Test
    void should_add_new_video() throws Exception {
        NewVideoRequest newVideoRequest = new NewVideoRequest("https://www.youtube.com/watch?v=gI4-vj0WpKM");

        mockMvc.perform(post("/courses/java-1/sections/flutter-cores-dinamicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newVideoRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", ""));
    }

    @Test
    void bad_request_when_empty_video() throws Exception {
        NewVideoRequest newVideoRequest = new NewVideoRequest("");

        mockMvc.perform(post("/courses/java-1/sections/flutter-cores-dinamicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newVideoRequest)))
                .andExpect(status().reason("Please inform video"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void conflict_when_video_in_lecture() throws Exception {
        NewVideoRequest newVideoRequest = new NewVideoRequest("https://www.youtube.com/watch?v=gI4-vj0WpKM");
        Optional<Lecture> lecture1 = lectureRepository.findLectureByCode("flutter-cores-dinamicas");
        Video video = new Video("https://www.youtube.com/watch?v=gI4-vj0WpKM");
        video.setLecture(lecture);
        videoRepository.save(video);

        mockMvc.perform(post("/courses/java-1/sections/flutter-cores-dinamicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newVideoRequest)))
                .andExpect(status().reason("Video https://www.youtube.com/watch?v=gI4-vj0WpKM already in lecture Flutter: Configurando cores dinâmicas"))
                .andExpect(status().isConflict());
    }
}