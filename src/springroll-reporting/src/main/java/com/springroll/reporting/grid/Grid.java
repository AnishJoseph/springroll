package com.springroll.reporting.grid;

import javax.persistence.Parameter;
import java.util.List;

/**
 * Created by anishjoseph on 18/10/16.
 */
public class Grid {
    private String name;
    private int fixedColumns;
    private String  namedQuery;
    private List<Column> columns;
    private List<Parameter<?>> parameters = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFixedColumns() {
        return fixedColumns;
    }

    public void setFixedColumns(int fixedColumns) {
        this.fixedColumns = fixedColumns;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getNamedQuery() {
        return namedQuery;
    }

    public void setNamedQuery(String namedQuery) {
        this.namedQuery = namedQuery;
    }

    public List<Parameter<?>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter<?>> parameters) {
        this.parameters = parameters;
    }
}
