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
    private Cell diggingCell;
    private boolean isDigging = false;
    private Cell startCell;
    private boolean isSearchingQueen;

    public AntNode(){
        super();
        setIcon("/images/ant.png");
    }

    @Override
    public void onStart() {
        super.onStart();
        setLocation(currentCell);
        startCell = currentCell;
        onSensingIn(this);
        setSensingRange(45);
    }

    public void decreaseLife(){
        lifetime--;
        if (lifetime <= 0) {
            die();
        }
    }

    public void dig(Cell diggingCell){
        if(diggingCell.getCost() != Cell.MIN_COST_VALUE) {
            diggingCell.decreasePv();
            int PV = diggingCell.getPV();
            if (PV % 20 == 0) {
                int diggingProcess = diggingCell.getCost() - 1;
                diggingCell.setCost(diggingProcess);
                if (diggingProcess == Cell.MIN_COST_VALUE) {
                    isDigging = false;
                }
            }
        } else {
            isDigging = false;
        }
    }

    @Override
    public void onClock() {
        if(!isDigging) {
            super.onClock();
            decreaseLife();
            antAlgorithm();
        } else {
            dig(diggingCell);
            decreaseLife();
            //On bloque le comportement normale de a fourmi qui ne peut que finir de creuser une fois lancées
        }
    }

    @Override
    public void onArrival() {
        arrived = true;
        if(getDestinations().element() instanceof Cell) {
            setCurrentCell((Cell) getDestinations().element());
        } else {
            setCurrentCell(startCell);
        }
        super.onArrival();
    }

    public void searchQueen(){
        List<Node>  Nb = getSensedNodes();
        for(int i = 0; i < Nb.size(); i++){
            Node currentNb = Nb.get(i);
            if(currentNb.getIcon() == "/images/ant-queen.png"){
                if( distance(currentNb) < 10){
                    dropFood(currentNb);
                } else {
                    addDestination(currentNb.getLocation());
                    arrived = false;
                }
            }
        }
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
            searchQueen();
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
            diggingCell = nextCell;
            isDigging = true;
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

    public void dropFood(Node queen) {
        System.out.println("Oh ma reine !");
        QueenNode q = (QueenNode) queen;
        q.increaseFoodStock();
        carry = false;
    }

}
