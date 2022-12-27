package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        //return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // Disjoint set for optimizer
        DisjointSets<V> disjointSets = createDisjointSets();

        // 1) initialize each vertex into their own components
        Collection<V> vertices = graph.allVertices();
        for (V vertex : vertices) {
            disjointSets.makeSet(vertex);
        }

        // 2) sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        // HasSet for storing the final MST
        HashSet<E> finalMST = new HashSet<>();

        // initializing result as MST (check MinimumSpanningTree.java to see where this comes from)\
        // we need to track the occurrences of vertexes in the set, so we don't make a cyclic mst
        for (E edge : edges) {
            V from = edge.from();
            V to = edge.to();

            if (from != to) {
                // if union is possible, add edge
                if (disjointSets.union(from, to)) {
                    finalMST.add(edge);
                }
            }
        }

        // check if a valid MST was created
        // for valid MST, number of edges is number of vertices - 1
        if (finalMST.size() == vertices.size() - 1 || vertices.size() == 0) {
            return new MinimumSpanningTree.Success<>(finalMST);
        } else {
            return new MinimumSpanningTree.Failure<>();
        }

    }
}
