package com.wwebooker;

import java.util.List;

public class Segment {
    public static final int TYPE_MATCH = 0;
    public static final int TYPE_PROMO = 1;

    public int type;
    public List<Integer> wrestlerIds;
    public String matchType;
    public int championshipId;
    public int feudId;
    public int winnerId;
    public String grade;

    public Segment(int type, List<Integer> ids, String matchType,
                   int champId, int feudId, int winnerId) {
        this.type = type; this.wrestlerIds = ids; this.matchType = matchType;
        this.championshipId = champId; this.feudId = feudId; this.winnerId = winnerId;
        this.grade = "?";
    }

    public String getParticipantsStr(List<Wrestler> roster) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wrestlerIds.size(); i++) {
            if (i > 0) sb.append(" vs ");
            sb.append(findName(roster, wrestlerIds.get(i)));
        }
        return sb.toString();
    }

    public String getWinnerName(List<Wrestler> roster) {
        return findName(roster, winnerId);
    }

    private String findName(List<Wrestler> roster, int id) {
        for (Wrestler w : roster) if (w.id == id) return w.name;
        return "?";
    }

    public static int matchTypeBonus(String mt) {
        switch (mt) {
            case "Royal Rumble":           return 13;
            case "Elimination Chamber":    return 11;
            case "TLC":                    return 10;
            case "Hell in a Cell":         return 9;
            case "Ladder Match":           return 8;
            case "Last Man Standing":      return 8;
            case "Iron Man Match":         return 7;
            case "Steel Cage":             return 6;
            case "Extreme Rules":          return 5;
            case "Street Fight":           return 5;
            case "Falls Count Anywhere":   return 4;
            case "Submission Match":       return 4;
            case "Fatal 4-Way":            return 4;
            case "Triple Threat":          return 3;
            case "No DQ":                  return 3;
            default:                       return 0;
        }
    }

    public static int hardcoreRisk(String mt) {
        switch (mt) {
            case "Hell in a Cell": case "TLC": case "Ladder Match": return 8;
            case "Street Fight":  case "Extreme Rules":             return 6;
            default:                                                return 3;
        }
    }

    public static final String[] ALL_MATCH_TYPES = {
        "Singles Match", "Tag Team Match", "Triple Threat", "Fatal 4-Way",
        "Ladder Match", "Steel Cage", "Hell in a Cell", "TLC",
        "Last Man Standing", "Street Fight", "Submission Match",
        "Iron Man Match", "Royal Rumble", "Elimination Chamber",
        "Extreme Rules", "No DQ", "Falls Count Anywhere"
    };
}
