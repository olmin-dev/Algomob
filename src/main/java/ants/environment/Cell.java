package ants.environment;

import io.jbotsim.core.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cell extends Point {

    public static final int MAX_COST_VALUE = 40;
    public static final int MIN_COST_VALUE = 1;
    private int cost;
    private boolean is_obstacle;

    private int PV;
    private ArrayList<Integer> pheromones;

    public int colorToPV(int color){
        int a = color - Cell.MIN_COST_VALUE;
        return cost * 10;
    }

    public int PVToColor(int PV){
        int a = PV / 10  + MIN_COST_VALUE;
        if(a < MIN_COST_VALUE) a = MIN_COST_VALUE;
        if(a > MAX_COST_VALUE) a = MAX_COST_VALUE;
        return a;
    }

    public boolean isPheromoneQueen(){
        return pheromones.get(0) == 0;
    }

    public boolean isPheromoneFood(){
        return pheromones.get(1) == 0;
    }

    public int getPheromoneQueen() { return pheromones.get(0);}

    public int getPheromoneFood() { return pheromones.get(1);}

    public ArrayList<Integer> getAroundPheromonesFood(){
        ArrayList<Integer> pheromones = new ArrayList<>();
        if(getRightNeighbor() != null)  pheromones.add(getRightNeighbor().getPheromoneFood());
        if(getBottomRightNeighbor() != null) pheromones.add(getBottomRightNeighbor().getPheromoneFood());
        if(getBottomNeighbor() != null)  pheromones.add(getBottomNeighbor().getPheromoneFood());
        if(getBottomLeftNeighbor() != null)  pheromones.add(getBottomLeftNeighbor().getPheromoneFood());
        if(getLeftNeighbor() != null)  pheromones.add(getLeftNeighbor().getPheromoneFood());
        if(getTopLeftNeighbor() != null)  pheromones.add(getTopLeftNeighbor().getPheromoneFood());
        if(getTopNeighbor() != null)  pheromones.add(getTopNeighbor().getPheromoneFood());
        if(getTopRightNeighbor() != null)  pheromones.add(getTopRightNeighbor().getPheromoneFood());
        return pheromones;
    }

    public ArrayList<Integer> getAroundPheromonesQueen(){
        ArrayList<Integer> pheromones = new ArrayList<>();
        if(getRightNeighbor() != null)  pheromones.add(getRightNeighbor().getPheromoneQueen());
        if(getBottomRightNeighbor() != null) pheromones.add(getBottomRightNeighbor().getPheromoneQueen());
        if(getBottomNeighbor() != null)  pheromones.add(getBottomNeighbor().getPheromoneQueen());
        if(getBottomLeftNeighbor() != null)  pheromones.add(getBottomLeftNeighbor().getPheromoneQueen());
        if(getLeftNeighbor() != null)  pheromones.add(getLeftNeighbor().getPheromoneQueen());
        if(getTopLeftNeighbor() != null)  pheromones.add(getTopLeftNeighbor().getPheromoneQueen());
        if(getTopNeighbor() != null)  pheromones.add(getTopNeighbor().getPheromoneQueen());
        if(getTopRightNeighbor() != null)  pheromones.add(getTopRightNeighbor().getPheromoneQueen());
        return pheromones;
    }

    public ArrayList<Cell> getFoodCells(int maxFoodPher){
        ArrayList<Cell> ret = new ArrayList<>();

        if(getRightNeighbor() != null && getRightNeighbor().getPheromoneFood() == maxFoodPher)  ret.add(getRightNeighbor());
        if(getBottomRightNeighbor() != null && getBottomRightNeighbor().getPheromoneFood() == maxFoodPher) ret.add(getBottomRightNeighbor());
        if(getBottomNeighbor() != null && getBottomNeighbor().getPheromoneFood() == maxFoodPher)  ret.add(getBottomNeighbor());
        if(getBottomLeftNeighbor() != null && getBottomLeftNeighbor().getPheromoneFood() == maxFoodPher)  ret.add(getBottomLeftNeighbor());
        if(getLeftNeighbor() != null && getLeftNeighbor().getPheromoneFood() == maxFoodPher)  ret.add(getLeftNeighbor());
        if(getTopLeftNeighbor() != null && getTopLeftNeighbor().getPheromoneFood() == maxFoodPher)  ret.add(getTopLeftNeighbor());
        if(getTopNeighbor() != null && getTopNeighbor().getPheromoneFood() == maxFoodPher)  ret.add(getTopNeighbor());
        if(getTopRightNeighbor() != null && getTopRightNeighbor().getPheromoneFood() == maxFoodPher)  ret.add(getTopRightNeighbor());


        return ret;
    }

    public ArrayList<Cell> getQueenCells(int maxQueenPher){
        ArrayList<Cell> ret = new ArrayList<>();
        if(getRightNeighbor() != null && getRightNeighbor().getPheromoneQueen() == maxQueenPher)  ret.add(getRightNeighbor());
        if(getBottomRightNeighbor() != null && getBottomRightNeighbor().getPheromoneQueen() == maxQueenPher)  ret.add(getBottomRightNeighbor());
        if(getBottomNeighbor() != null && getBottomNeighbor().getPheromoneQueen() == maxQueenPher)  ret.add(getBottomNeighbor());
        if(getBottomLeftNeighbor() != null && getBottomLeftNeighbor().getPheromoneQueen() == maxQueenPher)  ret.add(getBottomLeftNeighbor());
        if(getLeftNeighbor() != null && getLeftNeighbor().getPheromoneQueen() == maxQueenPher)  ret.add(getLeftNeighbor());
        if(getTopLeftNeighbor() != null && getTopLeftNeighbor().getPheromoneQueen() == maxQueenPher)  ret.add(getTopLeftNeighbor());
        if(getTopNeighbor() != null && getTopNeighbor().getPheromoneQueen() == maxQueenPher) ret.add(getTopNeighbor());
        if(getTopRightNeighbor() != null && getTopRightNeighbor().getPheromoneQueen() == maxQueenPher) ret.add(getTopRightNeighbor());
        return ret;
    }


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

    public synchronized void addPheromones(int type){
        int somme = 0;
        int autretype = Math.abs((type - 1) % 2);

        for (int i = 0; i < pheromones.size(); i++){
            somme += pheromones.get(i);
        }
        if(somme == 100){
            if(pheromones.get(autretype) > 0) {
                pheromones.set(autretype, pheromones.get(autretype) - 10);
                pheromones.set(type, pheromones.get(type) + 10);
            }
        } else if (somme < 100){
                pheromones.set(type, pheromones.get(type) + 10);
            }
    }


    public Cell(Point location){
        super(location);
        double rand = Math.random();
        is_obstacle = false;
        pheromones = new ArrayList<Integer>();
        Random r = new Random();
        int alt_multiplifier = (int) (Math.floor(location.y)/20);
        cost = (int) (r.nextGaussian() * 10 ) + alt_multiplifier;
        if (cost <= 1){
            cost = 2;
        }
        if (cost >= 40){
            cost = 40;
        }
        PV = colorToPV(cost);
        pheromones.add(0);
        pheromones.add(0);
    }

    public boolean getIs_obstacle(){
        return is_obstacle;
    }
    public void setIs_obstacle(boolean obstacle){
        is_obstacle = obstacle;
    }

    public void decreasePheromones(){
        int type = (int) Math.floor(Math.random() * 2);
        if(pheromones.get(0) == 0 && pheromones.get(1) == 0) return;
        if (pheromones.get(0) == 0 && type == 0){
            type = 1;
        }
        else if (pheromones.get(1) == 0 && type == 1) {
            type = 0;
        }
        pheromones.set(type,pheromones.get(type)-10);
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

    public Cell getLocation(Point current) {
        if (current == (Point) this) return this;
        if (current == (Point) this.getBottomNeighbor()) return this.getBottomNeighbor();
        if (current == (Point) this.getBottomLeftNeighbor()) return this.getBottomLeftNeighbor();
        if (current == (Point) this.getLeftNeighbor()) return this.getLeftNeighbor();
        if (current == (Point) this.getTopLeftNeighbor()) return this.getTopLeftNeighbor();
        if (current == (Point) this.getTopNeighbor()) return this.getTopNeighbor();
        if (current == (Point) this.getTopRightNeighbor()) return this.getTopRightNeighbor();
        if (current == (Point) this.getRightNeighbor()) return this.getRightNeighbor();
        if (current == (Point) this.getBottomRightNeighbor()) return this.getBottomRightNeighbor();
        return new Cell(new Point(-1, -1));
    }

}
