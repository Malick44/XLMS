package com.opnlms.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Question.
 */
@Table("question")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Column("section_id")
    private String sectionId;

    @Column("course_id")
    private String courseId;

    @Column("text")
    private String text;

    @Column("assignment_id")
    private String assignmentId;

    @Column("assessment_id")
    private String assessmentId;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Question id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectionId() {
        return this.sectionId;
    }

    public Question sectionId(String sectionId) {
        this.setSectionId(sectionId);
        return this;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Question courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getText() {
        return this.text;
    }

    public Question text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAssignmentId() {
        return this.assignmentId;
    }

    public Question assignmentId(String assignmentId) {
        this.setAssignmentId(assignmentId);
        return this;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssessmentId() {
        return this.assessmentId;
    }

    public Question assessmentId(String assessmentId) {
        this.setAssessmentId(assessmentId);
        return this;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Question setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", sectionId='" + getSectionId() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", text='" + getText() + "'" +
            ", assignmentId='" + getAssignmentId() + "'" +
            ", assessmentId='" + getAssessmentId() + "'" +
            "}";
    }
}
