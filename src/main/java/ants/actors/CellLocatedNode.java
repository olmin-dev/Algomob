package ants.actors;

import ants.environment.Cell;
import io.jbotsim.core.Node;

public class CellLocatedNode extends WayPointNode {

    protected Cell currentCell = null;


    public Cell getCurrentCell() {
        return currentCell;
    }

    @Override
    public void onClock() {
        super.onClock();
        if(getTime()% 1000 == 0){
            currentCell.decreasePheromones();
        }
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

}
