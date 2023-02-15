package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Course;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Course}, with proper type conversions.
 */
@Service
public class CourseRowMapper implements BiFunction<Row, String, Course> {

    private final ColumnConverter converter;

    public CourseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Course} stored in the database.
     */
    @Override
    public Course apply(Row row, String prefix) {
        Course entity = new Course();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setAuthorId(converter.fromRow(row, prefix + "_author_id", String.class));
        entity.setAuthorName(converter.fromRow(row, prefix + "_author_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCategory(converter.fromRow(row, prefix + "_category", String.class));
        entity.setSubCategory(converter.fromRow(row, prefix + "_sub_category", String.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", String.class));
        entity.setLanguage(converter.fromRow(row, prefix + "_language", String.class));
        entity.setDuration(converter.fromRow(row, prefix + "_duration", String.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", String.class));
        entity.setRating(converter.fromRow(row, prefix + "_rating", String.class));
        entity.setRatingCount(converter.fromRow(row, prefix + "_rating_count", String.class));
        entity.setThumbnail(converter.fromRow(row, prefix + "_thumbnail", String.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        return entity;
    }
}
