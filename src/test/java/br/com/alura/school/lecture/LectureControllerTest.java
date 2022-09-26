package br.com.alura.school.lecture;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
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
class LectureControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

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
    void should_add_new_lecture() throws Exception {
        NewLectureRequest newLectureRequest = new NewLectureRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "ana");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newLectureRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-1/sections/flutter-cores-dinamicas"));
    }

    @Test
    void bad_request_when_code_missing() throws Exception {
        NewLectureRequest newLectureRequest = new NewLectureRequest("",
                "Flutter: Configurando cores dinâmicas",
                "ana");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newLectureRequest)))
                .andExpect(status().reason("Please inform code, tittle, and author username"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_title_missing() throws Exception {
        NewLectureRequest newLectureRequest = new NewLectureRequest("flutter-cores-dinamicas",
                "",
                "ana");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newLectureRequest)))
                .andExpect(status().reason("Please inform code, tittle, and author username"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_authorUsername_missing() throws Exception {
        NewLectureRequest newLectureRequest = new NewLectureRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newLectureRequest)))
                .andExpect(status().reason("Please inform code, tittle, and author username"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_code_in_use() throws Exception {
        NewLectureRequest newLectureRequest = new NewLectureRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "ana");
        Optional<User> user = userRepository.findByUsername("ana");
        Optional<Course> course = courseRepository.findByCode("java-1");
        Lecture lecture = new Lecture("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas");
        lecture.setAuthor(user.get());
        lecture.setCourse(course.get());
        lectureRepository.save(lecture);

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newLectureRequest)))
                .andExpect(status().reason("Code flutter-cores-dinamicas already in use"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_title_less_than_5_chars() throws Exception {
        NewLectureRequest newLectureRequest = new NewLectureRequest("flutter-cores-dinamicas",
                "Flut",
                "ana");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newLectureRequest)))
                .andExpect(status().reason("Title must have more than 5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bad_request_when_author_is_not_instructor() throws Exception {
        NewLectureRequest newLectureRequest = new NewLectureRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "alex");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newLectureRequest)))
                .andExpect(status().reason("User must be an INSTRUCTOR"))
                .andExpect(status().isBadRequest());
    }
}