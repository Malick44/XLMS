package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Option;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Option}, with proper type conversions.
 */
@Service
public class OptionRowMapper implements BiFunction<Row, String, Option> {

    private final ColumnConverter converter;

    public OptionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Option} stored in the database.
     */
    @Override
    public Option apply(Row row, String prefix) {
        Option entity = new Option();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setText(converter.fromRow(row, prefix + "_text", String.class));
        entity.setQuestionId(converter.fromRow(row, prefix + "_question_id", String.class));
        entity.setCorrect(converter.fromRow(row, prefix + "_correct", Boolean.class));
        entity.setAssessmentId(converter.fromRow(row, prefix + "_assessment_id", String.class));
        entity.setAssignmentId(converter.fromRow(row, prefix + "_assignment_id", String.class));
        entity.setIsSelected(converter.fromRow(row, prefix + "_is_selected", Boolean.class));
        return entity;
    }
}
