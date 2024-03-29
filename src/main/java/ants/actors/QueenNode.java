package ants.actors;

import ants.environment.Cell;
import io.jbotsim.core.Node;

import java.util.Random;

public class QueenNode extends CellLocatedNode {

    private int foodStock;
    private int lifetime;


    public void increaseFoodStock(){
        foodStock++;
        System.out.println("Merci mon fidèle sujet");
    }

    public QueenNode(){
        super();
        foodStock = 100 ;
        lifetime = 0;
        setIcon("/images/ant-queen.png");
        setIconSize(getIconSize()*2);
    }

    @Override
    public void onClock() {
        currentCell.addPheromones(0);
        lifetime ++;
        if (shouldProduceOffspring())
            produceOffspring();

    }

    @Override
    public void onStart(){
        super.onStart();
        onSensingIn(this);
        setSensingRange(45);
    }

    @Override
    public void onPostClock() {
        super.onPostClock();
    }

    private boolean shouldProduceOffspring() {
        return new Random().nextDouble() < 0.01;
    }

    public void produceOffspring(){
        foodStock--;

        if(foodStock <= 0) {
            die();
            System.out.println("Temps de survie : "+ lifetime);
            System.exit(0);
        }

        AntNode babyAnt = new AntNode();
        babyAnt.setCurrentCell(getCurrentCell());
        getTopology().addNode(babyAnt);
    }

}
