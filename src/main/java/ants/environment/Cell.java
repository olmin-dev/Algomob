package ants.environment;

import io.jbotsim.core.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cell extends Point {

    public static final int MAX_COST_VALUE = 40;
    public static final int MIN_COST_VALUE = 1;
    private int cost;

    private int PV;

    public int getPV() {
        return PV;
    }

    public void setPV(int PV) {
        this.PV = PV;
    }

    public synchronized void decreasePv(){
        this.PV--;
    }

    Map<Integer, Cell> neighBor = new HashMap<>();


    public Cell(Point location){
        super(location);
        cost = new Random().nextInt(MAX_COST_VALUE - MIN_COST_VALUE+1) + MIN_COST_VALUE+1;
        PV = cost * 20;
    }

    public Cell getNeighBor(int index) {
        return neighBor.get(index);
    }
    public void setNeighBor(int index, Cell value) {
        neighBor.put(index, value);
    }

    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM = 3;
    public static final int TOP_RIGHT = 4;
    public static final int BOTTOM_RIGHT = 5;
    public static final int BOTTOM_LEFT = 6;
    public static final int TOP_LEFT = 7;

    public Cell getRightNeighbor() {
        return getNeighBor(RIGHT);
    }
    public void setRightNeighbor(Cell neighbor) {
        setNeighBor(RIGHT, neighbor);
    }
    public Cell getLeftNeighbor() {
        return getNeighBor(LEFT);
    }
    public void setLefNeighbor(Cell neighbor) {
        setNeighBor(LEFT, neighbor);
    }
    public Cell getTopNeighbor() {
        return getNeighBor(TOP);
    }
    public void setTopNeighbor(Cell neighbor) {
        setNeighBor(TOP, neighbor);
    }
    public Cell getBottomNeighbor() {
        return getNeighBor(BOTTOM);
    }
    public void setBottomNeighbor(Cell neighbor) {
        setNeighBor(BOTTOM, neighbor);
    }

    public Cell getTopRightNeighbor() {
        return getNeighBor(TOP_RIGHT);
    }
    public void setTopRightNeighbor(Cell neighbor) {
        setNeighBor(TOP_RIGHT, neighbor);
    }
    public Cell getTopLeftNeighbor() {
        return getNeighBor(TOP_LEFT);
    }
    public void setTopLefNeighbor(Cell neighbor) {
        setNeighBor(TOP_LEFT, neighbor);
    }
    public Cell getBottomRightNeighbor() {
        return getNeighBor(BOTTOM_RIGHT);
    }
    public void setBottomRightNeighbor(Cell neighbor) {
        setNeighBor(BOTTOM_RIGHT, neighbor);
    }
    public Cell getBottomLeftNeighbor() {
        return getNeighBor(BOTTOM_LEFT);
    }
    public void setBottomLeftNeighbor(Cell neighbor) {
        setNeighBor(BOTTOM_LEFT, neighbor);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
    public void decrementCost() {
        cost--;
        if(cost < MIN_COST_VALUE)
            cost = MIN_COST_VALUE;
    }

    public Cell getLocation(Point current){
        if(current == (Point) this) return this;
        if(current == (Point) this.getBottomNeighbor()) return this.getBottomNeighbor();
        if(current == (Point) this.getBottomLeftNeighbor()) return this.getBottomLeftNeighbor();
        if(current == (Point) this.getLeftNeighbor()) return this.getLeftNeighbor();
        if(current == (Point) this.getTopLeftNeighbor()) return this.getTopLeftNeighbor();
        if(current == (Point) this.getTopNeighbor()) return this.getTopNeighbor();
        if(current == (Point) this.getTopRightNeighbor()) return this.getTopRightNeighbor();
        if(current == (Point) this.getRightNeighbor()) return this.getRightNeighbor();
        if(current == (Point) this.getBottomRightNeighbor()) return this.getBottomRightNeighbor();
        return new Cell(new Point(-1,-1));
    }
}
