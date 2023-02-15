package com.opnlms.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Assessment.
 */
@Table("assessment")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Assessment implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @Column("title")
    private String title;

    @Column("course_id")
    private String courseId;

    @Column("section_id")
    private String sectionId;

    @Column("student_id")
    private String studentId;

    @Column("exam_date")
    private LocalDate examDate;

    @Column("time_limit")
    private Integer timeLimit;

    @Column("score")
    private Integer score;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Assessment id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Assessment title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Assessment courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSectionId() {
        return this.sectionId;
    }

    public Assessment sectionId(String sectionId) {
        this.setSectionId(sectionId);
        return this;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public Assessment studentId(String studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public LocalDate getExamDate() {
        return this.examDate;
    }

    public Assessment examDate(LocalDate examDate) {
        this.setExamDate(examDate);
        return this;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public Integer getTimeLimit() {
        return this.timeLimit;
    }

    public Assessment timeLimit(Integer timeLimit) {
        this.setTimeLimit(timeLimit);
        return this;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getScore() {
        return this.score;
    }

    public Assessment score(Integer score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Assessment setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assessment)) {
            return false;
        }
        return id != null && id.equals(((Assessment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assessment{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", sectionId='" + getSectionId() + "'" +
            ", studentId='" + getStudentId() + "'" +
            ", examDate='" + getExamDate() + "'" +
            ", timeLimit=" + getTimeLimit() +
            ", score=" + getScore() +
            "}";
    }
}
