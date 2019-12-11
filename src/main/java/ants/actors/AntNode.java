package ants.actors;

import ants.environment.Cell;
import io.jbotsim.core.Color;
import io.jbotsim.core.Node;

import java.util.Random;

public class AntNode extends CellLocatedNode {
    private int lifetime = 1000;

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
        lifetime --;
        if (lifetime <= 0){
            die();
        }
        antAlgorithm();

    }

    protected void antAlgorithm() {
        // TODO replace by your implementation
        Cell cell = pickNeighBoringCell();
        addDestination(cell);
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
