package ants.actors;

import ants.environment.Cell;
import ants.environment.FoodNode;
import io.jbotsim.core.Color;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AntNode extends CellLocatedNode {
    private int lifetime = 10000;
    private boolean arrived = true;
    private boolean carry = false;
    private Cell diggingCell;
    private boolean isDigging = false;
    private int pheromoneBehaviour;
    private Cell lastCell;
    private Cell nextCell;

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
            diggingCell.decreasePv();
            int PV = diggingCell.getPV();
            diggingCell.setCost(diggingCell.PVToColor(PV));
            if (diggingCell.getCost() == Cell.MIN_COST_VALUE) {
                isDigging = false;
                setIcon("/images/ant.png");
            }
        } else {
            diggingCell.setCost(Cell.MIN_COST_VALUE);
            isDigging = false;
            setIcon("/images/ant.png");
        }
    }

    @Override
    public void onClock() {
        if(!isDigging) {
            super.onClock();
            decreaseLife();
            antAlgorithm();
        } else {
            setDirection(diggingCell);
            dig(diggingCell);
            decreaseLife();
            //On bloque le comportement normale de a fourmi qui ne peut que finir de creuser une fois lancées
        }
    }

    @Override
    public void onArrival() {
        arrived = true;
        setCurrentCell(this.nextCell);
        nextCell = null;
        //setCurrentCell(getDestinations().element());
        currentCell.addPheromones(pheromoneBehaviour);
        super.onArrival();
    }

    public void searchQueen(){
        List<Node> Nb = getSensedNodes();
        for(int i = 0; i < Nb.size(); i++){
            Node currentNb = Nb.get(i);
            if(currentNb.getIcon() == "/images/ant-queen.png" && distance(currentNb) < 10){
                dropFood(currentNb);
                return;
            }
        }
        Cell cell = searchWay();
        nextCell = cell;
        addDestination(cell);
        arrived = false;
    }



    protected void antAlgorithm() {
        if(arrived) {
            //Cell cell = pickNeighBoringCell();
            Cell cell = searchWay();
            nextCell = cell;
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
        int i;
        while(nextCell == null) {
            i = 1;
            if(pheromoneBehaviour == 0) {
                ArrayList<Integer> phersFood = currentCell.getAroundPheromonesFood();
                Collections.sort(phersFood,Collections.reverseOrder());
                nextCell = pickIn(currentCell.getFoodCells(phersFood.get(0)));
                while(i < phersFood.size() &&  (nextCell == null)) {
                    int maxpheromoneFood = phersFood.get(i);
                    nextCell = pickIn(currentCell.getFoodCells(maxpheromoneFood));
                    i++;
                }
            } else {
                ArrayList<Integer> phersQueen = currentCell.getAroundPheromonesQueen();
                Collections.sort(phersQueen,Collections.reverseOrder());
                nextCell = pickIn(currentCell.getQueenCells(phersQueen.get(0)));
                while(i < phersQueen.size() &&  (nextCell == null)) {
                    int maxpheromoneQueen = phersQueen.get(i);
                    nextCell = pickIn(currentCell.getQueenCells(maxpheromoneQueen));
                    i++;
                }
            }

            if(nextCell == null || nextCell == lastCell || nextCell.getIs_obstacle()){
                nextCell = pickNeighBoringCell();
            }
        }
        if(nextCell.getCost() > Cell.MIN_COST_VALUE) {
            double a = (Math.random());
            if(a > 0.1){
                nextCell = searchWay();
            } else {
                diggingCell = nextCell;
                isDigging = true;
                setIcon("/images/ant_dig.png");
            }
        }
        this.nextCell = nextCell;
        return nextCell;
    }

    protected Cell pickIn(ArrayList<Cell> cells){
        int a = (int) (Math.floor(Math.random() * (cells.size()-1)));
        while(cells.size() > 0 && (cells.get(a) == null || cells.get(a) == currentCell ||cells.get(a) == lastCell )) {
            a = (int) (Math.floor(Math.random() * (cells.size()-1)));
            cells.remove(a);
        }
        if(cells.size() == 0) {
            return null;
        }else {
            return cells.get(a);
        }
    }

    protected Cell pickNeighBoringCell() {
        Cell nextCell = null;
        int a;
        while(nextCell == null || nextCell.getIs_obstacle() || nextCell == lastCell) {
            Random r = new Random();
            a = (int) (r.nextInt(8));

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
        }
        if(nextCell.getCost() > Cell.MIN_COST_VALUE) {
            diggingCell = nextCell;
            isDigging = true;
            setIcon("/images/ant_dig.png");
        }
        this.nextCell = nextCell;
        return nextCell;
    }

    public void takeFood() {
        List<Node>  Nb = getSensedNodes();
        for(int i = 0; i < Nb.size(); i++){
            Node currentNb = Nb.get(i);
            if(currentNb.getIcon() == "/images/ant-worm.png" && distance(currentNb) < 1){
                System.out.println("Miam miam pour la reine !");
                FoodNode food = (FoodNode) currentNb;
                food.decreaseQuantity();
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
    }
}
