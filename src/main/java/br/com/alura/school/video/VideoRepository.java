package br.com.alura.school.video;

import br.com.alura.school.section.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {

    boolean existsVideoByVideoAndSection(String code, Section section);
}
