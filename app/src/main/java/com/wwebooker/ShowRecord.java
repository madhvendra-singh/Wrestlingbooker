package com.wwebooker;

import java.util.ArrayList;
import java.util.List;

public class ShowRecord {
    public String name, brand, venue;
    public int week;
    public List<Segment> segments = new ArrayList<>();
    public String overallGrade;
    public long viewers;
    public List<String> titleChanges = new ArrayList<>();
    public List<String> injuryReports = new ArrayList<>();

    public ShowRecord(String name, String brand, String venue, int week) {
        this.name = name; this.brand = brand;
        this.venue = venue; this.week = week;
        this.overallGrade = "?"; this.viewers = 0;
    }

    public String viewersDisplay() {
        if (viewers >= 1_000_000)
            return String.format("%.1fM", viewers / 1_000_000.0);
        return String.format("%dK", viewers / 1000);
    }
}
