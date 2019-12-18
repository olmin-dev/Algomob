package ants.actors;

import ants.environment.Cell;
import ants.environment.FoodNode;
import io.jbotsim.core.Color;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntNode extends CellLocatedNode {
    private int lifetime = 1000;
    private boolean arrived = true;
    private boolean carry = false;
    private Cell diggingCell;
    private boolean isDigging = false;
    private int pheromoneBehaviour;

    public AntNode(){
        super();
        setIcon("/images/ant.png");
    }

    @Override
    public void onStart() {
        super.onStart();
        setLocation(currentCell);
        setSensingRange(45);
    }

    public void decreaseLife(){
        lifetime--;
        if (lifetime <= 0) {
            die();
        }
    }

    public void dig(Cell diggingCell){
        if(diggingCell.getPV()!= 0 && diggingCell.getCost() != Cell.MIN_COST_VALUE) {
            //System.out.println(this + " " + getLocation() + diggingCell + diggingCell.getPV() + " " + diggingCell.getCost());
            diggingCell.decreasePv();
            int PV = diggingCell.getPV();
            diggingCell.setCost(diggingCell.PVToColor(PV));
            if (diggingCell.getCost() == Cell.MIN_COST_VALUE) {
                isDigging = false;
            }
        } else {
            diggingCell.setCost(Cell.MIN_COST_VALUE);
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
            //setCurrentCell(getLocation());
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

    protected Cell searchWay(){
        Cell nextCell = null;
        while(nextCell == null) {
            nextCell = pickIn(currentCell.getUnexploredCells());
        }
    }

    protected Cell pickIn(ArrayList<Cell> cells){
        int a = (int) (Math.floor(Math.random() * cells.size()));
        return cells.get(a);
    }

    protected Cell pickNeighBoringCell() {
        Cell nextCell = null;
        int a;
        while(nextCell == null) {
            //while (nextCell != null && !nextCell.getIs_obstacle()) {
                a = (int) (Math.floor(Math.random() * 8));
                //System.out.println("aaaaaaadgfbk,jhtgfvbrshsgdj\n");
                switch (a) {
                    case 0:
                        nextCell = getCurrentCell().getBottomLeftNeighbor();
                        break;
                    case 1:
                        nextCell = getCurrentCell().getLeftNeighbor();
                        break;
                    case 2:
                        nextCell = getCurrentCell().getTopLeftNeighbor();
                        break;
                    case 3:
                        nextCell = getCurrentCell().getTopNeighbor();
                        break;
                    case 4:
                        nextCell = getCurrentCell().getTopRightNeighbor();
                        break;
                    case 5:
                        nextCell = getCurrentCell().getRightNeighbor();
                        break;
                    case 6:
                        nextCell = getCurrentCell().getBottomRightNeighbor();
                        break;
                    case 7:
                        nextCell = getCurrentCell().getBottomNeighbor();
                        break;
                }
            //}
        }
        nextCell.addPheromones(pheromoneBehaviour);
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
                setIcon("/images/ant-bean.png");
                pheromoneBehaviour = 1;
                return;
            }
        }
    }

    public void dropFood(Node queen) {
        System.out.println("Oh ma reine !");
        QueenNode q = (QueenNode) queen;
        q.increaseFoodStock();
        carry = false;
        setIcon("/images/ant.png");
        pheromoneBehaviour = 0;
        return;
    }

}
