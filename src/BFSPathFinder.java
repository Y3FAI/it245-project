// BFSPathFinder.java  -- Solution 1
// This finds the route with the fewest stops using Breadth-First Search.
// The working data structure is a FIFO queue (Queue made with LinkedList).
// This is the same search we learned in Module 11.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BFSPathFinder {

    // Find the path from start to end.
    // Returns the list of station codes, or null if there is no path.
    public static ArrayList<String> find(MetroGraph g, String start, String end) {
        // if either code does not exist, there is no route
        if (!g.hasCode(start) || !g.hasCode(end)) {
            return null;
        }

        Queue<String> queue = new LinkedList<String>();      // stations to visit next
        HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
        HashMap<String, String> prev = new HashMap<String, String>(); // how we reached each station

        // start from the start station
        queue.add(start);
        visited.put(start, true);

        // keep taking stations from the front of the queue
        while (!queue.isEmpty()) {
            String cur = queue.remove();
            if (cur.equals(end)) {
                // we reached the end station, build the route
                return buildPath(prev, start, end);
            }
            // look at all stations next to the current one
            ArrayList<MetroGraph.Edge> neighbours = g.adj.get(cur);
            for (int i = 0; i < neighbours.size(); i++) {
                String next = neighbours.get(i).to;
                if (!visited.containsKey(next)) {
                    visited.put(next, true);
                    prev.put(next, cur);
                    queue.add(next); // put it at the back of the queue
                }
            }
        }
        return null; // the queue became empty, so there is no path
    }

    // Rebuild the route by walking backwards from end to start using prev,
    // then reverse it so it goes from start to end.
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
