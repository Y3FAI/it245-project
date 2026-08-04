// Main.java
// The command line tool. Every run is one command, and then the program stops.
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

        if (isCommand(command, "stations")) {
            showStations();

        } else {
            System.out.println("I do not know this command: " + command);
            showUsage();
        }
    }

    // The commands we have so far, in one short line.
    static void showUsage() {
        System.out.println("Commands:  -stations");
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
}
