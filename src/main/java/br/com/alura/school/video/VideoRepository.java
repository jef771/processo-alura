package br.com.alura.school.video;

import br.com.alura.school.lecture.Lecture;
import br.com.alura.school.reports.VideoByLectureReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    boolean existsVideoByVideoAndLecture(String code, Lecture lecture);
}
