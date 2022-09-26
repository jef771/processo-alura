CREATE TABLE Video (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    video VARCHAR(100) NOT NULL UNIQUE,
    section_id BIGINT NOT NULL
);

ALTER TABLE Video ADD CONSTRAINT FK_SECTION_ON_VIDEO FOREIGN KEY (section_id) REFERENCES Section (id);
