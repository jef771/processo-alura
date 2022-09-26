package br.com.alura.school.lecture;

import br.com.alura.school.reports.VideoByLectureReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    boolean existsLectureByCode(String code);
    Optional<Lecture> findLectureByCode(String code);

    /*@Query("SELECT NEW br.com.alura.school.reports.VideoByLectureReport(c.name, l.title, u.username) " +
            "from Lecture l " +
            "left join l.course c " +
            "left join l.author u " +
            "where l.course.id in(select distinct e.id.courseId from Enrollment e)")
    List<VideoByLectureReport> report();*/

    @Query("SELECT NEW br.com.alura.school.reports.VideoByLectureReport(c.name, l.title, u.username, count(v.video)) " +
            "from Video v " +
            "left join v.lecture l " +
            "left join l.course c " +
            "left join l.author u " +
            "where l.course.id in(select distinct e.id.courseId from Enrollment e)")
    List<VideoByLectureReport> report();
}
