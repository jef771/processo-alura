package br.com.alura.school.video;

import br.com.alura.school.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {

    boolean existsVideoByVideoAndLecture(String code, Lecture lecture);
}
