package ants.environment;

import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import ants.actors.QueenNode;

import java.util.Random;
import java.util.Vector;

public class Environment implements ClockListener {
    private Vector<Vector> env;
    private Topology tp;
    private final int nbColumn;
    private final int nbRow;
    private Point gridStartLocation;
    private final double elementWidth;
    private final double elementHeight;
    private final Random locationRandom;
    private QueenNode queen;

    public Environment(Topology tp, int nbColumn, int nbRow) {
        this.tp = tp;
        this.nbColumn = nbColumn;
        this.nbRow = nbRow;
        elementWidth = tp.getWidth()/nbColumn;
        double usedHeight = tp.getHeight() * 2. / 3;
        elementHeight = usedHeight / nbRow;

        locationRandom = new Random();

        createEnvironment(tp);

    }

    @Override
    public void onClock() {
        if(tp.getTime() % 1000 == 0){
            for(int i = 0; i < nbColumn; i++) {
                for (int j = 0; j < nbRow;j++) {
                    Cell cell =(Cell) env.get(i).get(j);
                    cell.decreasePheromones();
                }
            }
        }
    }

    private void createEnvironment(Topology tp) {

        env = new Vector<>(nbColumn);

        double gridStartY = tp.getHeight() * 1. / 3 + elementHeight /2;
        double gridStartX = 0 + elementWidth /2;
        gridStartLocation = new Point(gridStartX, gridStartY);

        createCells(elementWidth, elementHeight);
        initializeObstacle(50, tp);
        initializeFood(10, tp);
        updateNeighborhood();
        initializeQueen(tp);
    }

    private void initializeFood(int nb, Topology tp) {
        FoodSpawner foodSpawner = new FoodSpawner(tp, this);
        for(int i = 0; i<nb;i++)
            foodSpawner.spawnRandomFood();
    }

    public QueenNode getQueen() {
        return queen;
    }

    public void initializeQueen(Topology tp) {

        queen = new QueenNode();
        Cell queenCell = this.getRandomLocationGauss(0.2, 5);


        while (queenCell.getIs_obstacle()) {
            queenCell = this.getRandomLocationGauss(0.2, 5);
            queen.setCurrentCell(queenCell);
            queen.setLocation(queenCell);
        }
        queen.setCurrentCell(queenCell);
        queen.setLocation(queenCell);

        queenCell.setCost(Cell.MIN_COST_VALUE);
        if(queenCell.getBottomNeighbor() != null) queenCell.getBottomNeighbor().setCost(Cell.MIN_COST_VALUE);
        if(queenCell.getRightNeighbor() != null)queenCell.getRightNeighbor().setCost(Cell.MIN_COST_VALUE);
        if(queenCell.getLeftNeighbor() != null)queenCell.getLeftNeighbor().setCost(Cell.MIN_COST_VALUE);
        if(queenCell.getTopNeighbor() != null)queenCell.getTopNeighbor().setCost(Cell.MIN_COST_VALUE);

        tp.addNode(queen);

    }

    private void initializeObstacle(int nb, Topology tp) {
        ObstacleSpawner obstacleSpawner= new ObstacleSpawner(tp, this);
        for(int i = 0; i<nb;i++)
            obstacleSpawner.spawnRandomObstacle();
    }

    private void createCells(double width, double height) {
        for(int x = 0; x < nbColumn; x++)
            for(int y = 0; y < nbRow; y++)
                addCell(x, y, createCell(x, y, width, height));
    }

    private void updateNeighborhood() {
        for(int x = 0; x < nbColumn; x++)
            for(int y = 0; y < nbRow; y++)
                if(!getElement(x, y).getIs_obstacle()) {
                    updateNeighbors(x, y);
                }
    }

    private void updateNeighbors(int x, int y) {
        Cell current = getElement(x, y);

        if(x>0 && !getElement(x - 1, y).getIs_obstacle())
            current.setLefNeighbor(getElement(x-1, y));

        if(x>0 && y>0 && !getElement(x - 1, y - 1).getIs_obstacle())
            current.setTopLefNeighbor(getElement(x-1, y-1));

        if(x>0 && y<nbRow-1 && !getElement(x - 1, y + 1).getIs_obstacle())
            current.setBottomLeftNeighbor(getElement(x-1, y+1));

        if(y>0 && !getElement(x, y - 1).getIs_obstacle())
            current.setTopNeighbor(getElement(x, y-1));

        if(x<nbColumn-1 && !getElement(x + 1, y).getIs_obstacle())
            current.setRightNeighbor(getElement(x+1, y));

        if(x<nbColumn-1 && y>0 && !getElement(x + 1, y - 1).getIs_obstacle())
            current.setTopRightNeighbor(getElement(x+1, y-1));

        if(x<nbColumn-1 && y<nbRow-1 && !getElement(x + 1, y + 1).getIs_obstacle())
            current.setBottomRightNeighbor(getElement(x+1, y+1));

        if(y<nbRow-1 && !getElement(x, y + 1).getIs_obstacle())
            current.setBottomNeighbor(getElement(x, y+1));
    }

    public Cell getElement(int x, int y) {
        return (Cell) env.get(x).get(y);
    }

    private void addCell(int x, int y, Cell value) {
        Vector column = null;
        if(env.size()<=x) {
            column = new Vector<Cell>(nbRow);
            env.add(x, column);
        }
        column = env.get(x);

        column.add(y, value);

    }

    private Cell createCell(int x, int y, double width, double height) {
        Point location = new Point(gridStartLocation.getX() + x * width, gridStartLocation.getY() + y * height);
        Cell cell = new Cell(location);
        return cell;
    }

    public Vector<Vector> getGrid() {
        return env;
    }

    public double getElementWidth() {
        return elementWidth;
    }
    public double getElementHeight() {
        return elementHeight;
    }

    public Cell getRandomLocation() {
        return getElement(locationRandom.nextInt(nbColumn-2)+1, locationRandom.nextInt(nbRow-2)+1);
    }

    public Cell getRandomLocationGauss(double relative_height, int derive) {
        Random r = new Random();
        double alt = (nbRow - 2) * relative_height;
        int moy = (int) Math.floor(alt);
        int val = (int) Math.round(r.nextGaussian() * derive) + moy;
        if (val > nbRow - 2) {
            val = nbRow - 2;
        }
        if (val < 1){
            val = 1;
        }
        return getElement(locationRandom.nextInt(nbColumn-2)+1, val + 1);
    }

    public int getNbColumn() {
        return nbColumn;
    }

    public int getNbRow() {
        return nbRow;
    }

}
