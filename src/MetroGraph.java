// MetroGraph.java
// The metro network as a graph. Each station code is one vertex, and the
// adjacency list says which stations that station is joined to.
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MetroGraph {

    // One connection from a station to a neighbour station.
    class Edge {
        String to;
        Edge(String to) {
            this.to = to;
        }
    }

    // code -> the Station information for that code
    HashMap<String, Station> stations = new HashMap<String, Station>();
    // code -> the list of stations it is joined to
    HashMap<String, ArrayList<Edge>> adj = new HashMap<String, ArrayList<Edge>>();

    // Read the cleaned CSV file and build the graph.
    public void load(String path) throws Exception {
        ArrayList<Station> rows = new ArrayList<Station>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine(); // skip the header line
        String line = br.readLine();
        while (line != null) {
            if (line.trim().length() > 0) {
                String[] p = line.split(",");
                Station s = new Station(p[0], p[1], p[2], p[3],
                        Integer.parseInt(p[4]),
                        Double.parseDouble(p[5]),
                        Double.parseDouble(p[6]));
                rows.add(s);
                // an interchange station appears on more than one line but shares
                // the same code, so we only keep one Station per code
                if (!stations.containsKey(s.code)) {
                    stations.put(s.code, s);
                    adj.put(s.code, new ArrayList<Edge>());
                }
            }
            line = br.readLine();
        }
        br.close();

        buildEdges(rows);
    }

    // Connect each station to the next one, in both directions.
    private void buildEdges(ArrayList<Station> rows) {
        for (int i = 0; i + 1 < rows.size(); i++) {
            Station a = rows.get(i);
            Station b = rows.get(i + 1);
            adj.get(a.code).add(new Edge(b.code));
            adj.get(b.code).add(new Edge(a.code));
        }
    }

    // TEMPORARY quick check that the edges are there. This is removed once the
    // real Main class is added later this week.
    public static void main(String[] args) throws Exception {
        MetroGraph g = new MetroGraph();
        g.load("data/stations.csv");
        System.out.println("Loaded " + g.stations.size() + " stations.");
        System.out.println("S25 " + g.stations.get("S25").name + " is joined to:");
        for (int i = 0; i < g.adj.get("S25").size(); i++) {
            String code = g.adj.get("S25").get(i).to;
            System.out.println("   " + code + " " + g.stations.get(code).name
                    + " (" + g.stations.get(code).line + ")");
        }
    }
}
