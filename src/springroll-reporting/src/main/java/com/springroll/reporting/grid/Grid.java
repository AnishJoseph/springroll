package com.springroll.reporting.grid;

import javax.persistence.Parameter;
import java.util.List;

/**
 * This defines a single Grid. It is populated from grid definitions expressed in the JSON file
 *
 */
public class Grid {
    private String name;
    private int fixedColumns;
    private String  namedQuery;
    private List<GridColumn> gridColumns;
    private List<Parameter<?>> parameters = null;
    private String key = "ID";

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

    public List<GridColumn> getGridColumns() {
        return gridColumns;
    }

    public void setGridColumns(List<GridColumn> gridColumns) {
        this.gridColumns = gridColumns;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
