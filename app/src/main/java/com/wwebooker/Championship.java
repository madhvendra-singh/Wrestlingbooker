package com.wwebooker;

import java.util.ArrayList;
import java.util.List;

public class Championship {
    public int id;
    public String name;
    public String brand;
    public boolean isTagTitle;
    public int holderId;
    public int holderBId;
    public int defenses;
    public int reignStartWeek;
    public List<TitleChange> history = new ArrayList<>();

    public Championship(int id, String name, String brand, boolean isTag,
                        int holderId, int holderBId) {
        this.id = id; this.name = name; this.brand = brand;
        this.isTagTitle = isTag;
        this.holderId = holderId; this.holderBId = holderBId;
        this.defenses = 0; this.reignStartWeek = 1;
    }

    public String getHolderName(List<Wrestler> roster) {
        String n = findName(roster, holderId);
        if (isTagTitle && holderBId != 0)
            n += " & " + findName(roster, holderBId);
        return n;
    }

    private String findName(List<Wrestler> roster, int id) {
        for (Wrestler w : roster) if (w.id == id) return w.name;
        return "VACANT";
    }

    public static class TitleChange {
        public String newChamp, prevChamp, showName;
        public int week;
        public TitleChange(String n, String p, String show, int wk) {
            newChamp = n; prevChamp = p; showName = show; week = wk;
        }
    }
}
