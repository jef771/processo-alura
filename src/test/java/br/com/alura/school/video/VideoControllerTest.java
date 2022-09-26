package br.com.alura.school.video;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;
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

import javax.transaction.Transactional;

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
    private SectionRepository sectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    private Section section;


    @Test
    @Transactional
    void should_add_new_video() throws Exception {
        Course course = courseRepository.save(new Course("git-2", "Git 01", "Git basics."));
        User newUser2 = userRepository.save(new User("john", "john@email.com"));
        newUser2.setRole(UserRole.INSTRUCTOR);
        User user = userRepository.save(newUser2);
        Section section = new Section("git-commit", "Git: Commit Command");
        section.setAuthor(user);
        section.setCourse(course);
        sectionRepository.save(section);


        NewVideoRequest newVideoRequest = new NewVideoRequest("https://www.youtube.com/watch?v=gI4-v");

        mockMvc.perform(post("/courses/git-2/sections/git-commit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newVideoRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", ""));
    }

    @Test
    @Transactional
    void bad_request_when_empty_video() throws Exception {
        NewVideoRequest newVideoRequest = new NewVideoRequest("");

        mockMvc.perform(post("/courses/java-1/sections/flutter-cores-dinamicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newVideoRequest)))
                .andExpect(status().reason("Please inform video"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void conflict_when_video_in_lecture() throws Exception {
        Course course = courseRepository.save(new Course("git-3", "Git 02", "Git basics."));
        User newUser2 = userRepository.save(new User("doe", "doe@email.com"));
        newUser2.setRole(UserRole.INSTRUCTOR);
        User user = userRepository.save(newUser2);
        Section section = new Section("git-add", "Git: Add Command");
        section.setAuthor(user);
        section.setCourse(course);
        Section newSection = sectionRepository.save(section);
        Video video = new Video("https://www.youtube.com/watch?v=gI4-");
        video.setSection(newSection);
        videoRepository.save(video);

        NewVideoRequest newVideoRequest = new NewVideoRequest("https://www.youtube.com/watch?v=gI4-");

        mockMvc.perform(post("/courses/git-3/sections/git-add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newVideoRequest)))
                .andExpect(status().reason("Video https://www.youtube.com/watch?v=gI4- already in section Git: Add Command"))
                .andExpect(status().isConflict());
    }
}