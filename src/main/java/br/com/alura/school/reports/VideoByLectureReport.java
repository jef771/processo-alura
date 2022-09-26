package br.com.alura.school.reports;

public class VideoByLectureReport {

    private String courseName;
    private String sectionTitle;
    private String authorName;
    private Long totalVideos;

    public VideoByLectureReport(String courseName, String sectionTitle, String authorName, Long totalVideos) {
        this.courseName = courseName;
        this.sectionTitle = sectionTitle;
        this.authorName = authorName;
        this.totalVideos = totalVideos;
    }

    public VideoByLectureReport(String courseName, String sectionTitle, String authorName) {
        this.courseName = courseName;
        this.sectionTitle = sectionTitle;
        this.authorName = authorName;
    }
}
