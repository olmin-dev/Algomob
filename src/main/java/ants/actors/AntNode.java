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
        setLocation(currentCell);
    }

    @Override
    public void onClock() {
        super.onClock();

        antAlgorithm();

    }

    @Override
    public void onArrival() {
        Cell a = (Cell) getDestinations().element();
        setCurrentCell(a);
        System.out.println(currentCell);
        super.onArrival();

    }

    protected void antAlgorithm() {
        if(getDestinations().isEmpty() || currentCell != getDestinations().element()) {
            Cell cell = pickNeighBoringCell();
            addDestination(cell);
        }
    }

    protected Cell pickNeighBoringCell() {
        Cell nextCell = null;
        Random random = new Random();
        while(nextCell == null){
            if(random.nextBoolean()){
                if(random.nextBoolean()){
                    if(random.nextBoolean()){
                        nextCell = getCurrentCell().getBottomLeftNeighbor();
                    } else {
                        nextCell = getCurrentCell().getLeftNeighbor();
                    }
                } else {
                    if(random.nextBoolean()){
                        nextCell = getCurrentCell().getTopLeftNeighbor();
                    } else {
                        nextCell = getCurrentCell().getTopNeighbor();
                    }
                }
            } else {
                if(random.nextBoolean()){
                    if(random.nextBoolean()){
                        nextCell = getCurrentCell().getTopRightNeighbor();
                    } else {
                        nextCell = getCurrentCell().getRightNeighbor();
                    }
                } else {
                    if(random.nextBoolean()){
                        nextCell = getCurrentCell().getBottomRightNeighbor();
                    } else {
                        nextCell = getCurrentCell().getBottomNeighbor();
                    }
                }

            }
        }

        return nextCell;
    }
    public void takeFood() {
        // TODO
    }

    public void dropFood() {
        // TODO
    }

}
