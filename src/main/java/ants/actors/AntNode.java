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
    private int dig = 1000;

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
        if(dig == 1000) {
            lifetime--;
            if (lifetime <= 0) {
                die();
            }
            antAlgorithm();
        } else {
            if(dig == 0){
                dig = 1000;
                currentCell.setCost(Cell.MIN_COST_VALUE);
            } else {
                dig--;
            }
        }
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
        if(nextCell.getCost() > Cell.MIN_COST_VALUE) {
            dig--;
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
