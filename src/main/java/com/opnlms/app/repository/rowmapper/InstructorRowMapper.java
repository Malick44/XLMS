package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Instructor;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Instructor}, with proper type conversions.
 */
@Service
public class InstructorRowMapper implements BiFunction<Row, String, Instructor> {

    private final ColumnConverter converter;

    public InstructorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Instructor} stored in the database.
     */
    @Override
    public Instructor apply(Row row, String prefix) {
        Instructor entity = new Instructor();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        return entity;
    }
}
