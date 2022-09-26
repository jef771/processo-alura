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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Rollback
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

/*    @BeforeEach
    void init() {
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        User newUser = userRepository.save(new User("ana", "ana@email.com"));
        newUser.setRole(UserRole.INSTRUCTOR);
        userRepository.save(newUser);
        User newUser2 = userRepository.save(new User("alex", "ana@email.com"));
        newUser2.setRole(UserRole.STUDENT);
        userRepository.save(newUser2);
    }*/


    @Test
    void should_add_new_section() throws Exception {
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        User user = new User("ana1", "ana@email.com");
        user.setRole(UserRole.INSTRUCTOR);
        userRepository.save(user);

        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "ana1");

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

        mockMvc.perform(post("/courses/course-1/sections")
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

        mockMvc.perform(post("/courses/course-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().reason("Please inform code, tittle, and author username"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_code_in_use() throws Exception {
        courseRepository.save(new Course("java-2", "Java O1", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        User newUser = userRepository.save(new User("alice", "alice@email.com"));
        newUser.setRole(UserRole.INSTRUCTOR);
        userRepository.save(newUser);

        NewSectionRequest newSectionRequest = new NewSectionRequest("java-encapsulation",
                "Java: Encapsulation",
                "alice");
        Optional<User> user = userRepository.findByUsername("alice");
        Optional<Course> course = courseRepository.findByCode("java-2");
        Section section = new Section("java-encapsulation", "Java: Encapsulation");
        section.setAuthor(user.get());
        section.setCourse(course.get());
        sectionRepository.save(section);

        mockMvc.perform(post("/courses/java-2/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().reason("Code java-encapsulation already in use"))
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
        courseRepository.save(new Course("java-3", "Java O2", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        User newUser2 = userRepository.save(new User("alex1", "ana@email.com"));
        newUser2.setRole(UserRole.STUDENT);
        userRepository.save(newUser2);

        NewSectionRequest newSectionRequest = new NewSectionRequest("java-inheritance",
                "Java: Inheritance",
                "alex1");

        mockMvc.perform(post("/courses/java-3/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().reason("User must be an INSTRUCTOR"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_report() throws Exception {
        Course course = courseRepository.save(new Course("java-4", "Java O3", "Java Multithreading"));
        User newUser2 = userRepository.save(new User("bob", "bob@email.com"));
        newUser2.setRole(UserRole.INSTRUCTOR);
        User user = userRepository.save(newUser2);
        Section section = new Section("java-4", "Java: 4");
        section.setAuthor(user);
        section.setCourse(course);
        Section newSection = sectionRepository.save(section);

        EnrollmentKey key = new EnrollmentKey(user.getId(), course.getId());
        Enrollment enrollment = new Enrollment(key, user, course, LocalDate.now());
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

        mockMvc.perform(get("/sectionByVideosReport")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].courseName", is("Java O3")))
                .andExpect(jsonPath("$[0].sectionTitle", is("Java: 4")))
                .andExpect(jsonPath("$[0].authorName", is("bob")))
                .andExpect(jsonPath("$[0].totalVideos", is(4)))
                .andExpect(status().isOk());
    }
}