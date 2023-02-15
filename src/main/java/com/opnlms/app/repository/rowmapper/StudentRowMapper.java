package com.opnlms.app.repository.rowmapper;

import com.opnlms.app.domain.Student;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Student}, with proper type conversions.
 */
@Service
public class StudentRowMapper implements BiFunction<Row, String, Student> {

    private final ColumnConverter converter;

    public StudentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Student} stored in the database.
     */
    @Override
    public Student apply(Row row, String prefix) {
        Student entity = new Student();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        return entity;
    }
}
