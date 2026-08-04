# Riyadh Metro Route Planner

A Java program that finds a route between two Riyadh Metro stations. It finds the route in
two ways and compares them.

Solution A is Breadth-First Search. It uses a Queue and it finds the route with the fewest
stops.

Solution B is Dijkstra's algorithm. It uses a HashMap distance table and it finds the route
with the shortest distance in km.

Both of them search the same graph, which holds the 83 stations of the 6 metro lines and
the connections between them.

## Build it

```
javac -d out src/*.java
```
Run the program from the top folder of the
project, because it needs to reads `data/stations.csv`.

## Run it

Copy any line below and run it.

King Fahad District to Railway. This is the trip where the two solutions are the most
different:

```
java -cp out Main path S05 S53
```

Another trip where the answers are different:

```
java -cp out Main path S05 S60
```

A trip on one line, where both answers are the same:

```
java -cp out Main path S01 S10
```

A trip where the two routes have the same number of stops, but one of them is half the
distance:

```
java -cp out Main path S08 S54
```

A trip where the shortest distance costs ten stops more and saves almost nothing:

```
java -cp out Main path S30 S57
```

The nine test cases of the project:

```
java -cp out Main -test
```

The list of all the stations and their codes:

```
java -cp out Main -stations
```

## What it prints

```
$ java -cp out Main path S05 S53
ROUTE FROM  S05  King Fahad District
        TO  S53  Railway
Shared data structure: the graph, a HashMap<String, ArrayList<Edge>>
adjacency list of the 83 stations.

SOLUTION A - the fewest stops
Algorithm: Breadth-First Search
Data structure: Queue<String> (a FIFO queue made with LinkedList)
    1.  S05  King Fahad District
    2.  S04  Al Murooj
    ...
   18.  S53  Railway

  17 stops, 40.65 km.

SOLUTION B - the shortest distance
Algorithm: Dijkstra's algorithm
Data structure: HashMap<String, Double> (the distance table)
    1.  S05  King Fahad District
    2.  S06  King Fahad District 2
    ...
   19.  S53  Railway

  18 stops, 17.83 km.

COMPARISON
  Solution A (fewest stops)      : 17 stops, 40.65 km
  Solution B (shortest distance) : 18 stops, 17.83 km
  Solution B has 1 stop more, but it is about 22.82 km shorter.
```

One more stop saves about 23 km. This is the result we use in the report.

Between two stations on the same line, like S01 and S10, there is only one way to go, so
both solutions give the same route.

## The station codes

A station is always written as its code, like `S05`. Small letters also work. The command
`-stations` prints all 83 codes. If the code does not exist, the program says so and stops.

## The files

```
src/Station.java             one station: code, name, name in Arabic, line, seq, lat, lon
src/MetroGraph.java          the graph: the stations, their connections and the distance
                             between two stations
src/BFSPathFinder.java       Solution A, Breadth-First Search with a Queue
src/DijkstraPathFinder.java  Solution B, Dijkstra with a HashMap distance table
src/Main.java                the commands

data/official_riyadh_metro_data.json   the data as we downloaded it (94 records)
data/stations.csv                      the clean data we use (94 rows, 83 stations)
tools/clean_data.py                    makes stations.csv from the JSON file
```

To make the CSV file again:

```
python3 tools/clean_data.py
```

It writes one row for every line a station belongs to, so it says 94, and the program then
loads them as 83 stations.

## The data

The data is the official file *Metro Stations in Riyadh by Metro Line and Station Type
(2024)* from the RCRC Open Data Portal. It has 94 records, and they become 83 stations
because a station that serves more than one line has the same code on every line. In the
graph such a station is only one point, so changing from one line to another happens by
itself.

The distance between two neighbour stations is a straight line worked out from their
coordinates, not the real length of the track, so the km are close but not exact.
