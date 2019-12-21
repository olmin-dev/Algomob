package ants;

import ants.environment.*;
import ants.ui.EnvironmentBackgroundPainter;
import ants.actors.AntNode;
import ants.actors.QueenNode;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class AntHillMain {

    private Topology tp;
    public QueenNode queen;

    public static Environment environment;

    public static void main(String[] args) {
        new AntHillMain();
    }

    public AntHillMain() {
        tp = new Topology(1000,800);
        tp.disableWireless();

        tp.setNodeModel("ant", AntNode.class);
        tp.setNodeModel("queen", QueenNode.class);
        tp.setNodeModel("food", FoodNode.class);

        environment = new Environment(tp, 30, 25);
        queen = environment.getQueen();

        JViewer jv = new JViewer(tp);
        EnvironmentBackgroundPainter painter = new EnvironmentBackgroundPainter(tp, environment);
        jv.getJTopology().setDefaultBackgroundPainter(painter);

        tp.addClockListener(environment);
        tp.start();

    }

}
