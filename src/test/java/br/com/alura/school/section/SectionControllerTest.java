package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.enrollment.Enrollment;
import br.com.alura.school.enrollment.EnrollmentKey;
import br.com.alura.school.enrollment.EnrollmentRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import br.com.alura.school.video.Video;
import br.com.alura.school.video.VideoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SectionControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    void init() {
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        User newUser = userRepository.save(new User("ana", "ana@email.com"));
        newUser.setRole(UserRole.INSTRUCTOR);
        userRepository.save(newUser);
        User newUser2 = userRepository.save(new User("alex", "ana@email.com"));
        newUser2.setRole(UserRole.STUDENT);
        userRepository.save(newUser2);
    }

    @Test
    void should_add_new_section() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "ana");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-1/sections/flutter-cores-dinamicas"));
    }

    @Test
    void bad_request_when_code_missing() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("",
                "Flutter: Configurando cores dinâmicas",
                "ana");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().reason("Please inform code, tittle, and author username"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_title_missing() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas",
                "",
                "ana");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().reason("Please inform code, tittle, and author username"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_authorUsername_missing() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().reason("Please inform code, tittle, and author username"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_code_in_use() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "ana");
        Optional<User> user = userRepository.findByUsername("ana");
        Optional<Course> course = courseRepository.findByCode("java-1");
        Section section = new Section("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas");
        section.setAuthor(user.get());
        section.setCourse(course.get());
        sectionRepository.save(section);

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().reason("Code flutter-cores-dinamicas already in use"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_title_less_than_5_chars() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas",
                "Flut",
                "ana");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().reason("Title must have more than 5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_author_is_not_instructor() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "alex");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().reason("User must be an INSTRUCTOR"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_report() throws Exception {
        Section section = new Section("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas");
        Optional<User> user = userRepository.findByUsername("ana");
        Optional<Course> course = courseRepository.findByCode("java-1");
        section.setAuthor(user.get());
        section.setCourse(course.get());
        Section newSection = sectionRepository.save(section);

        EnrollmentKey key = new EnrollmentKey(user.get().getId(), course.get().getId());
        Enrollment enrollment = new Enrollment(key, user.get(), course.get(), LocalDate.now());
        enrollmentRepository.save(enrollment);

        Video video = new Video("https://www.youtube.com/watch?v=gI4-vj0WpKM");
        Video video2 = new Video("https://www.youtube.com/watch?v=gI4-vj0WpK");
        Video video3 = new Video("https://www.youtube.com/watch?v=gI4-vj0Wp");
        Video video4 = new Video("https://www.youtube.com/watch?v=gI4-vj0W");
        video.setSection(newSection);
        video2.setSection(newSection);
        video3.setSection(newSection);
        video4.setSection(newSection);
        videoRepository.saveAll(List.of(video, video2, video3, video4));

        mockMvc.perform(get("/sectionByVideosReport"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }
}