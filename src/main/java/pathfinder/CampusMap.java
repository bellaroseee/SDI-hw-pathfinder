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

package pathfinder;

import graph.DiGraph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;
import pathfinder.parser.Dijkstra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI {

    // AF(this):
    //      graph with points and distance between them => map
    //      building short name and its ong name => campusBuilding
    //      building shortname and its point coordinate => campusPoint

    // Rep Invariant:
    //      map != null &&
    //      campusBuilding != null &&
    //      campusPoint != null &&
    //      distance between points is positive

    private DiGraph<Point, Double> map = new DiGraph<>();
    private Map<String, String> campusBuilding = new HashMap<>();  // maps shortname to longname
    private Map<String, Point> campusPoint = new HashMap<>();  // maps shortName to Point

    private boolean CHECK = true;

    private void checkRep() {
        assert(map != null);
        assert(campusBuilding != null);
        assert(campusPoint != null);
        if (CHECK) {
            List<Point> lst = map.listNodes();
            for(Point p : lst) {
                for(DiGraph.LabeledEdge<Point, Double> e : map.listChildren(p)) {
                    assert (e.getLabel() >= 0);
                }
            }
        }
    }

    public CampusMap() {
        List<CampusBuilding> campusBuildingList = CampusPathsParser.parseCampusBuildings("campus_buildings.tsv");
        for (CampusBuilding b : campusBuildingList) {
            Point point = new Point(b.getX(), b.getY());
            map.addNode(point);
            campusPoint.put(b.getShortName(), point);
            campusBuilding.put(b.getShortName(), b.getLongName());
        }
        List<CampusPath> campusPaths = CampusPathsParser.parseCampusPaths("campus_paths.tsv");
        for (CampusPath p : campusPaths) {
            Point from = new Point(p.getX1(), p.getY1());
            Point to = new Point(p.getX2(), p.getY2());
            Double label = p.getDistance();
            if (!map.containsNode(from)) map.addNode(from);
            map.addEdge(from, to, label);
        }
        checkRep();
    }

    @Override
    public boolean shortNameExists(String shortName) {
        checkRep();
        return campusBuilding.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        if (!campusBuilding.containsKey(shortName)) {
            throw new IllegalArgumentException(shortName + "does not exist in CampusMap");
        }
        checkRep();
        return campusBuilding.get(shortName);
    }

    @Override
    public Map<String, String> buildingNames() {
        checkRep();
        return campusBuilding;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        checkRep();
        return Dijkstra.findPath(campusPoint.get(startShortName), campusPoint.get(endShortName), map);
    }

    public Map<String, Point> buildingLocations() {
        checkRep();;
        return this.campusPoint;
    }
}
