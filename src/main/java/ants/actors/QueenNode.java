package ants.actors;

import ants.environment.Cell;
import io.jbotsim.core.Node;

import java.util.Random;

public class QueenNode extends CellLocatedNode {

    private int foodStock;

    public void increaseFoodStock(){
        foodStock++;
    }

    public QueenNode(){
        super();
        foodStock = 10;
        setIcon("/images/ant-queen.png");
        setIconSize(getIconSize()*2);
    }

    @Override
    public void onClock() {

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
            return;
        }

        AntNode babyAnt = new AntNode();
        babyAnt.setCurrentCell(getCurrentCell());
        getTopology().addNode(babyAnt);
    }

}
