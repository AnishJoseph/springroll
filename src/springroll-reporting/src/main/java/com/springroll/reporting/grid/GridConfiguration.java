package com.springroll.reporting.grid;

import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created by anishjoseph on 18/10/16.
 */
@Component public class GridConfiguration {
    private List<Grid> grids;

    public List<Grid> getGrids() {
        return grids;
    }

    public void setGrids(List<Grid> grids) {
        this.grids = grids;
    }

    public Grid findGridByName(String gridName){
        for (Grid grid : grids) {
            if(grid.getName().equals(gridName))return grid;
        }
        return null;
    }
}
