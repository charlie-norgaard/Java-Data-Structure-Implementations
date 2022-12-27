package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes the shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    // Returns a (partial) shortest paths tree (a map from vertex to preceding edge)
    // containing the shortest path from start to end in given graph.
    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Set<V> visited = new HashSet<>();  // allows us to keep track of vertices that we've already visited
        Map<V, E> edgeTo = new HashMap<>();  // stores vertices and edges (shortestPathMap)
        Map<V, Double> distTo = new HashMap<>();  // stores a vertex and the distance from its predecessor
        ExtrinsicMinPQ<E> distancePQ = createMinPQ(); // allows us to sort path lengths from least to greatest
        double infinity = Double.POSITIVE_INFINITY; // used to initialize path lengths from a vertex to infinity

        distTo.put(start, 0.0); // initialize distance from start as 0
        V currVertex = start;  // vertex we are starting our path search from

        if (graph.outgoingEdgesFrom(start).size() < 1) {
            return edgeTo;
        }

        while (!Objects.equals(currVertex, end)) {

            // add current vertex to list of visited vertices
            visited.add(currVertex);

            // Collection of edges
            Collection<E> edges = graph.outgoingEdgesFrom(currVertex);

            for (E edge : edges) {
                // Initialize path length to infinity
                if (!distTo.containsKey(edge.to())) {
                    distTo.put(edge.to(), Double.POSITIVE_INFINITY);
                }

                // find old and new distances between start and currVertex
                double oldDist = distTo.get(edge.to()); // INF
                double newDist = distTo.get(currVertex) + edge.weight(); // 0 + w

                // if the new path length is shorter -------
                if (newDist < oldDist) {
                    // v.dist = u.dist+weight(u,v)
                    distTo.put(edge.to(), newDist); // update distance from start to this vertex

                    // v.predecessor = u
                    edgeTo.put(edge.to(), edge); // add new shortest path from start to

                    // add new dist from start to vertex to minPQ (so we can decide what vertex to start from next)
                    distancePQ.add(edge, newDist);
                }
            }
            // Now that we have found the shortest edge and its corresponding vertex (shortestPath.to()),
            // we need to change currVertex to this vertex, so we can look at its edges

            // Get the shortest path to next vertex (remove first item in minPQ)

            // Need to check if minPQ is empty or not
            if (!distancePQ.isEmpty()) {
                E shortestPath = distancePQ.removeMin();
                if (!visited.contains(shortestPath.to())) {
                    currVertex = shortestPath.to();
                } else {
                    currVertex = distancePQ.removeMin().to();
                }
            }
        }

        return edgeTo;
    }

    // Extracts the shortest path from start to end from the given shortest paths tree.
    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {v
        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        } else {
            List<E> edges = new ArrayList<>();
            for (V vertex : spt.keySet()) {
                E edge = spt.get(vertex);
                edges.add(edge);
            }
            return new ShortestPath.Success<>(edges);
        }
    }

}
