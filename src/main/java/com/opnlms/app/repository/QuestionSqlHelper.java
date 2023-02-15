package com.opnlms.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class QuestionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("section_id", table, columnPrefix + "_section_id"));
        columns.add(Column.aliased("course_id", table, columnPrefix + "_course_id"));
        columns.add(Column.aliased("text", table, columnPrefix + "_text"));
        columns.add(Column.aliased("assignment_id", table, columnPrefix + "_assignment_id"));
        columns.add(Column.aliased("assessment_id", table, columnPrefix + "_assessment_id"));

        return columns;
    }
}
