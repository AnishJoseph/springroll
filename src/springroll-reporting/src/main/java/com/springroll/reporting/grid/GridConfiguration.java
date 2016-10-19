package com.springroll.reporting.grid;

import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created by anishjoseph on 18/10/16.
 */
@Component public class GridConfiguration {
    private List<Grid> grids;
    private List<GridParameter> gridParameters;

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
}
