package br.com.alura.school.lecture;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.NewUserRequest;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void should_add_new_lecture() throws Exception {
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        User newUser = userRepository.save(new User("ana", "ana@email.com"));
        newUser.setRole(UserRole.INSTRUCTOR);
        userRepository.save(newUser);
        NewLectureRequest newLectureRequest = new NewLectureRequest("flutter-cores-dinamicas",
                "Flutter: Configurando cores dinâmicas",
                "ana");

        mockMvc.perform(post("/courses/java-1/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newLectureRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-1/sections/flutter-cores-dinamicas"));
    }
}