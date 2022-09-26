package br.com.alura.school.video;

import br.com.alura.school.section.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    boolean existsVideoByVideoAndSection(String code, Section section);

    @Query("select new br.com.alura.school.video.VideoDTO(v.video) from Video v where v.section.code =?1")
    List<VideoDTO> findAllBySectionCode(String sectionCode);
}
