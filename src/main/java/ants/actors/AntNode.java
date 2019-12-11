package ants.actors;

import ants.environment.Cell;
import io.jbotsim.core.Color;
import io.jbotsim.core.Node;

import java.util.Random;

public class AntNode extends CellLocatedNode {

    public AntNode(){
        super();
        setIcon("/images/ant.png");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClock() {
        super.onClock();

        antAlgorithm();

    }

    protected void antAlgorithm() {
        // TODO replace by your implementation
        Cell cell = pickNeighBoringCell();
        setDirection(cell);
        setCurrentCell(cell);

        setLocation(currentCell);
    }

    protected Cell pickNeighBoringCell() {
        Cell nextCell = null;
        Random random = new Random();

        while(nextCell == null)
            if(random.nextBoolean())
                nextCell = getCurrentCell().getRightNeighbor();
            else
                nextCell = getCurrentCell().getLeftNeighbor();

        return nextCell;
    }
    public void takeFood() {
        // TODO
    }

    public void dropFood() {
        // TODO
    }

}
