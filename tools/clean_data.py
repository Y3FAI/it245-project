# clean_data.py
# One-time data cleaning script (Part A of the plan).
# Reads the official JSON and writes a small stations.csv with only the fields we need.

# importing library needed 
import json
import csv

# the source data
RAW = "data/official_riyadh_metro_data.json"

# the output data
OUT = "data/stations.csv"

# load the raw records
records = json.load(open(RAW, encoding="utf-8"))

# sort by the sequence number so stations come out in line order
records.sort(key=lambda r: r["metro_station_seq"])

# write only the fields we need (English name and Arabic name are both kept)
with open(OUT, "w", newline="", encoding="utf-8") as f:
    writer = csv.writer(f)
    writer.writerow(["code", "name", "name_ar", "line", "seq", "lat", "lon"])
    for r in records:
        point = r["geo_point_2d"]
        writer.writerow([
            r["metro_station_cd"],
            r["metro_station_desc_en"].strip(),
            (r["metro_station_desc_ar"] or "").strip(),
            r["metro_line_cd"],
            r["metro_station_seq"],
            round(point["lat"], 6),
            round(point["lon"], 6),
        ])

# print success message
print("Wrote", OUT, "with", len(records), "stations")
