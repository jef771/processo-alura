package br.com.alura.school.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    boolean existsLectureByCode(String code);
    Optional<Lecture> findLectureByCode(String code);
}
