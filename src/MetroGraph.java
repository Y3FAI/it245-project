// MetroGraph.java
// This builds the graph of the Riyadh metro network.
// Each station code is one point in the graph, and we store which stations
// are next to each other and how far apart they are.
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MetroGraph {

    // This is one connection between two stations.
    // It says which station we are going to and the distance in km.
    class Edge {
        String to;          // the station code of the neighbour
        double distance;    // how far it is in km
        Edge(String to, double distance) {
            this.to = to;
            this.distance = distance;
        }
    }

    // stations holds all the station information, with the code as the key.
    HashMap<String, Station> stations = new HashMap<String, Station>();
    // adj is the adjacency list. For each station code, it stores a list of
    // the stations that are directly connected to it.
    HashMap<String, ArrayList<Edge>> adj = new HashMap<String, ArrayList<Edge>>();

    // Read the CSV file and build the whole graph.
    // The file has one row per station per line, so a station on two lines
    // appears twice, but we only store it once.
    public void load(String path) throws Exception {
        ArrayList<Station> rows = new ArrayList<Station>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine(); // skip the first line, it is just column names
        String line = br.readLine();
        while (line != null) {
            if (line.trim().length() > 0) {
                // split the line by commas and make a Station object
                String[] p = line.split(",");
                Station s = new Station(p[0], p[1], p[2], p[3],
                        Integer.parseInt(p[4]),
                        Double.parseDouble(p[5]),
                        Double.parseDouble(p[6]));
                rows.add(s);
                // if we already have this station code, do not add it again
                // because interchange stations share the same code
                if (!stations.containsKey(s.code)) {
                    stations.put(s.code, s);
                    adj.put(s.code, new ArrayList<Edge>());
                }
            }
            line = br.readLine();
        }
        br.close();

        // now connect the stations that are next to each other on the same line
        buildEdges(rows);
    }

    // Go through each line, sort the stations by their order number, and
    // connect every station to the one after it on the same line.
    // We do this for both directions so the graph is undirected.
    private void buildEdges(ArrayList<Station> rows) {
        String[] lines = { "Line1", "Line2", "Line3", "Line4", "Line5", "Line6" };
        for (int L = 0; L < lines.length; L++) {
            // first collect all stations that belong to this line
            ArrayList<Station> group = new ArrayList<Station>();
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).line.equals(lines[L])) {
                    group.add(rows.get(i));
                }
            }
            // sort them by their seq number using selection sort
            for (int i = 0; i < group.size(); i++) {
                int min = i;
                for (int j = i + 1; j < group.size(); j++) {
                    if (group.get(j).seq < group.get(min).seq) {
                        min = j;
                    }
                }
                Station temp = group.get(i);
                group.set(i, group.get(min));
                group.set(min, temp);
            }
            // connect each station to the next one on the line
            for (int i = 0; i + 1 < group.size(); i++) {
                Station a = group.get(i);
                Station b = group.get(i + 1);
                double d = distance(a, b);
                // add the connection in both directions
                adj.get(a.code).add(new Edge(b.code, d));
                adj.get(b.code).add(new Edge(a.code, d));
            }
        }

        // check if any station has no connections, which would mean the data
        // has a problem (like a wrong line name)
        for (String code : stations.keySet()) {
            if (adj.get(code).size() == 0) {
                System.out.println("Warning: station " + code + " ("
                        + stations.get(code).line + ") has no connections.");
            }
        }
    }

    // Check if a station code exists in the graph.
    public boolean hasCode(String code) {
        return stations.containsKey(code);
    }

    // Work out the straight-line distance in km between two stations.
    // We use their latitude and longitude.
    // One degree of latitude is about 111 km.
    // But a degree of longitude is shorter, so we multiply by cos(latitude).
    double distance(Station a, Station b) {
        double midLat = Math.toRadians((a.lat + b.lat) / 2.0);
        double dLat = b.lat - a.lat;
        double dLon = (b.lon - a.lon) * Math.cos(midLat);
        return Math.sqrt(dLat * dLat + dLon * dLon) * 111.0;
    }
}
