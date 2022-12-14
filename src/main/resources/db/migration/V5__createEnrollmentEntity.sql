CREATE TABLE Enrollment (
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    date DATE NOT NULL
);

ALTER TABLE Enrollment ADD CONSTRAINT PK_ENROLLMENT PRIMARY KEY (user_id, course_id);
ALTER TABLE Enrollment ADD CONSTRAINT FK_USER_ON_ENROLLMENT FOREIGN KEY (user_id) REFERENCES User (id);
ALTER TABLE Enrollment ADD CONSTRAINT FK_COURSE_ON_ENROLLMENT FOREIGN KEY (course_id) REFERENCES Course (id);
