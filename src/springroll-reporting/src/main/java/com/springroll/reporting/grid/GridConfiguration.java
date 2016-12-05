package com.springroll.reporting.grid;

import java.util.List;

/**
 * Created by anishjoseph on 18/10/16.
 */
public class GridConfiguration {
    private List<Grid> grids;
    private List<GridParameter> gridParameters;
    private List<NamedQuery> namedQueries = null;
    private List<NumberFormat> numberFormats = null;

    public List<Grid> getGrids() {
        return grids;
    }

    public void setGrids(List<Grid> grids) {
        this.grids = grids;
    }

    public List<GridParameter> getGridParameters() {
        return gridParameters;
    }

    public void setGridParameters(List<GridParameter> gridParameters) {
        this.gridParameters = gridParameters;
    }

    public Grid findGridByName(String gridName){
        for (Grid grid : grids) {
            if(grid.getName().equals(gridName))return grid;
        }
        return null;
    }
    public GridParameter findParameterByName(String parameterName){
        for (GridParameter gridParameter : gridParameters) {
            if(gridParameter.getName().equals(parameterName))return gridParameter;
        }
        return null;
    }
    public NumberFormat findNumberFormatByName(String formatName){
        for (NumberFormat numberFormat : numberFormats) {
            if(numberFormat.getName().equalsIgnoreCase(formatName))return numberFormat;
        }
        return null;
    }

    public List<NamedQuery> getNamedQueries() {
        return namedQueries;
    }

    public void setNamedQueries(List<NamedQuery> namedQueries) {
        this.namedQueries = namedQueries;
    }

    public List<NumberFormat> getNumberFormats() {
        return numberFormats;
    }

    public void setNumberFormats(List<NumberFormat> numberFormats) {
        this.numberFormats = numberFormats;
    }
}
