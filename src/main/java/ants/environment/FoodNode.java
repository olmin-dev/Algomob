package ants.environment;

import ants.actors.CellLocatedNode;
import io.jbotsim.core.Node;

import java.util.Random;

public class FoodNode extends CellLocatedNode {

    public static final int MAX_QUANTITY = 10;
    public static final int MIN_QUANTITY = 10;
    private int quantity;
    private int lifetime = 1000;

    public FoodNode(){
        super();
        setIcon("/images/ant-worm.png");
        setWirelessStatus(false);

        setDirection(new Random().nextDouble()*2*Math.PI);
        quantity = new Random().nextInt(MAX_QUANTITY) + MIN_QUANTITY;

        setIconSize((int)(getIconSize()* quantity /10*0.9));
    }

    @Override
    public void onPostClock() {

        super.onPostClock();
        lifetime --;
        if (lifetime <= 0){
            die();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        onSensingIn(this);
        setSensingRange(45);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (quantity <= 0)
            die();

    }

    public int getQuantity(){
        return quantity;
    }

    public void decreaseQuantity(){
        quantity--;
        if(quantity == 0){
            die();
        }
    }

}
