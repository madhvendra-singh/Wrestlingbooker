package com.wwebooker;

import java.util.*;

public class GameEngine {

    public int currentWeek = 1;
    public List<Wrestler>     roster        = new ArrayList<>();
    public List<Championship> championships = new ArrayList<>();
    public List<Feud>         feuds         = new ArrayList<>();
    public List<ShowRecord>   showHistory   = new ArrayList<>();

    private int nextFeudId = 100;
    private final Random rng = new Random();

    public static class PPVEvent {
        public int week; public String name, venue; public boolean isBig;
        public PPVEvent(int w, String n, String v, boolean b) { week=w; name=n; venue=v; isBig=b; }
    }
    public List<PPVEvent> ppvSchedule = new ArrayList<>();

    // ──────────────────────────────────────────
    public GameEngine() {
        initRoster();
        initChampionships();
        initFeuds();
        initPPV();
    }

    // ══════════════════════════════════════════
    // INIT DATA
    // ══════════════════════════════════════════
    private void initRoster() {
        // RAW MEN
        add(1,"Cody Rhodes","RAW","Face","M",96,86,92,90,"","WWE Championship",45,8);
        add(2,"CM Punk","RAW","Face","M",91,83,97,84,"","",38,12);
        add(3,"Seth Rollins","RAW","Face","M",89,93,86,88,"","",42,14);
        add(4,"Gunther","RAW","Heel","M",88,93,82,84,"","World Heavyweight Championship",52,4);
        add(5,"Sami Zayn","RAW","Face","M",85,85,88,78,"","Intercontinental Championship",36,15);
        add(6,"Jey Uso","RAW","Face","M",87,82,80,82,"","",34,16);
        add(7,"Drew McIntyre","RAW","Heel","M",83,86,84,86,"","",30,18);
        add(8,"Damian Priest","RAW","Heel","M",80,83,78,84,"Judgment Day","",28,20);
        add(9,"Dominik Mysterio","RAW","Heel","M",80,74,82,78,"Judgment Day","",24,22);
        add(10,"Finn Balor","RAW","Heel","M",78,86,78,84,"Judgment Day","",26,20);
        add(11,"Sheamus","RAW","Face","M",80,87,82,82,"","",25,20);
        add(12,"Bronson Reed","RAW","Heel","M",74,79,68,80,"","",22,18);
        add(13,"Pete Dunne","RAW","Face","M",76,92,74,76,"New Catch Republic","",28,16);
        add(14,"Tyler Bate","RAW","Face","M",72,90,72,80,"New Catch Republic","RAW Tag Team Championships",26,16);
        add(15,"Ludwig Kaiser","RAW","Heel","M",72,82,76,78,"","",20,20);
        add(16,"Montez Ford","RAW","Face","M",78,82,80,84,"Street Profits","RAW Tag Team Championships",24,18);
        add(17,"Angelo Dawkins","RAW","Face","M",74,78,76,82,"Street Profits","",24,18);
        add(18,"JD McDonagh","RAW","Heel","M",68,82,74,76,"Judgment Day","",20,22);
        add(19,"Karrion Kross","RAW","Heel","M",74,79,83,82,"","",22,20);
        // RAW WOMEN
        add(21,"Rhea Ripley","RAW","Heel","F",93,83,89,94,"","Women's World Championship",42,6);
        add(22,"Becky Lynch","RAW","Face","F",90,82,93,90,"","",38,10);
        add(23,"Liv Morgan","RAW","Heel","F",83,76,82,88,"","",30,16);
        add(24,"Nia Jax","RAW","Heel","F",78,73,74,78,"","",26,18);
        add(25,"Lyra Valkyria","RAW","Face","F",72,82,70,82,"","",22,18);
        add(26,"Raquel Rodriguez","RAW","Face","F",74,79,71,84,"","",22,20);
        add(27,"Zoey Stark","RAW","Heel","F",70,79,69,80,"","",20,20);
        add(28,"Katana Chance","Both","Face","F",68,78,70,80,"Chance/Kayden","Women's Tag Team Championships",18,18);
        add(29,"Kayden Carter","Both","Face","F",66,76,68,80,"Chance/Kayden","",18,18);
        // SMACKDOWN MEN
        add(30,"Roman Reigns","SmackDown","Face","M",95,84,92,94,"The OTC","",48,6);
        add(31,"Randy Orton","SmackDown","Face","M",90,88,84,88,"","",40,12);
        add(32,"Kevin Owens","SmackDown","Heel","M",86,87,91,76,"","",36,14);
        add(33,"AJ Styles","SmackDown","Face","M",85,93,80,82,"","",38,14);
        add(34,"LA Knight","SmackDown","Face","M",87,79,95,86,"","",32,16);
        add(35,"Solo Sikoa","SmackDown","Heel","M",84,82,76,86,"The Bloodline","United States Championship",30,10);
        add(36,"Jacob Fatu","SmackDown","Heel","M",78,84,66,82,"The Bloodline","",26,12);
        add(37,"Tama Tonga","SmackDown","Heel","M",72,76,70,78,"The Bloodline","",22,16);
        add(38,"Carmelo Hayes","SmackDown","Face","M",76,83,82,84,"","",24,18);
        add(39,"Santos Escobar","SmackDown","Heel","M",72,82,76,78,"LWO","",22,20);
        add(40,"Andrade","SmackDown","Heel","M",74,86,72,82,"","",24,18);
        add(41,"Tiffany Stratton","SmackDown","Heel","F",80,77,80,90,"","",26,18);
        add(42,"The Miz","SmackDown","Heel","M",70,68,87,78,"","",20,24);
        add(43,"Braun Strowman","SmackDown","Face","M",78,80,70,86,"","",24,18);
        add(44,"Rey Mysterio","SmackDown","Face","M",82,85,78,78,"LWO","",30,18);
        add(45,"Dragon Lee","SmackDown","Face","M",70,88,68,76,"LWO","",22,18);
        // SMACKDOWN WOMEN
        add(46,"Bianca Belair","SmackDown","Face","F",85,87,83,92,"","Women's Championship",40,10);
        add(47,"Bayley","SmackDown","Face","F",83,85,86,84,"","",36,14);
        add(48,"Naomi","SmackDown","Face","F",78,79,78,88,"","",28,18);
        add(49,"Chelsea Green","SmackDown","Heel","F",74,73,83,86,"","",22,22);
        add(50,"Michin","SmackDown","Face","F",70,81,72,82,"","",24,20);
        add(51,"Piper Niven","SmackDown","Heel","F",68,74,70,74,"","",20,20);
    }

    private void add(int id, String name, String brand, String align, String gender,
                     int pop, int ring, int mic, int look,
                     String tag, String title, int w, int l) {
        roster.add(new Wrestler(id, name, brand, align, gender, pop, ring, mic, look, tag, title, w, l));
    }

    private void initChampionships() {
        championships.add(new Championship(1, "WWE Championship", "RAW", false, 1, 0));
        championships.add(new Championship(2, "World Heavyweight Championship", "RAW", false, 4, 0));
        championships.add(new Championship(3, "Intercontinental Championship", "RAW", false, 5, 0));
        championships.add(new Championship(4, "Women's World Championship", "RAW", false, 21, 0));
        championships.add(new Championship(5, "United States Championship", "SmackDown", false, 35, 0));
        championships.add(new Championship(6, "Women's Championship", "SmackDown", false, 46, 0));
        championships.add(new Championship(7, "RAW Tag Team Championships", "RAW", true, 16, 14));
        championships.add(new Championship(8, "SmackDown Tag Team Championships", "SmackDown", true, 33, 34));
        championships.add(new Championship(9, "Women's Tag Team Championships", "Both", true, 28, 29));
    }

    private void initFeuds() {
        feuds.add(new Feud(1, "American Nightmare vs The Ring General", 1, 4, "RAW", 72));
        feuds.add(new Feud(2, "Best in the World vs The Visionary", 2, 3, "RAW", 65));
        feuds.add(new Feud(3, "Head of the Table - Civil War", 30, 35, "SmackDown", 80));
        feuds.add(new Feud(4, "The Legend Killer Returns", 31, 40, "SmackDown", 55));
        feuds.add(new Feud(5, "Mami vs The Man", 21, 22, "RAW", 70));
    }

    private void initPPV() {
        ppvSchedule.add(new PPVEvent(4,  "Royal Rumble",              "Lucas Oil Stadium, Indianapolis IN",   false));
        ppvSchedule.add(new PPVEvent(8,  "Elimination Chamber",       "Rogers Centre, Toronto Canada",         false));
        ppvSchedule.add(new PPVEvent(12, "WrestleMania 41",           "Allegiant Stadium, Las Vegas NV",       true));
        ppvSchedule.add(new PPVEvent(16, "Backlash",                  "SAP Center, San Jose CA",               false));
        ppvSchedule.add(new PPVEvent(20, "King & Queen of the Ring",  "Jeddah Superdome, Saudi Arabia",        false));
        ppvSchedule.add(new PPVEvent(24, "Money in the Bank",         "Intuit Dome, Los Angeles CA",           false));
        ppvSchedule.add(new PPVEvent(28, "SummerSlam",                "MetLife Stadium, East Rutherford NJ",   true));
        ppvSchedule.add(new PPVEvent(32, "Bash in Berlin",            "Uber Arena, Berlin Germany",            false));
        ppvSchedule.add(new PPVEvent(36, "Bad Blood",                 "State Farm Arena, Atlanta GA",          false));
        ppvSchedule.add(new PPVEvent(40, "Crown Jewel",               "Kingdom Arena, Riyadh Saudi Arabia",    false));
        ppvSchedule.add(new PPVEvent(44, "Survivor Series",           "Rogers Centre, Toronto Canada",         false));
        ppvSchedule.add(new PPVEvent(48, "Saturday Night's Main Event","Chase Center, San Francisco CA",       false));
        ppvSchedule.add(new PPVEvent(52, "WWE Day 1",                 "State Farm Arena, Atlanta GA",          false));
    }

    // ══════════════════════════════════════════
    // GRADING
    // ══════════════════════════════════════════
    public String calcMatchGrade(List<Integer> ids, String matchType,
                                  boolean hasTitle, int feudIntensity) {
        if (ids.isEmpty()) return "F";
        float totalRing=0, totalPop=0, totalLook=0;
        int faces=0, heels=0, cnt=0;
        for (int id : ids) {
            Wrestler w = findWrestler(id);
            if (w == null) continue;
            totalRing += w.ringSkill; totalPop += w.popularity; totalLook += w.look;
            if ("Face".equals(w.alignment)) faces++;
            else if ("Heel".equals(w.alignment)) heels++;
            cnt++;
        }
        if (cnt == 0) return "F";
        float score = (totalRing/cnt)*0.45f + (totalPop/cnt)*0.35f + (totalLook/cnt)*0.10f;
        score += Segment.matchTypeBonus(matchType);
        if (hasTitle) score += 7;
        if (faces > 0 && heels > 0) score += 5;
        score += feudIntensity * 0.09f;
        score += (rng.nextFloat() * 16f) - 8f;
        score = Math.max(20, Math.min(100, score));
        return scoreToGrade((int)score);
    }

    public String calcPromoGrade(int wrestlerId) {
        Wrestler w = findWrestler(wrestlerId);
        if (w == null) return "F";
        float score = w.micSkill*0.55f + w.popularity*0.30f + w.look*0.10f;
        score += (rng.nextFloat() * 12f) - 6f;
        score = Math.max(20, Math.min(100, score));
        return scoreToGrade((int)score);
    }

    public String calcShowGrade(List<Segment> segments) {
        if (segments.isEmpty()) return "F";
        int n = segments.size();
        float wSum = 0, wTotal = 0;
        for (int i = 0; i < n; i++) {
            float w = (i == n-1) ? 3f : (i == n-2) ? 2f : 1f;
            wSum += gradeToScore(segments.get(i).grade) * w;
            wTotal += w;
        }
        return scoreToGrade((int)(wSum / wTotal));
    }

    public long calcViewers(String grade, String brand) {
        long base;
        switch (grade) {
            case "A*": base = 3200000; break;
            case "A":  base = 2800000; break;
            case "B":  base = 2400000; break;
            case "C":  base = 2000000; break;
            case "D":  base = 1600000; break;
            case "E":  base = 1200000; break;
            default:   base =  900000; break;
        }
        if ("SmackDown".equals(brand)) base = (long)(base * 1.05);
        long var = (rng.nextLong() % 200000);
        return Math.max(500000, base + var);
    }

    public static String scoreToGrade(int s) {
        if (s >= 95) return "A*";
        if (s >= 84) return "A";
        if (s >= 72) return "B";
        if (s >= 61) return "C";
        if (s >= 50) return "D";
        if (s >= 38) return "E";
        return "F";
    }

    public static int gradeToScore(String g) {
        switch (g) {
            case "A*": return 97; case "A": return 88; case "B": return 78;
            case "C": return 65;  case "D": return 54; case "E": return 44;
            default:  return 30;
        }
    }

    // ══════════════════════════════════════════
    // SHOW EXECUTION
    // ══════════════════════════════════════════
    public void executeShow(ShowRecord show) {
        // Grade segments
        for (Segment seg : show.segments) {
            int feudInt = 0;
            if (seg.feudId != 0) {
                Feud f = findFeud(seg.feudId);
                if (f != null) feudInt = f.intensity;
            }
            if (seg.type == Segment.TYPE_MATCH)
                seg.grade = calcMatchGrade(seg.wrestlerIds, seg.matchType,
                                           seg.championshipId != 0, feudInt);
            else
                seg.grade = calcPromoGrade(seg.wrestlerIds.isEmpty() ? 0 : seg.wrestlerIds.get(0));
        }

        show.overallGrade = calcShowGrade(show.segments);
        show.viewers = calcViewers(show.overallGrade, show.brand);

        // Title changes
        for (Segment seg : show.segments)
            if (seg.type == Segment.TYPE_MATCH && seg.championshipId != 0)
                handleTitleChange(seg, show);

        // W/L records
        for (Segment seg : show.segments) {
            if (seg.type != Segment.TYPE_MATCH || seg.winnerId == 0) continue;
            for (int id : seg.wrestlerIds) {
                Wrestler w = findWrestler(id);
                if (w == null) continue;
                if (id == seg.winnerId) w.wins++; else w.losses++;
            }
        }

        // Popularity shifts
        int showScore = gradeToScore(show.overallGrade);
        int mainShift = showScore >= 85 ? 3 : showScore >= 70 ? 2 : showScore >= 55 ? 1 : 0;
        int regShift  = showScore >= 80 ? 1 : 0;
        int penalty   = showScore < 50 ? -1 : 0;
        int n = show.segments.size();
        for (int i = 0; i < n; i++) {
            Segment seg = show.segments.get(i);
            boolean isMain = (i == n - 1);
            int shift = isMain ? mainShift : (regShift + penalty);
            for (int id : seg.wrestlerIds) {
                Wrestler w = findWrestler(id);
                if (w == null) continue;
                int bonus = (seg.type == Segment.TYPE_MATCH && id == seg.winnerId) ? 1 : 0;
                w.popularity = Math.max(1, Math.min(100, w.popularity + shift + bonus));
            }
        }

        // Injury check
        for (Segment seg : show.segments) {
            if (seg.type != Segment.TYPE_MATCH) continue;
            int chance = Segment.hardcoreRisk(seg.matchType);
            for (int id : seg.wrestlerIds) {
                if (rng.nextInt(100) < chance) {
                    Wrestler w = findWrestler(id);
                    if (w != null && !w.injured) {
                        w.injured = true;
                        w.injuryWeeksLeft = 2 + rng.nextInt(7);
                        show.injuryReports.add(w.name + " injured! Out ~" + w.injuryWeeksLeft + " weeks.");
                    }
                }
            }
        }

        // Feud intensity boost
        for (Segment seg : show.segments)
            if (seg.feudId != 0) {
                Feud f = findFeud(seg.feudId);
                if (f != null) f.intensity = Math.min(100, f.intensity + 2 + rng.nextInt(5));
            }

        weeklyFeudsUpdate();
        showHistory.add(show);
    }

    private void handleTitleChange(Segment seg, ShowRecord show) {
        Championship champ = findChampionship(seg.championshipId);
        if (champ == null || seg.winnerId == 0) return;
        if (champ.holderId == seg.winnerId) { champ.defenses++; return; }

        Wrestler oldH = findWrestler(champ.holderId);
        Wrestler newH = findWrestler(seg.winnerId);
        champ.history.add(new Championship.TitleChange(
                newH != null ? newH.name : "?",
                oldH != null ? oldH.name : "?",
                show.name, currentWeek));
        if (oldH != null) oldH.titleHeld = "";
        champ.holderId = seg.winnerId; champ.holderBId = 0;
        champ.defenses = 0; champ.reignStartWeek = currentWeek;
        if (newH != null) newH.titleHeld = champ.name;
        show.titleChanges.add((newH != null ? newH.name : "?") + " is the NEW " + champ.name + "!");
    }

    // ══════════════════════════════════════════
    // WEEKLY UPDATE
    // ══════════════════════════════════════════
    public void advanceWeek() {
        currentWeek++;
        weeklyFeudsUpdate();
        // Injury recovery
        for (Wrestler w : roster) {
            if (!w.injured) continue;
            w.injuryWeeksLeft--;
            if (w.injuryWeeksLeft <= 0) { w.injured = false; w.injuryWeeksLeft = 0; }
        }
    }

    private void weeklyFeudsUpdate() {
        for (Feud f : feuds) {
            f.weeksActive++;
            if (f.intensity > 50 && f.weeksActive > 8)
                f.intensity = Math.max(30, f.intensity - 1 - rng.nextInt(3));
        }
    }

    // ══════════════════════════════════════════
    // TITLE MANAGEMENT
    // ══════════════════════════════════════════
    public void manualTitleChange(int champId, int newHolderId) {
        Championship champ = findChampionship(champId);
        Wrestler oldH = findWrestler(champ.holderId);
        Wrestler newH = findWrestler(newHolderId);
        if (champ == null || newH == null) return;
        champ.history.add(new Championship.TitleChange(newH.name,
                oldH != null ? oldH.name : "?", "Manual", currentWeek));
        if (oldH != null) oldH.titleHeld = "";
        champ.holderId = newHolderId; champ.holderBId = 0;
        champ.defenses = 0; champ.reignStartWeek = currentWeek;
        newH.titleHeld = champ.name;
    }

    // ══════════════════════════════════════════
    // FEUD MANAGEMENT
    // ══════════════════════════════════════════
    public void createFeud(String name, int w1, int w2, String brand) {
        feuds.add(new Feud(nextFeudId++, name, w1, w2, brand, 40));
    }

    public void escalateFeud(int feudId) {
        Feud f = findFeud(feudId);
        if (f != null) f.intensity = Math.min(100, f.intensity + 10 + rng.nextInt(8));
    }

    public void endFeud(int feudId) {
        feuds.removeIf(f -> f.id == feudId);
    }

    // ══════════════════════════════════════════
    // UTILITIES
    // ══════════════════════════════════════════
    public Wrestler findWrestler(int id) {
        for (Wrestler w : roster) if (w.id == id) return w;
        return null;
    }
    public Championship findChampionship(int id) {
        for (Championship c : championships) if (c.id == id) return c;
        return null;
    }
    public Feud findFeud(int id) {
        for (Feud f : feuds) if (f.id == id) return f;
        return null;
    }

    public PPVEvent getNextPPV() {
        for (PPVEvent p : ppvSchedule) if (p.week >= currentWeek) return p;
        return null;
    }

    public boolean isPPVWeek() {
        for (PPVEvent p : ppvSchedule) if (p.week == currentWeek) return true;
        return false;
    }

    public List<Wrestler> getAvailableWrestlers(String brand) {
        List<Wrestler> result = new ArrayList<>();
        for (Wrestler w : roster)
            if ("PPV".equals(brand) || brand.equals(w.brand) || "Both".equals(w.brand))
                result.add(w);
        result.sort((a, b) -> b.popularity - a.popularity);
        return result;
    }

    public List<Championship> getAvailableTitles(String brand) {
        List<Championship> result = new ArrayList<>();
        for (Championship c : championships)
            if ("PPV".equals(brand) || brand.equals(c.brand) || "Both".equals(c.brand))
                result.add(c);
        return result;
    }

    public List<Feud> getActiveFeuds(String brand) {
        List<Feud> result = new ArrayList<>();
        for (Feud f : feuds)
            if (f.active && ("PPV".equals(brand) || brand.equals(f.brand) || "Both".equals(f.brand)))
                result.add(f);
        return result;
    }

    public String getWeekDate() {
        String[] months = {"January","February","March","April","May","June",
                "July","August","September","October","November","December"};
        int[] dpm = {31,28,31,30,31,30,31,31,30,31,30,31};
        int day = 6 + (currentWeek - 1) * 7;
        int month = 0, year = 2025;
        while (day > dpm[month]) {
            day -= dpm[month];
            month++;
            if (month >= 12) { month = 0; year++; }
        }
        return months[month] + " " + (day+1) + ", " + year;
    }

    public List<Wrestler> getTopStars(int n) {
        List<Wrestler> sorted = new ArrayList<>(roster);
        sorted.sort((a, b) -> b.popularity - a.popularity);
        return sorted.subList(0, Math.min(n, sorted.size()));
    }
}
