package com.opnlms.app.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CourseSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("author_id", table, columnPrefix + "_author_id"));
        columns.add(Column.aliased("author_name", table, columnPrefix + "_author_name"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("category", table, columnPrefix + "_category"));
        columns.add(Column.aliased("sub_category", table, columnPrefix + "_sub_category"));
        columns.add(Column.aliased("level", table, columnPrefix + "_level"));
        columns.add(Column.aliased("language", table, columnPrefix + "_language"));
        columns.add(Column.aliased("duration", table, columnPrefix + "_duration"));
        columns.add(Column.aliased("price", table, columnPrefix + "_price"));
        columns.add(Column.aliased("rating", table, columnPrefix + "_rating"));
        columns.add(Column.aliased("rating_count", table, columnPrefix + "_rating_count"));
        columns.add(Column.aliased("thumbnail", table, columnPrefix + "_thumbnail"));
        columns.add(Column.aliased("url", table, columnPrefix + "_url"));

        return columns;
    }
}
