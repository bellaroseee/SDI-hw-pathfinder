/*
 * Copyright (C) 2020 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.DiGraph;
import marvel.MarvelPaths;
import pathfinder.datastructures.Path;
import pathfinder.parser.Dijkstra;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    public static void main(String[] args) {
        // You only need a main() method if you choose to implement
        // the 'interactive' test driver, as seen with GraphTestDriver's sample
        // code. You may also delete this method entirely and just
    }

    private final Map<String, DiGraph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        // See GraphTestDriver as an example.
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch (command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        DiGraph<String, Double> g = new DiGraph<>();
        if (!graphs.containsKey(graphName)) {
            graphs.put(graphName, g);
            output.println("created graph " + graphName);
            return;
        }
        output.println();
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        DiGraph<String, Double> g = graphs.get(graphName);
        g.addNode(nodeName);
        // added node n1 to graph1
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        Double edgeLabel = Double.parseDouble(arguments.get(3));

        addEdge(graphName, parentName, childName, edgeLabel);
    }
    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {
        DiGraph<String, Double> g = graphs.get(graphName);
        g.addEdge(parentName, childName, edgeLabel);
        // added edge e1 from n1 to n2 in graph1

        output.println("added edge " + String.format("%.3f", edgeLabel) + " from " + parentName +
                " to " + childName + " in " + graphName);
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        DiGraph<String, Double> g = graphs.get(graphName);
        List<DiGraph.LabeledEdge<String, Double>> children = g.listChildren(parentName);
        children.sort(Comparator.comparing(DiGraph.LabeledEdge::getTo));
        // the children of n1 in graph1 are: n2(e1) n3(e2)
        output.print("the children of " + parentName + " in " + graphName + " are:");
        if (children.isEmpty()) {
            output.print("\n");
            return;
        }
        for (int i = 0; i < children.size(); i++) {
            output.print((i == children.size() - 1) ?
                    (" " + children.get(i).getTo() + "(" + children.get(i).getLabel() + ")\n")
                    : (" " + children.get(i).getTo() + "(" + children.get(i).getLabel() + ") "));
        }
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to findPath: " + arguments);
        }

        boolean flag = false;
        String graphName = arguments.get(0);
        String node1 = arguments.get(1).replace("_", " ");
        if (!graphs.get(graphName).containsNode(node1)) {
            output.println("unknown character " + node1);
            flag = true;
        }
        String node2 = arguments.get(2).replace("_", " ");
        if (!graphs.get(graphName).containsNode(node2)) {
            output.println("unknown character " + node2);
            flag = true;
        }
        if (flag) {
            return;
        }
        findPath(graphName, node1, node2);
    }

    private void findPath(String graphName, String node1, String node2) {
        output.println("path from " + node1 + " to " + node2 + ":");
        Path<String> path = Dijkstra.findPath(node1, node2, graphs.get(graphName));
        if (path == null) {
            output.println("no path found");
            return;
        }
        Iterator<Path<String>.Segment<String>> it = path.iterator();
        double costSum = 0.0;
        if (it.hasNext()) {
            while (it.hasNext()) {
                Path<String>.Segment<String> seg = it.next();
                costSum += seg.getCost();
                output.println(seg.getStart() + " to " + seg.getEnd() + " with weight " + String.format("%.3f", seg.getCost()));
            }
        }
        output.println("total cost: " + String.format("%.3f", costSum));
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
