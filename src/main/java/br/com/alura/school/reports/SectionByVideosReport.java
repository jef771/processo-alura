package br.com.alura.school.reports;

public class SectionByVideosReport {

    private String courseName;
    private String sectionTitle;
    private String authorName;
    private Long totalVideos;

    public SectionByVideosReport(String courseName, String sectionTitle, String authorName, Long totalVideos) {
        this.courseName = courseName;
        this.sectionTitle = sectionTitle;
        this.authorName = authorName;
        this.totalVideos = totalVideos;
    }

    public SectionByVideosReport(String courseName, String sectionTitle, String authorName) {
        this.courseName = courseName;
        this.sectionTitle = sectionTitle;
        this.authorName = authorName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Long getTotalVideos() {
        return totalVideos;
    }
}
