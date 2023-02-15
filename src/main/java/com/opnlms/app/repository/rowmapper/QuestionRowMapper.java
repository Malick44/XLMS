package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Question;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Question}, with proper type conversions.
 */
@Service
public class QuestionRowMapper implements BiFunction<Row, String, Question> {

    private final ColumnConverter converter;

    public QuestionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Question} stored in the database.
     */
    @Override
    public Question apply(Row row, String prefix) {
        Question entity = new Question();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setSectionId(converter.fromRow(row, prefix + "_section_id", String.class));
        entity.setCourseId(converter.fromRow(row, prefix + "_course_id", String.class));
        entity.setText(converter.fromRow(row, prefix + "_text", String.class));
        entity.setAssignmentId(converter.fromRow(row, prefix + "_assignment_id", String.class));
        entity.setAssessmentId(converter.fromRow(row, prefix + "_assessment_id", String.class));
        return entity;
    }
}
