package br.com.alura.school.section;

import br.com.alura.school.reports.SectionByVideosReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    boolean existsSectionByCode(String code);
    Optional<Section> findSectionByCode(String code);

    @Query("SELECT NEW br.com.alura.school.reports.SectionByVideosReport(c.name, s.title, u.username, count(v.video)) " +
            "from Video v " +
            "left join v.section s " +
            "left join s.course c " +
            "left join s.author u " +
            "where s.course.id in(select distinct e.id.courseId from Enrollment e)")
    List<SectionByVideosReport> report();
}
