package ants.environment;

import ants.actors.CellLocatedNode;
import io.jbotsim.core.Node;

import java.util.Random;

public class ObstacleNode extends CellLocatedNode {

    public ObstacleNode(){
        super();
        setIcon("/images/ant-obstacle.png");
        setIconSize(getIconSize()*2);
        setWirelessStatus(false);
    }
}
