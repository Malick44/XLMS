package com.opnlms.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AssessmentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("course_id", table, columnPrefix + "_course_id"));
        columns.add(Column.aliased("section_id", table, columnPrefix + "_section_id"));
        columns.add(Column.aliased("student_id", table, columnPrefix + "_student_id"));
        columns.add(Column.aliased("exam_date", table, columnPrefix + "_exam_date"));
        columns.add(Column.aliased("time_limit", table, columnPrefix + "_time_limit"));
        columns.add(Column.aliased("score", table, columnPrefix + "_score"));

        return columns;
    }
}
