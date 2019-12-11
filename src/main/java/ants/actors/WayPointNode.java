package ants.actors;

import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import java.util.LinkedList;
import java.util.Queue;

public class WayPointNode extends Node{
    private double speed = 1; // Number of units to be moved in each step.
    private Queue<Point> destinations = new LinkedList<Point>();

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Queue<Point> getDestinations() {
        return destinations;
    }

    public void setDestinations(Queue<Point> destinations) {
        this.destinations = destinations;
    }

    public void addDestination(Point added) {
        destinations.add(added);
    }

    @Override
    public void onClock() {
        if(!(this.destinations.isEmpty())) {
            double distance = distance(destinations.element());
            if(distance > this.speed) {
                setDirection(destinations.element());
                move(this.speed);
            } else {
                onArrival();
                setLocation(destinations.remove());
            }
        }
    }

    public void onArrival() {

    }
}
