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
 * A Course.
 */
@Table("course")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Course implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private String id;

    @NotNull(message = "must not be null")
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("author_id")
    private String authorId;

    @Column("author_name")
    private String authorName;

    @Column("description")
    private String description;

    @Column("category")
    private String category;

    @Column("sub_category")
    private String subCategory;

    @Column("level")
    private String level;

    @Column("language")
    private String language;

    @Column("duration")
    private String duration;

    @Column("price")
    private String price;

    @Column("rating")
    private String rating;

    @Column("rating_count")
    private String ratingCount;

    @Column("thumbnail")
    private String thumbnail;

    @Column("url")
    private String url;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Course id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Course title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public Course authorId(String authorId) {
        this.setAuthorId(authorId);
        return this;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public Course authorName(String authorName) {
        this.setAuthorName(authorName);
        return this;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return this.description;
    }

    public Course description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return this.category;
    }

    public Course category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return this.subCategory;
    }

    public Course subCategory(String subCategory) {
        this.setSubCategory(subCategory);
        return this;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getLevel() {
        return this.level;
    }

    public Course level(String level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return this.language;
    }

    public Course language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDuration() {
        return this.duration;
    }

    public Course duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return this.price;
    }

    public Course price(String price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return this.rating;
    }

    public Course rating(String rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingCount() {
        return this.ratingCount;
    }

    public Course ratingCount(String ratingCount) {
        this.setRatingCount(ratingCount);
        return this;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public Course thumbnail(String thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return this.url;
    }

    public Course url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Course setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", authorId='" + getAuthorId() + "'" +
            ", authorName='" + getAuthorName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", subCategory='" + getSubCategory() + "'" +
            ", level='" + getLevel() + "'" +
            ", language='" + getLanguage() + "'" +
            ", duration='" + getDuration() + "'" +
            ", price='" + getPrice() + "'" +
            ", rating='" + getRating() + "'" +
            ", ratingCount='" + getRatingCount() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
