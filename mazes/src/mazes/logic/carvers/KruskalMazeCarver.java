package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // Hint: you'll probably need to include something like the following:
        // this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges))
        Collection<EdgeWithData<Room, Wall>> newWalls = new ArrayList<>();
        Set<Wall> result = new HashSet<>();

        for (Wall wall : walls) {
            Room room1 = wall.getRoom1();
            Room room2 = wall.getRoom2();
            double randWeight = rand.nextDouble();
            newWalls.add(new EdgeWithData<>(room1, room2, randWeight, wall));
        }

        MazeGraph myGraph = new MazeGraph(newWalls);
        Collection<EdgeWithData<Room, Wall>> edges =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(myGraph).edges();

        for (EdgeWithData<Room, Wall> edge : edges) {
            result.add(edge.data());
        }
        return result;
    }
}
