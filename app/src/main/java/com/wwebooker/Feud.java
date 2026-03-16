package com.wwebooker;

public class Feud {
    public int id;
    public String name;
    public int wrestler1Id, wrestler2Id;
    public String brand;
    public int intensity;
    public int weeksActive;
    public boolean active;

    public Feud(int id, String name, int w1, int w2, String brand, int intensity) {
        this.id = id; this.name = name;
        this.wrestler1Id = w1; this.wrestler2Id = w2;
        this.brand = brand; this.intensity = intensity;
        this.weeksActive = 0; this.active = true;
    }

    public int matchBonus() {
        return (int)(intensity * 0.09f);
    }
}
