// MetroGraph.java
// Will hold the whole metro network as a graph. For now it only LOADS the
// cleaned station data into a HashMap. Building the connections (edges) between
// stations comes next (Week 2).
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class MetroGraph {

    // code -> the Station information for that code
    HashMap<String, Station> stations = new HashMap<String, Station>();

    // Read the cleaned CSV file and create a Station for each row.
    public void load(String path) throws Exception {
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
                // an interchange station appears on more than one line but shares
                // the same code, so we only keep one Station per code
                if (!stations.containsKey(s.code)) {
                    stations.put(s.code, s);
                }
            }
            line = br.readLine();
        }
        br.close();
    }

    // TEMPORARY quick check that loading works. This will be removed once the
    // real Main class is added in Week 2.
    public static void main(String[] args) throws Exception {
        MetroGraph g = new MetroGraph();
        g.load("data/stations.csv");
        System.out.println("Loaded " + g.stations.size() + " stations.");
        System.out.println("Example: S03 = " + g.stations.get("S03").name);
    }
}
