package br.com.alura.school.course;

import br.com.alura.school.section.SectionDTO;

import java.util.List;

public class CourseDTO {

    private String code;

    private String name;

    private String description;

    private List<SectionDTO> sections;

    public CourseDTO(Course course, List<SectionDTO> sections) {
        this.code = course.getCode();
        this.name = course.getName();
        this.description = course.getDescription();
        this.sections = sections;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SectionDTO> getSections() {
        return sections;
    }

    public void setSections(List<SectionDTO> sections) {
        this.sections = sections;
    }

}
