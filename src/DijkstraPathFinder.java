// DijkstraPathFinder.java  -- Solution B
// Working data structure: a HashMap-based distance table.
// Finds the path with the shortest total distance in km.
// We follow the steps from Module 11: a table of the shortest known distance
// and the previous vertex, and each round we pick the unvisited vertex with
// the smallest distance.
import java.util.ArrayList;
import java.util.HashMap;

public class DijkstraPathFinder {

    static final double INFINITY = Double.MAX_VALUE;

    // holds the answer: the path and its total distance
    static class Result {
        ArrayList<String> path;
        double totalDistance;
    }

    public static Result find(MetroGraph g, String start, String end) {
        // no route if a code is not in the graph
        if (!g.hasCode(start) || !g.hasCode(end)) {
            Result none = new Result();
            none.path = null;
            none.totalDistance = INFINITY;
            return none;
        }

        HashMap<String, Double> dist = new HashMap<String, Double>();  // shortest known distance
        HashMap<String, String> prev = new HashMap<String, String>(); // previous vertex
        HashMap<String, Boolean> visited = new HashMap<String, Boolean>();

        // Step 1: distance to start = 0, everything else = infinity
        for (String code : g.stations.keySet()) {
            dist.put(code, INFINITY);
        }
        dist.put(start, 0.0);

        // Step 2: repeat until all reachable vertices are visited
        while (true) {
            // pick the unvisited vertex with the smallest known distance
            String cur = null;
            double smallest = INFINITY;
            for (String code : g.stations.keySet()) {
                if (!visited.containsKey(code) && dist.get(code) < smallest) {
                    smallest = dist.get(code);
                    cur = code;
                }
            }
            if (cur == null) break;   // nothing left to reach
            visited.put(cur, true);
            if (cur.equals(end)) break; // we reached the destination

            // update the distances of the current vertex's neighbours
            ArrayList<MetroGraph.Edge> neighbours = g.adj.get(cur);
            for (int i = 0; i < neighbours.size(); i++) {
                String next = neighbours.get(i).to;
                double newDist = dist.get(cur) + neighbours.get(i).distance;
                if (newDist < dist.get(next)) {
                    dist.put(next, newDist);
                    prev.put(next, cur);
                }
            }
        }

        // build the path backwards from end to start
        Result r = new Result();
        r.totalDistance = dist.get(end);

        // still infinity means we never reached the end, so there is no route
        if (r.totalDistance == INFINITY) {
            r.path = null;
            return r;
        }

        r.path = new ArrayList<String>();
        String cur = end;
        while (cur != null) {
            r.path.add(0, cur);
            if (cur.equals(start)) break;
            cur = prev.get(cur);
        }
        return r;
    }
}
