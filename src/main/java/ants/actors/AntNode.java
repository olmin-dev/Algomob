package ants.actors;

import ants.environment.Cell;
import ants.environment.FoodNode;
import io.jbotsim.core.Color;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.List;
import java.util.Random;

public class AntNode extends CellLocatedNode {
    private int lifetime = 1000;
    private boolean arrived = true;
    private boolean carry = false;

    public AntNode(){
        super();
        setIcon("/images/ant.png");
    }

    @Override
    public void onStart() {
        super.onStart();
        setLocation(currentCell);
        onSensingIn(this);
        setSensingRange(45);
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

    @Override
    public void onArrival() {
        arrived = true;
        setCurrentCell((Cell) getDestinations().element());
        super.onArrival();
    }

    protected void antAlgorithm() {
        if(arrived) {
            Cell cell = pickNeighBoringCell();
            addDestination(cell);
            arrived = false;
        }
        if(!carry){
            takeFood();
        } else {
            dropFood();
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
        List<Node>  Nb = getSensedNodes();
        for(int i = 0; i < Nb.size(); i++){
            Node currentNb = Nb.get(i);
            if(currentNb.getIcon() == "/images/ant-worm.png" && distance(currentNb) < 10){
                System.out.println("Miam miam pour la reine !");
                FoodNode queen = (FoodNode) currentNb;
                queen.decreaseQuantity();
                carry = true;
                return;
            }
        }
    }

    public void dropFood() {
        List<Node>  Nb = getSensedNodes();
        for(int i = 0; i < Nb.size(); i++){
            Node currentNb = Nb.get(i);
            if(currentNb.getIcon() == "/images/ant-queen.png" && distance(currentNb) < 10){
                System.out.println("Oh ma reine !");
                QueenNode queen = (QueenNode) currentNb;
                queen.increaseFoodStock();
                carry = false;
                return;
            }
        }
    }

}
