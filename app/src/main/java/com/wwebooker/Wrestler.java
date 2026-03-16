package com.wwebooker;

public class Wrestler {
    public int id;
    public String name;
    public String brand;      // "RAW", "SmackDown", "Both"
    public String alignment;  // "Face", "Heel", "Tweener"
    public String gender;     // "M", "F"
    public int popularity;
    public int ringSkill;
    public int micSkill;
    public int look;
    public String tagTeam;
    public String titleHeld;
    public int wins;
    public int losses;
    public boolean injured;
    public int injuryWeeksLeft;

    public Wrestler(int id, String name, String brand, String alignment, String gender,
                    int pop, int ring, int mic, int look,
                    String tagTeam, String titleHeld, int wins, int losses) {
        this.id = id; this.name = name; this.brand = brand;
        this.alignment = alignment; this.gender = gender;
        this.popularity = pop; this.ringSkill = ring;
        this.micSkill = mic; this.look = look;
        this.tagTeam = tagTeam; this.titleHeld = titleHeld;
        this.wins = wins; this.losses = losses;
        this.injured = false; this.injuryWeeksLeft = 0;
    }

    public float getOverall() {
        return ringSkill * 0.40f + popularity * 0.35f + micSkill * 0.15f + look * 0.10f;
    }

    public String shortInfo() {
        String champ = titleHeld.isEmpty() ? "" : " ★";
        String inj   = injured ? " [INJ]" : "";
        return String.format("%-22s %-10s %-7s Pop:%-3d Rng:%-3d %d-%d%s%s",
                (name + champ + inj).substring(0, Math.min(name.length()+champ.length()+inj.length(), 22)),
                brand, alignment, popularity, ringSkill, wins, losses, champ, inj);
    }
}
