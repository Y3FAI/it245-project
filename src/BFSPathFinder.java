// BFSPathFinder.java  -- Solution A
// Working data structure: a FIFO queue (Queue with LinkedList).
// Finds the path with the fewest stations.
// This is the breadth-first search from Module 11.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BFSPathFinder {

    // Returns the list of station codes from start to end (or null if none).
    public static ArrayList<String> find(MetroGraph g, String start, String end) {
        // no route if a code is not in the graph
        if (!g.hasCode(start) || !g.hasCode(end)) {
            return null;
        }

        Queue<String> queue = new LinkedList<String>();
        HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
        HashMap<String, String> prev = new HashMap<String, String>();

        queue.add(start);
        visited.put(start, true);

        while (!queue.isEmpty()) {
            String cur = queue.remove();
            if (cur.equals(end)) {
                return buildPath(prev, start, end);
            }
            ArrayList<MetroGraph.Edge> neighbours = g.adj.get(cur);
            for (int i = 0; i < neighbours.size(); i++) {
                String next = neighbours.get(i).to;
                if (!visited.containsKey(next)) {
                    visited.put(next, true);
                    prev.put(next, cur);
                    queue.add(next);
                }
            }
        }
        return null; // no path found
    }

    // Walk backwards from end to start using the prev map, then reverse.
    private static ArrayList<String> buildPath(HashMap<String, String> prev,
                                               String start, String end) {
        ArrayList<String> path = new ArrayList<String>();
        String cur = end;
        while (cur != null) {
            path.add(0, cur); // add to the front
            if (cur.equals(start)) break;
            cur = prev.get(cur);
        }
        return path;
    }
}
