// Station.java
// This class stores all the information we have about one metro station.
// It is a simple data holder, no methods except the constructor.
public class Station {
    String code;    // short code like S01, used as the key in the graph
    String name;      // station name in English
    String nameAr;    // station name in Arabic
    String line;      // which metro line it belongs to (Line1, Line2, etc.)
    int seq;          // the order of the station on its line
    double lat;       // latitude, to work out distances
    double lon;       // longitude, to work out distances

    // The constructor just saves all the values when we create a new Station.
    public Station(String code, String name, String nameAr, String line, int seq, double lat, double lon) {
        this.code = code;
        this.name = name;
        this.nameAr = nameAr;
        this.line = line;
        this.seq = seq;
        this.lat = lat;
        this.lon = lon;
    }
}
