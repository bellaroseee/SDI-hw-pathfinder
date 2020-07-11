package pathfinder.parser;

import graph.DiGraph;
import pathfinder.datastructures.Path;

import java.util.*;

/**
 * Dijkstra Path Finder Implementation
 */
public class Dijkstra {
    // if Dijkstra is an ADT, RI and AF would go here

    /**
     * Finds the shortest path with Dijkstra algorithm in the graph
     * from node start to node dest.
     * null is returned if no path is found.
     *
     * @param start node
     * @param dest node
     * @param graph to find path from start to dest
     * @param <Node> type of node
     * @param <T> type of edges between nodes
     * @return a Path from start node to dest node
     */
    public static <Node, T> Path<Node> findPath(Node start, Node dest, DiGraph<Node, T> graph) {
        // Dijkstra's algorithm assumes a graph with nonnegative edge weights.
        Comparator<Path<Node>> comparator = Comparator.comparingDouble(Path::getCost);
        PriorityQueue<Path<Node>> active = new PriorityQueue<>(comparator);
        // Each element is a path from start to a given node.
        // A path's “priority” in the queue is the total cost of that path.
        // Nodes for which no path is known yet are not in the queue.
        Set<Node> finished = new HashSet<>();
        // set of nodes for which we know the minimum-cost path from start

        Path<Node> p = new Path<>(start);
        active.add(p);

        while(!active.isEmpty()) {
            // minPath is the lowest-cost path in active and,
            // if minDest isn't already 'finished,' is the
            // minimum-cost path to the node minDest
            Path<Node> minPath = active.remove();
            Node minDest = minPath.getEnd();
            if (minDest.equals(dest)) {
                return minPath;
            }
            if (finished.contains(minDest)) {
                continue;
            }
            List<DiGraph.LabeledEdge<Node, T>> edgeList = graph.listChildren(minDest);
            // For all children of minDest
            for(DiGraph.LabeledEdge<Node, T> e : edgeList) {
                // If we don't know the minimum-cost path from start to child,
                // examine the path we've just found
                if (!finished.contains(e.getTo())) { //or get From?
                    Path<Node> newPath = minPath.extend(e.getTo(), (Double) e.getLabel());
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        return null;
        // If the loop terminates, then no path exists from start to dest.
        // The implementation should indicate this to the client.
    }
}
