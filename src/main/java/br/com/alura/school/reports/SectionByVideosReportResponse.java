package br.com.alura.school.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SectionByVideosReportResponse {

    @JsonProperty
    private final String courseName;

    @JsonProperty
    private final String sectionTitle;

    @JsonProperty
    private final String authorName;

    @JsonProperty
    private final Long totalVideos;


    public SectionByVideosReportResponse(SectionByVideosReport report) {
        this.courseName = report.getCourseName();
        this.sectionTitle = report.getSectionTitle();
        this.authorName = report.getAuthorName();
        this.totalVideos = report.getTotalVideos();
    }
}
