// Station.java
// Holds the information for one metro station (a plain data holder).
public class Station {
    String code;
    String name;      // English name
    String nameAr;    // Arabic name
    String line;
    int seq;
    double lat;
    double lon;

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
