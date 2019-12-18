package ants.environment;

import io.jbotsim.core.Point;

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
        return pheromones.get(0) == 0;
    }

    public ArrayList<Cell> getUnexploredCells(){
        ArrayList<Cell> ret = new ArrayList<>();
        if(!getRightNeighbor().isPheromoneFood()) ret.add(getRightNeighbor());
        if(!getBottomRightNeighbor().isPheromoneFood()) ret.add(getBottomNeighbor());
        if(!getBottomNeighbor().isPheromoneFood()) ret.add(getBottomNeighbor());
        if(!getBottomLeftNeighbor().isPheromoneFood()) ret.add(getBottomLeftNeighbor());
        if(!getLeftNeighbor().isPheromoneFood()) ret.add(getLeftNeighbor());
        if(!getTopLeftNeighbor().isPheromoneFood()) ret.add(getTopLeftNeighbor());
        if(!getTopNeighbor().isPheromoneFood()) ret.add(getTopNeighbor());
        if(!getTopRightNeighbor().isPheromoneFood()) ret.add(getTopRightNeighbor());
        if(!this.isPheromoneFood()) ret.add(this);
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
        for (int i = 0; i < pheromones.size(); i++){
            somme += pheromones.get(i);
        }
        if(somme == 100){
            if(pheromones.get((Math.abs((type-1)%2))) > 0) {
                pheromones.add(Math.abs((type - 1) % 2), pheromones.get(Math.abs((type - 1) % 2)) - 10);
                pheromones.add(type, pheromones.get(type) + 10);
            }
        } else if (somme <= 100){
                pheromones.add(type, pheromones.get(type) + 10);
            }
        if (type ==  0) {
            //System.out.println("La reine est là, vive la reine : " + pheromones.get(0) + ",autre :" + pheromones.get(1));
        }
        if (type == 1) {
            //System.out.println("Omnomnomnomnom : " + pheromones.get(1) + ",autre :" + pheromones.get(0));
        }
    }


    public Cell(Point location){
        super(location);
        double rand = Math.random();
        is_obstacle = false;
        if (rand > 0.9){
            is_obstacle = true;
        }
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

        if (pheromones.get(0) == 0 && type == 0){
            type = 1;
        }
        else if (pheromones.get(1) == 0 && type == 1){
            type = 0;
        }
        else{
            return;
        }
        pheromones.add(type,pheromones.get(type)-10);
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
