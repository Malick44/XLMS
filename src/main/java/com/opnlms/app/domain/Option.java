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
 * A Option.
 */
@Table("jhi_option")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Option implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Column("text")
    private String text;

    @NotNull(message = "must not be null")
    @Column("question_id")
    private String questionId;

    @NotNull(message = "must not be null")
    @Column("correct")
    private Boolean correct;

    @Column("assessment_id")
    private String assessmentId;

    @Column("assignment_id")
    private String assignmentId;

    @Column("is_selected")
    private Boolean isSelected;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Option id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public Option text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getQuestionId() {
        return this.questionId;
    }

    public Option questionId(String questionId) {
        this.setQuestionId(questionId);
        return this;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Boolean getCorrect() {
        return this.correct;
    }

    public Option correct(Boolean correct) {
        this.setCorrect(correct);
        return this;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public String getAssessmentId() {
        return this.assessmentId;
    }

    public Option assessmentId(String assessmentId) {
        this.setAssessmentId(assessmentId);
        return this;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssignmentId() {
        return this.assignmentId;
    }

    public Option assignmentId(String assignmentId) {
        this.setAssignmentId(assignmentId);
        return this;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Boolean getIsSelected() {
        return this.isSelected;
    }

    public Option isSelected(Boolean isSelected) {
        this.setIsSelected(isSelected);
        return this;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Option setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        return id != null && id.equals(((Option) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Option{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", questionId='" + getQuestionId() + "'" +
            ", correct='" + getCorrect() + "'" +
            ", assessmentId='" + getAssessmentId() + "'" +
            ", assignmentId='" + getAssignmentId() + "'" +
            ", isSelected='" + getIsSelected() + "'" +
            "}";
    }
}
