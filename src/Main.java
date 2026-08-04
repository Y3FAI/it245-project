// Main.java
// The command line tool. Every run is one command, and then the program stops.
// The commands are explained in README.md.
//
// Solution A is Breadth-First Search and it uses a queue.
// Solution B is Dijkstra's algorithm and it uses a distance table.
// Both of them search the same station graph.
import java.util.ArrayList;

public class Main {

    static MetroGraph g;

    public static void main(String[] args) throws Exception {
        g = new MetroGraph();
        g.load("data/stations.csv");

        if (args.length == 0) {
            showUsage();
            return;
        }

        String command = args[0];

        if (isCommand(command, "path")) {
            showRoute(args);

        } else if (isCommand(command, "stations")) {
            showStations();

        } else if (isCommand(command, "test")) {
            runTests();

        } else {
            System.out.println("I do not know this command: " + command);
            showUsage();
        }
    }

    // The commands, in one short line. README.md explains them.
    static void showUsage() {
        System.out.println("Commands:  path <from> <to>  |  -stations  |  -test");
    }

    // The commands work with or without the '-' in front, and in any letter case,
    // so both "-stations" and "stations" are accepted.
    static boolean isCommand(String text, String name) {
        return text.equalsIgnoreCase(name) || text.equalsIgnoreCase("-" + name);
    }

    // Print every station in code order.
    static void showStations() {
        ArrayList<String> codes = sortedCodes();
        System.out.println("ALL " + codes.size() + " STATIONS");
        for (int i = 0; i < codes.size(); i++) {
            Station s = g.stations.get(codes.get(i));
            System.out.println("  " + s.code + "  " + s.name);
        }
    }

    // ----------------------------------------------------------------- routes

    // Find the route with both solutions and show them one after the other.
    static void showRoute(String[] args) {
        if (args.length != 3) {
            System.out.println("path needs two station codes, for example S05 and S53.");
            return;
        }

        String start = readCode(args[1]);
        if (start == null) {
            return;
        }
        String end = readCode(args[2]);
        if (end == null) {
            return;
        }
        if (start.equals(end)) {
            System.out.println("These are the same station, so there is no route to plan.");
            return;
        }

        Station a = g.stations.get(start);
        Station b = g.stations.get(end);
        System.out.println("ROUTE FROM  " + a.code + "  " + a.name);
        System.out.println("        TO  " + b.code + "  " + b.name);
        System.out.println("Shared data structure: the graph, a HashMap<String, ArrayList<Edge>>");
        System.out.println("adjacency list of the " + g.stations.size() + " stations.");

        ArrayList<String> bfsPath = BFSPathFinder.find(g, start, end);
        DijkstraPathFinder.Result d = DijkstraPathFinder.find(g, start, end);

        if (bfsPath == null || d.path == null) {
            System.out.println();
            System.out.println("There is no route between these two stations.");
            return;
        }

        System.out.println();
        System.out.println("SOLUTION A - the fewest stops");
        System.out.println("Algorithm: Breadth-First Search");
        System.out.println("Data structure: Queue<String> (a FIFO queue made with LinkedList)");
        printPath(bfsPath);
        System.out.println("  " + summary(bfsPath, pathDistance(bfsPath)) + ".");

        System.out.println();
        System.out.println("SOLUTION B - the shortest distance");
        System.out.println("Algorithm: Dijkstra's algorithm");
        System.out.println("Data structure: HashMap<String, Double> (the distance table)");
        printPath(d.path);
        System.out.println("  " + summary(d.path, d.totalDistance) + ".");

        compare(bfsPath, pathDistance(bfsPath), d.path, d.totalDistance);
    }

    // Print the route as a numbered list, one station on every line.
    static void printPath(ArrayList<String> path) {
        for (int i = 0; i < path.size(); i++) {
            Station s = g.stations.get(path.get(i));
            String number = "" + (i + 1);
            if (number.length() < 2) {
                number = " " + number;      // keep the numbers under each other
            }
            System.out.println("   " + number + ".  " + s.code + "  " + s.name);
        }
        System.out.println();
    }

    // "18 stops, 17.83 km"
    static String summary(ArrayList<String> path, double distance) {
        return stopsText(path.size() - 1) + ", " + km(distance) + " km";
    }

    // "1 stop" or "9 stops"
    static String stopsText(int stops) {
        if (stops == 1) {
            return "1 stop";
        }
        return stops + " stops";
    }

    // Say in normal words how the two routes are different.
    static void compare(ArrayList<String> bfsPath, double bfsKm,
                        ArrayList<String> dijPath, double dijKm) {
        System.out.println();
        System.out.println("COMPARISON");
        System.out.println("  Solution A (fewest stops)      : " + summary(bfsPath, bfsKm));
        System.out.println("  Solution B (shortest distance) : " + summary(dijPath, dijKm));

        if (samePath(bfsPath, dijPath)) {
            System.out.println("  The two solutions found the same route.");
        } else {
            System.out.println("  Solution B has "
                    + stopsText(dijPath.size() - bfsPath.size())
                    + " more, but it is about " + km(bfsKm - dijKm)
                    + " km shorter.");
        }
    }

    // ---------------------------------------------------------------- helpers

    // Turn what the user wrote into a station code, or null if it is not one.
    static String readCode(String text) {
        String code = text.trim().toUpperCase();
        if (g.hasCode(code)) {
            return code;
        }
        System.out.println("There is no station with the code \"" + text + "\".");
        System.out.println("Use -stations to see all the codes.");
        return null;
    }

    // The station codes in order (S01, S02, ...), with a simple selection sort.
    static ArrayList<String> sortedCodes() {
        ArrayList<String> codes = new ArrayList<String>();
        for (String code : g.stations.keySet()) {
            codes.add(code);
        }
        for (int i = 0; i < codes.size() - 1; i++) {
            int small = i;
            for (int j = i + 1; j < codes.size(); j++) {
                if (codes.get(j).compareTo(codes.get(small)) < 0) {
                    small = j;
                }
            }
            String temp = codes.get(i);
            codes.set(i, codes.get(small));
            codes.set(small, temp);
        }
        return codes;
    }

    // True if the two routes pass the same stations in the same order.
    static boolean samePath(ArrayList<String> a, ArrayList<String> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }
        return true;
    }

    // Add up the distance of every connection on the route.
    static double pathDistance(ArrayList<String> path) {
        double total = 0;
        for (int i = 0; i + 1 < path.size(); i++) {
            ArrayList<MetroGraph.Edge> neighbours = g.adj.get(path.get(i));
            for (int j = 0; j < neighbours.size(); j++) {
                if (neighbours.get(j).to.equals(path.get(i + 1))) {
                    total = total + neighbours.get(j).distance;
                    break;
                }
            }
        }
        return total;
    }

    // Always two numbers after the point, so 15.4238 becomes 15.42 and 20.4 becomes 20.40
    static String km(double value) {
        return String.format("%.2f", value);
    }

    // ------------------------------------------------------------- test cases

    // The fixed test cases of the project, so the results can be checked quickly.
    static void runTests() {
        System.out.println("FIXED TEST CASES");
        System.out.println("Solution A: Breadth-First Search with a Queue<String>.");
        System.out.println("Solution B: Dijkstra with a HashMap<String, Double> distance table.");
        System.out.println("Both search the same HashMap<String, ArrayList<Edge>> graph.");
        oneTest("1. Two stations on the same line", "S01", "S10");
        oneTest("2. Two lines, the passenger must change", "S01", "S32");
        oneTest("3. A long trip across the city", "S02", "S60");
        oneTest("4. The biggest difference between the solutions", "S05", "S53");
        oneTest("5. Another trip where the solutions differ", "S05", "S60");
        oneTest("6. A station code that does not exist", "S01", "S99");
    }

    static void oneTest(String title, String start, String end) {
        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println(title + "   (" + start + " to " + end + ")");

        if (!g.hasCode(start) || !g.hasCode(end)) {
            System.out.println("  Result: unknown station code, the program says so and stops.");
            return;
        }

        ArrayList<String> bfsPath = BFSPathFinder.find(g, start, end);
        DijkstraPathFinder.Result d = DijkstraPathFinder.find(g, start, end);
        if (bfsPath == null || d.path == null) {
            System.out.println("  Result: there is no route between these two stations.");
            return;
        }

        System.out.println("  Solution A (fewest stops)      : "
                + summary(bfsPath, pathDistance(bfsPath)));
        System.out.println("  Solution B (shortest distance) : "
                + summary(d.path, d.totalDistance));
        if (samePath(bfsPath, d.path)) {
            System.out.println("  Result: the two solutions found the same route.");
            return;
        }

        System.out.println("  Result: the routes are different, Solution B is about "
                + km(pathDistance(bfsPath) - d.totalDistance) + " km shorter.");
    }
}
