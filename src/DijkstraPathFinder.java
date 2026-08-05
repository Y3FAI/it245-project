// DijkstraPathFinder.java  -- Solution 2
// This finds the route with the shortest total distance in km.
// The working data structure is a HashMap distance table.
// We follow the same steps from Module 11: keep a table of the shortest
// known distance to each station, and each round pick the unvisited station
// with the smallest distance.
import java.util.ArrayList;
import java.util.HashMap;

public class DijkstraPathFinder {

    // a very large number to use as infinity
    static final double INFINITY = Double.MAX_VALUE;

    // This holds the result: the path and the total distance.
    static class Result {
        ArrayList<String> path;
        double totalDistance;
    }

    // Find the path from start to end with the shortest distance.
    // Returns a Result object with the path and the total km.
    public static Result find(MetroGraph g, String start, String end) {
        // if either code does not exist, there is no route
        if (!g.hasCode(start) || !g.hasCode(end)) {
            Result none = new Result();
            none.path = null;
            none.totalDistance = INFINITY;
            return none;
        }

        // dist is the distance table: station code -> shortest known distance
        HashMap<String, Double> dist = new HashMap<String, Double>();
        // prev stores which station we came from
        HashMap<String, String> prev = new HashMap<String, String>();
        // visited stores the stations we have already processed
        HashMap<String, Boolean> visited = new HashMap<String, Boolean>();

        // Step 1: set the distance to every station as infinity,
        // except the start station which is 0
        for (String code : g.stations.keySet()) {
            dist.put(code, INFINITY);
        }
        dist.put(start, 0.0);

        // Step 2: keep going until we have visited all reachable stations
        while (true) {
            // find the unvisited station with the smallest distance
            String cur = null;
            double smallest = INFINITY;
            for (String code : g.stations.keySet()) {
                if (!visited.containsKey(code) && dist.get(code) < smallest) {
                    smallest = dist.get(code);
                    cur = code;
                }
            }
            if (cur == null) break;   // no more stations we can reach
            visited.put(cur, true);
            if (cur.equals(end)) break; // we found the destination

            // for each neighbour of the current station, check if going
            // through the current station gives a shorter route
            ArrayList<MetroGraph.Edge> neighbours = g.adj.get(cur);
            for (int i = 0; i < neighbours.size(); i++) {
                String next = neighbours.get(i).to;
                double newDist = dist.get(cur) + neighbours.get(i).distance;
                if (newDist < dist.get(next)) {
                    // we found a shorter way to get to 'next'
                    dist.put(next, newDist);
                    prev.put(next, cur);
                }
            }
        }

        // build the path by walking backwards from end to start
        Result r = new Result();
        r.totalDistance = dist.get(end);

        // if the distance is still infinity, we never reached the end
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
