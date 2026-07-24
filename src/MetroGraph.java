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

      // Connect stations next to each other on the same line. We group by line
      // first because the seq numbers are global, not per line.
      private void buildEdges(ArrayList<Station> rows) {
          String[] lines = { "Line1", "Line2", "Line3", "Line4", "Line5", "Line6" };
          for (int L = 0; L < lines.length; L++) {
              // collect the stations that belong to this line
              ArrayList<Station> group = new ArrayList<Station>();
              for (int i = 0; i < rows.size(); i++) {
                  if (rows.get(i).line.equals(lines[L])) {
                      group.add(rows.get(i));
                  }
              }
              // sort them by seq using a simple selection sort
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
              // connect each station to the next one on the line, both directions
              for (int i = 0; i + 1 < group.size(); i++) {
                  Station a = group.get(i);
                  Station b = group.get(i + 1);
                  adj.get(a.code).add(new Edge(b.code));
                  adj.get(b.code).add(new Edge(a.code));
              }
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
        System.out.println("S03 " + g.stations.get("S03").name + " is joined to "
                + g.adj.get("S03").size() + " stations (it is on 3 lines).");
    }
}
