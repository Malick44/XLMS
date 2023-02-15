package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Assignment;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Assignment}, with proper type conversions.
 */
@Service
public class AssignmentRowMapper implements BiFunction<Row, String, Assignment> {

    private final ColumnConverter converter;

    public AssignmentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Assignment} stored in the database.
     */
    @Override
    public Assignment apply(Row row, String prefix) {
        Assignment entity = new Assignment();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setCourseId(converter.fromRow(row, prefix + "_course_id", String.class));
        entity.setStudentId(converter.fromRow(row, prefix + "_student_id", String.class));
        entity.setExamDate(converter.fromRow(row, prefix + "_exam_date", LocalDate.class));
        entity.setTimeLimit(converter.fromRow(row, prefix + "_time_limit", Integer.class));
        entity.setScore(converter.fromRow(row, prefix + "_score", Integer.class));
        return entity;
    }
}
