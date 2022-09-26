package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    @Transactional
    void should_enroll() throws Exception {
        courseRepository.save(new Course("git-4", "Git O3", "Git basics."));
        User newUser = userRepository.save(new User("hunter", "hunter@email.com"));
        newUser.setRole(UserRole.INSTRUCTOR);
        userRepository.save(newUser);

        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("hunter");

        mockMvc.perform(post("/courses/git-4/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", ""));
    }

    @Test
    @Transactional
    void bad_request_when_user_already_enrolled() throws Exception {
        Course course =courseRepository.save(new Course("git-5", "Git O4", "Git basics."));
        User newUser = userRepository.save(new User("hunter2", "hunter2@email.com"));
        newUser.setRole(UserRole.INSTRUCTOR);
        newUser = userRepository.save(newUser);

        EnrollmentKey key = new EnrollmentKey(newUser.getId(), course.getId());
        Enrollment enrollment = new Enrollment(key, newUser, course, LocalDate.now());
        enrollmentRepository.save(enrollment);

        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("hunter2");

        mockMvc.perform(post("/courses/git-5/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("User with username hunter2 already enrolled"));
    }

    @Test
    @Transactional
    void not_found_when_course_is_not_found() throws Exception {
        User newUser = userRepository.save(new User("hunter3", "hunter3@email.com"));
        newUser.setRole(UserRole.INSTRUCTOR);
        userRepository.save(newUser);

        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("hunter3");

        mockMvc.perform(post("/courses/git-100/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Course with code git-100 not found"));
    }

    @Test
    void not_found_when_user_is_not_found() throws Exception {
        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("hunter4");

        mockMvc.perform(post("/courses/git-100/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with username hunter4 not found"));
    }
}