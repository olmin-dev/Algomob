package ants.environment;

import io.jbotsim.core.Topology;

public class ObstacleSpawner{

    private Topology tp;
    private Environment environment;

    public ObstacleSpawner(Topology topology, Environment environment) {
        tp = topology;
        this.environment = environment;
    }

    public void spawnRandomObstacle() {
        ObstacleNode n = new ObstacleNode();
        Cell location = environment.getRandomLocationGauss(0.5,8);
        n.setLocation(location);
        n.setCurrentCell(location);
        n.setObstacle(true);
        tp.addNode(n);
        }
    }
