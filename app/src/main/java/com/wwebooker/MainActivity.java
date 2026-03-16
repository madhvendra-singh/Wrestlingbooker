package com.wwebooker;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    // ── UI refs ──────────────────────────────────────────────────────────
    private TextView   tvOutput;
    private TextView   tvWeek;
    private ScrollView scrollView;
    private LinearLayout btnGrid;

    // ── Game ─────────────────────────────────────────────────────────────
    private final GameEngine engine = new GameEngine();

    // ── Booking state ────────────────────────────────────────────────────
    private ShowRecord   pendingShow;
    private List<Integer> pendingIds = new ArrayList<>();
    private String       pendingMatchType;
    private int          pendingChampId;
    private int          pendingFeudId;

    // ── Colors (int) ─────────────────────────────────────────────────────
    private static final int C_BG      = 0xFF07070D;
    private static final int C_SURFACE = 0xFF0F0F1A;
    private static final int C_SURF2   = 0xFF181828;
    private static final int C_BORDER  = 0xFF252540;
    private static final int C_RED     = 0xFFD4001A;
    private static final int C_BRED    = 0xFFFF3348;
    private static final int C_GOLD    = 0xFFFFD700;
    private static final int C_TEXT    = 0xFFE8E8F0;
    private static final int C_DIM     = 0xFF7A7A99;
    private static final int C_GREEN   = 0xFF00E676;
    private static final int C_BLUE    = 0xFF5599FF;
    private static final int C_CYAN    = 0xFF00BCD4;
    private static final int C_ORANGE  = 0xFFFFAB40;

    // ═════════════════════════════════════════════════════════════════════
    // LIFECYCLE
    // ═════════════════════════════════════════════════════════════════════

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOutput   = findViewById(R.id.tvOutput);
        tvWeek     = findViewById(R.id.tvWeek);
        scrollView = findViewById(R.id.scrollView);
        btnGrid    = findViewById(R.id.btnGrid);

        showSplash();
    }

    // ═════════════════════════════════════════════════════════════════════
    // SPLASH → MAIN MENU
    // ═════════════════════════════════════════════════════════════════════

    private void showSplash() {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendLine(sb, "██╗    ██╗██╗    ██╗███████╗", C_RED, true, 1.5f);
        appendLine(sb, "██║    ██║██║    ██║██╔════╝", C_RED, true, 1.5f);
        appendLine(sb, "██║ █╗ ██║██║ █╗ ██║█████╗  ", C_RED, true, 1.5f);
        appendLine(sb, "██║███╗██║██║███╗██║██╔══╝  ", C_RED, true, 1.5f);
        appendLine(sb, "╚███╔███╔╝╚███╔███╔╝███████╗", C_RED, true, 1.5f);
        appendLine(sb, " ╚══╝╚══╝  ╚══╝╚══╝ ╚══════╝", C_RED, true, 1.5f);
        sb.append("\n");
        appendLine(sb, "  B O O K E R   S I M U L A T I O N", C_GOLD, true, 1.1f);
        appendLine(sb, "  Version 1.0  ·  Android Edition", C_DIM, false, 0.9f);
        sb.append("\n");
        appendLine(sb, "  51 Superstars  ·  9 Championships  ·  Full WWE Roster", C_DIM, false, 0.85f);
        tvOutput.setText(sb);
        clearButtons();
        addBtn("▶  START GAME", C_GOLD, C_BG, v -> showMainMenu());
    }

    // ═════════════════════════════════════════════════════════════════════
    // MAIN MENU
    // ═════════════════════════════════════════════════════════════════════

    private void showMainMenu() {
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendSectionHead(sb, "MAIN MENU");
        GameEngine.PPVEvent next = engine.getNextPPV();
        if (next != null) {
            int away = next.week - engine.currentWeek;
            if (away == 0)
                appendLine(sb, "  ★ PPV THIS WEEK: " + next.name, C_GOLD, true, 1.0f);
            else
                appendLine(sb, "  Next: " + next.name + " in " + away + " week(s)", C_DIM, false, 0.85f);
        }
        appendLine(sb, "  " + engine.showHistory.size() + " shows booked this season", C_DIM, false, 0.85f);
        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();
        addBtn("📊  Dashboard",          C_SURF2, C_TEXT,  v -> showDashboard());
        addBtn("💪  Roster",             C_SURF2, C_TEXT,  v -> showRoster("ALL"));
        addBtn("📋  Book Show",          C_RED,   C_TEXT,  v -> showBookMenu());
        addBtn("🏆  Championships",      C_SURF2, C_TEXT,  v -> showChampionships());
        addBtn("⚔️  Feuds & Storylines", C_SURF2, C_TEXT,  v -> showFeuds());
        addBtn("📜  Show History",       C_SURF2, C_TEXT,  v -> showHistory());
        addBtn("⏭  Advance Week",        C_SURF2, C_GOLD,  v -> doAdvanceWeek());
    }

    // ═════════════════════════════════════════════════════════════════════
    // DASHBOARD
    // ═════════════════════════════════════════════════════════════════════

    private void showDashboard() {
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, "DASHBOARD", "Week " + engine.currentWeek + "  ·  " + engine.getWeekDate());

        // PPV countdown
        GameEngine.PPVEvent next = engine.getNextPPV();
        if (next != null) {
            int away = next.week - engine.currentWeek;
            appendSectionHead(sb, away == 0 ? "★ PPV THIS WEEK" : "NEXT PPV");
            if (away == 0) {
                appendLine(sb, "  " + next.name, C_GOLD, true, 1.1f);
                appendLine(sb, "  " + next.venue, C_DIM, false, 0.85f);
            } else {
                appendLine(sb, "  " + next.name + " — " + away + " week(s) away", C_GOLD, false, 1.0f);
                appendLine(sb, "  " + next.venue, C_DIM, false, 0.85f);
            }
        }

        // Top stars
        appendSectionHead(sb, "TOP 5 SUPERSTARS");
        List<Wrestler> top = engine.getTopStars(5);
        for (int i = 0; i < top.size(); i++) {
            Wrestler w = top.get(i);
            boolean hasChamp = !w.titleHeld.isEmpty();
            String line = "  " + (i+1) + ". " + w.name + (hasChamp ? " ★" : "") +
                    "  [" + w.brand + "]  " + w.alignment + "  Pop:" + w.popularity;
            appendLine(sb, line, hasChamp ? C_GOLD : C_TEXT, hasChamp, 0.95f);
        }

        // Champions
        appendSectionHead(sb, "CURRENT CHAMPIONS");
        for (Championship c : engine.championships) {
            String h = c.getHolderName(engine.roster);
            appendLine(sb, "  ★ " + c.name, C_GOLD, false, 0.85f);
            appendLine(sb, "    " + h + " (" + c.defenses + " def.)", C_TEXT, true, 0.95f);
        }

        // Recent results
        if (!engine.showHistory.isEmpty()) {
            appendSectionHead(sb, "RECENT RESULTS");
            int start = Math.max(0, engine.showHistory.size() - 4);
            for (int i = engine.showHistory.size()-1; i >= start; i--) {
                ShowRecord sh = engine.showHistory.get(i);
                int gc = gradeColor(sh.overallGrade);
                appendLine(sb, "  [" + sh.overallGrade + "] " + sh.name +
                        "  Wk" + sh.week + "  " + sh.viewersDisplay() + " viewers", gc, true, 0.9f);
                for (String tc : sh.titleChanges)
                    appendLine(sb, "       ★ " + tc, C_GOLD, false, 0.8f);
            }
        }

        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();
        addBtn("←  Back", C_SURF2, C_DIM, v -> showMainMenu());
    }

    // ═════════════════════════════════════════════════════════════════════
    // ROSTER
    // ═════════════════════════════════════════════════════════════════════

    private void showRoster(String filter) {
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, "ROSTER", engine.roster.size() + " Superstars");

        List<Wrestler> list = new ArrayList<>();
        for (Wrestler w : engine.roster) {
            if ("ALL".equals(filter)) list.add(w);
            else if ("RAW".equals(filter) && "RAW".equals(w.brand)) list.add(w);
            else if ("SD".equals(filter) && "SmackDown".equals(w.brand)) list.add(w);
            else if ("CHAMPS".equals(filter) && !w.titleHeld.isEmpty()) list.add(w);
        }
        list.sort((a, b) -> b.popularity - a.popularity);

        appendLine(sb, "  ── " + filter + " (" + list.size() + " superstars) ──", C_DIM, false, 0.85f);
        sb.append("\n");
        for (int i = 0; i < list.size(); i++) {
            Wrestler w = list.get(i);
            boolean hasChamp = !w.titleHeld.isEmpty();
            int nameColor = "RAW".equals(w.brand) ? C_BRED : C_BLUE;
            String champStr = hasChamp ? " ★" : "";
            String injStr   = w.injured ? " [INJ]" : "";
            int rowColor    = hasChamp ? C_GOLD : (w.injured ? C_RED : C_TEXT);
            String alCol    = "Face".equals(w.alignment) ? "Face" : "Heel";
            appendLine(sb,
                "  " + (i+1) + ". " + w.name + champStr + injStr +
                "  Pop:" + w.popularity + "  Rng:" + w.ringSkill +
                "  Mic:" + w.micSkill + "  " + w.wins + "-" + w.losses,
                rowColor, hasChamp, 0.9f);
            appendLine(sb,
                "      [" + w.brand + "]  " + alCol +
                (w.tagTeam.isEmpty() ? "" : "  " + w.tagTeam) +
                (hasChamp ? "  🏆 " + w.titleHeld : ""),
                nameColor, false, 0.78f);
        }

        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();
        addBtnRow(
            new String[]{"All","RAW","SmackDown","Champs"},
            new int[]{C_SURF2, C_RED, C_BLUE, C_GOLD},
            new View.OnClickListener[]{
                v -> showRoster("ALL"),
                v -> showRoster("RAW"),
                v -> showRoster("SD"),
                v -> showRoster("CHAMPS")
            }
        );
        addBtn("←  Back", C_SURF2, C_DIM, v -> showMainMenu());
    }

    // ═════════════════════════════════════════════════════════════════════
    // BOOK SHOW MENU
    // ═════════════════════════════════════════════════════════════════════

    private void showBookMenu() {
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, "BOOK A SHOW", "Week " + engine.currentWeek);
        appendLine(sb, "  Choose which show to book:", C_DIM, false, 0.9f);
        sb.append("\n");
        appendLine(sb, "  📺  Monday Night RAW", C_RED, true, 1.0f);
        appendLine(sb, "      USA Network  ·  Monday", C_DIM, false, 0.8f);
        sb.append("\n");
        appendLine(sb, "  📺  Friday Night SmackDown", C_BLUE, true, 1.0f);
        appendLine(sb, "      Fox  ·  Friday", C_DIM, false, 0.8f);

        GameEngine.PPVEvent ppv = null;
        for (GameEngine.PPVEvent p : engine.ppvSchedule)
            if (p.week == engine.currentWeek) { ppv = p; break; }
        if (ppv != null) {
            sb.append("\n");
            appendLine(sb, "  ★  " + ppv.name + " (PPV)", C_GOLD, true, 1.0f);
            appendLine(sb, "     " + ppv.venue, C_DIM, false, 0.8f);
        }

        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();

        final GameEngine.PPVEvent finalPpv = ppv;
        addBtn("🔴  Book Monday Night RAW", C_RED,  C_TEXT, v -> startBooking("RAW", "Monday Night RAW",
                rawVenue()));
        addBtn("🔵  Book Friday Night SmackDown", C_BLUE, C_TEXT, v -> startBooking("SmackDown", "Friday Night SmackDown",
                sdVenue()));
        if (ppv != null)
            addBtn("★  Book " + ppv.name, C_GOLD, C_BG, v ->
                    startBooking("PPV", finalPpv.name, finalPpv.venue));
        addBtn("←  Back", C_SURF2, C_DIM, v -> showMainMenu());
    }

    private String rawVenue() {
        String[] v={"T-Mobile Arena, Las Vegas NV","Madison Square Garden, New York NY",
                "Barclays Center, Brooklyn NY","United Center, Chicago IL","TD Garden, Boston MA"};
        return v[engine.currentWeek % 5];
    }
    private String sdVenue() {
        String[] v={"Chase Center, San Francisco CA","Gainbridge Fieldhouse, Indianapolis IN",
                "Capital One Arena, Washington D.C.","Fiserv Forum, Milwaukee WI","Ball Arena, Denver CO"};
        return v[engine.currentWeek % 5];
    }

    // ═════════════════════════════════════════════════════════════════════
    // BOOKING — BUILD CARD
    // ═════════════════════════════════════════════════════════════════════

    private void startBooking(String brand, String name, String venue) {
        pendingShow = new ShowRecord(name, brand, venue, engine.currentWeek);
        showCard();
    }

    private void showCard() {
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, "BOOKING: " + pendingShow.name,
                pendingShow.venue + "  ·  Wk" + pendingShow.week);

        if (pendingShow.segments.isEmpty()) {
            appendLine(sb, "  [ No segments yet — add matches and promos ]", C_DIM, false, 0.9f);
        } else {
            appendLine(sb, "  Card (" + pendingShow.segments.size() + " segments):", C_DIM, false, 0.85f);
            int n = pendingShow.segments.size();
            for (int i = 0; i < n; i++) {
                Segment seg = pendingShow.segments.get(i);
                boolean isMain = (i == n - 1);
                String prefix = isMain ? "  ★ MAIN: " : "  " + (i+1) + ". ";
                int col = isMain ? C_GOLD : C_TEXT;
                if (seg.type == Segment.TYPE_MATCH) {
                    String parts = seg.getParticipantsStr(engine.roster);
                    String titleTag = seg.championshipId != 0 ? " 🏆" : "";
                    appendLine(sb, prefix + parts + titleTag, col, isMain, 0.95f);
                    appendLine(sb, "     " + seg.matchType, C_DIM, false, 0.78f);
                } else {
                    String part = seg.getParticipantsStr(engine.roster);
                    appendLine(sb, prefix + part + " — Promo", col, isMain, 0.95f);
                }
            }
        }

        tvOutput.setText(sb);
        scrollToBottom();
        clearButtons();
        addBtn("➕  Add Match",   C_RED,    C_TEXT, v -> pickMatchType());
        addBtn("🎤  Add Promo",   C_SURF2,  C_TEXT, v -> pickPromoWrestler());
        if (pendingShow.segments.size() >= 2)
            addBtn("🔔  RUN SHOW!", C_GOLD, C_BG, v -> runShow());
        if (!pendingShow.segments.isEmpty())
            addBtn("↩  Remove Last Segment", C_SURF2, C_BRED, v -> {
                if (!pendingShow.segments.isEmpty())
                    pendingShow.segments.remove(pendingShow.segments.size()-1);
                showCard();
            });
        addBtn("✕  Cancel Show", C_SURF2, C_DIM, v -> showMainMenu());
    }

    // ─── Match Type Picker ────────────────────────────────────────────
    private void pickMatchType() {
        pendingIds.clear();
        pendingChampId = 0;
        pendingFeudId  = 0;
        pendingMatchType = "Singles Match";

        String[] types = Segment.ALL_MATCH_TYPES;
        new AlertDialog.Builder(this)
            .setTitle("Select Match Type")
            .setItems(types, (d, which) -> {
                pendingMatchType = types[which];
                pickParticipant(1);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    // ─── Participant Picker ───────────────────────────────────────────
    private void pickParticipant(final int slotNum) {
        List<Wrestler> avail = engine.getAvailableWrestlers(pendingShow.brand);

        // Max participants by match type
        int maxParts = maxParticipants(pendingMatchType);
        int minParts = minParticipants(pendingMatchType);

        String[] names = new String[avail.size()];
        for (int i = 0; i < avail.size(); i++) {
            Wrestler w = avail.get(i);
            names[i] = w.name + " [" + w.brand + "]  " + w.alignment +
                    "  Pop:" + w.popularity + (w.titleHeld.isEmpty() ? "" : " ★");
        }

        AlertDialog.Builder b = new AlertDialog.Builder(this)
            .setTitle("Participant " + slotNum + (slotNum > minParts ? " (or Done)" : " *"))
            .setItems(names, (d, which) -> {
                int wid = avail.get(which).id;
                boolean dup = false;
                for (int id : pendingIds) if (id == wid) { dup = true; break; }
                if (!dup) pendingIds.add(wid);

                if (pendingIds.size() < maxParts && pendingIds.size() >= minParts) {
                    // offer another or proceed
                    new AlertDialog.Builder(this)
                        .setTitle("Add Another Participant?")
                        .setMessage("Current: " + pendingIds.size() + "/" + maxParts)
                        .setPositiveButton("Add More", (d2, w2) -> pickParticipant(slotNum + 1))
                        .setNegativeButton("Done Selecting", (d2, w2) -> pickTitleOrFeud())
                        .show();
                } else if (pendingIds.size() < minParts) {
                    pickParticipant(slotNum + 1);
                } else {
                    pickTitleOrFeud();
                }
            });
        if (pendingIds.size() >= minParts)
            b.setNegativeButton("Done", (d, w) -> pickTitleOrFeud());
        b.show();
    }

    private void pickTitleOrFeud() {
        List<Championship> titles = engine.getAvailableTitles(pendingShow.brand);
        if (titles.isEmpty()) { pickFeud(); return; }

        String[] opts = new String[titles.size() + 1];
        opts[0] = "No Title";
        for (int i = 0; i < titles.size(); i++)
            opts[i+1] = "🏆 " + titles.get(i).name + " (" + titles.get(i).getHolderName(engine.roster) + ")";

        new AlertDialog.Builder(this)
            .setTitle("Title on the Line?")
            .setItems(opts, (d, which) -> {
                pendingChampId = (which == 0) ? 0 : titles.get(which-1).id;
                pickFeud();
            })
            .show();
    }

    private void pickFeud() {
        List<Feud> active = engine.getActiveFeuds(pendingShow.brand);
        if (active.isEmpty()) { pickWinner(); return; }

        String[] opts = new String[active.size() + 1];
        opts[0] = "No Feud";
        for (int i = 0; i < active.size(); i++) {
            Feud f = active.get(i);
            Wrestler w1 = engine.findWrestler(f.wrestler1Id);
            Wrestler w2 = engine.findWrestler(f.wrestler2Id);
            opts[i+1] = f.name + " (" + (w1!=null?w1.name:"?") + " vs " +
                    (w2!=null?w2.name:"?") + ")  +" + f.matchBonus() + " pts";
        }

        new AlertDialog.Builder(this)
            .setTitle("Part of a Feud?")
            .setItems(opts, (d, which) -> {
                pendingFeudId = (which == 0) ? 0 : active.get(which-1).id;
                pickWinner();
            })
            .show();
    }

    private void pickWinner() {
        String[] names = new String[pendingIds.size()];
        for (int i = 0; i < pendingIds.size(); i++) {
            Wrestler w = engine.findWrestler(pendingIds.get(i));
            names[i] = w != null ? w.name : "?";
        }
        new AlertDialog.Builder(this)
            .setTitle("Who Wins?")
            .setItems(names, (d, which) -> {
                List<Integer> ids = new ArrayList<>(pendingIds);
                Segment seg = new Segment(Segment.TYPE_MATCH, ids,
                        pendingMatchType, pendingChampId, pendingFeudId,
                        pendingIds.get(which));
                pendingShow.segments.add(seg);
                showCard();
            })
            .show();
    }

    // ─── Promo Picker ─────────────────────────────────────────────────
    private void pickPromoWrestler() {
        List<Wrestler> avail = engine.getAvailableWrestlers(pendingShow.brand);
        String[] names = new String[avail.size()];
        for (int i = 0; i < avail.size(); i++) {
            Wrestler w = avail.get(i);
            names[i] = w.name + "  Mic:" + w.micSkill + "  Pop:" + w.popularity;
        }
        new AlertDialog.Builder(this)
            .setTitle("Choose Superstar for Promo")
            .setItems(names, (d, which) -> {
                List<Integer> ids = new ArrayList<>();
                ids.add(avail.get(which).id);
                Segment seg = new Segment(Segment.TYPE_PROMO, ids, "", 0, 0, 0);
                pendingShow.segments.add(seg);
                showCard();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    // ═════════════════════════════════════════════════════════════════════
    // RUN SHOW → RESULTS
    // ═════════════════════════════════════════════════════════════════════

    private void runShow() {
        engine.executeShow(pendingShow);
        showResults(pendingShow);
    }

    private void showResults(ShowRecord show) {
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, show.name, "Week " + show.week + "  ·  " + show.venue);

        // Overall rating
        int gc = gradeColor(show.overallGrade);
        appendLine(sb, "  ┌─────────────────────────────────┐", C_DIM, false, 0.85f);
        appendLine(sb, "  │  SHOW RATING: " + show.overallGrade +
                "   " + show.viewersDisplay() + " viewers  │", gc, true, 1.1f);
        appendLine(sb, "  └─────────────────────────────────┘", C_DIM, false, 0.85f);
        sb.append("\n");

        // Title changes
        for (String tc : show.titleChanges) {
            appendLine(sb, "  ★ TITLE CHANGE: " + tc, C_GOLD, true, 1.0f);
        }
        if (!show.titleChanges.isEmpty()) sb.append("\n");

        // Injury reports
        for (String inj : show.injuryReports)
            appendLine(sb, "  ⚠ INJURY: " + inj, C_BRED, false, 0.9f);
        if (!show.injuryReports.isEmpty()) sb.append("\n");

        // Segment results
        appendSectionHead(sb, "MATCH RESULTS");
        int n = show.segments.size();
        for (int i = 0; i < n; i++) {
            Segment seg = show.segments.get(i);
            boolean isMain = (i == n-1);
            if (isMain) appendLine(sb, "  ★ MAIN EVENT", C_GOLD, true, 0.9f);
            int grc = gradeColor(seg.grade);
            String parts = seg.getParticipantsStr(engine.roster);
            if (seg.type == Segment.TYPE_MATCH) {
                appendLine(sb, "  [" + seg.grade + "]  " + parts, grc, true, 1.0f);
                String titleName = "";
                if (seg.championshipId != 0) {
                    Championship c = engine.findChampionship(seg.championshipId);
                    if (c != null) titleName = "  🏆 " + c.name;
                }
                appendLine(sb, "       " + seg.matchType + titleName, C_DIM, false, 0.82f);
                String winner = seg.getWinnerName(engine.roster);
                if (!winner.isEmpty())
                    appendLine(sb, "       → " + winner + " wins", C_GREEN, false, 0.88f);
            } else {
                appendLine(sb, "  [" + seg.grade + "]  " + parts + " — Promo", grc, true, 1.0f);
            }
            if (i < n-1) appendLine(sb, "  · · · · · · · · · · · · · · · · · · ·", C_BORDER, false, 0.75f);
        }

        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();
        addBtn("⏭  Next Week  (Wk " + (engine.currentWeek + 1) + ")", C_GOLD, C_BG, v -> {
            engine.advanceWeek();
            showMainMenu();
        });
        addBtn("📊  Back to Dashboard", C_SURF2, C_TEXT, v -> showDashboard());
        addBtn("←  Main Menu", C_SURF2, C_DIM, v -> showMainMenu());
    }

    // ═════════════════════════════════════════════════════════════════════
    // CHAMPIONSHIPS
    // ═════════════════════════════════════════════════════════════════════

    private void showChampionships() {
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, "CHAMPIONSHIPS", "Current Title Holders");

        for (int i = 0; i < engine.championships.size(); i++) {
            Championship c = engine.championships.get(i);
            String holder = c.getHolderName(engine.roster);
            int brandCol = "RAW".equals(c.brand) ? C_BRED : "SmackDown".equals(c.brand) ? C_BLUE : C_CYAN;
            appendLine(sb, "  " + (i+1) + ". " + c.name, C_GOLD, true, 0.95f);
            appendLine(sb, "       " + holder, C_TEXT, false, 1.0f);
            appendLine(sb, "       [" + c.brand + "]  " + c.defenses + " defense(s)  Since Wk" + c.reignStartWeek,
                    brandCol, false, 0.78f);
            if (!c.history.isEmpty()) {
                Championship.TitleChange last = c.history.get(c.history.size()-1);
                appendLine(sb, "       Last change: " + last.newChamp + " def. " + last.prevChamp +
                        " (" + last.showName + " Wk" + last.week + ")", C_DIM, false, 0.75f);
            }
            sb.append("\n");
        }

        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();
        addBtn("✏  Change Champion", C_GOLD, C_BG, v -> pickChampionshipToChange());
        addBtn("📜  View Title History", C_SURF2, C_TEXT, v -> pickChampionshipHistory());
        addBtn("←  Back", C_SURF2, C_DIM, v -> showMainMenu());
    }

    private void pickChampionshipToChange() {
        String[] names = new String[engine.championships.size()];
        for (int i = 0; i < engine.championships.size(); i++)
            names[i] = engine.championships.get(i).name +
                    " (" + engine.championships.get(i).getHolderName(engine.roster) + ")";
        new AlertDialog.Builder(this)
            .setTitle("Select Championship")
            .setItems(names, (d, which) -> {
                Championship champ = engine.championships.get(which);
                List<Wrestler> elig = new ArrayList<>();
                for (Wrestler w : engine.roster)
                    if ("Both".equals(champ.brand) || champ.brand.equals(w.brand) || "Both".equals(w.brand))
                        elig.add(w);
                elig.sort((a, b) -> b.popularity - a.popularity);
                String[] wNames = new String[elig.size()];
                for (int i = 0; i < elig.size(); i++)
                    wNames[i] = elig.get(i).name + " [" + elig.get(i).brand + "]  Pop:" + elig.get(i).popularity;
                new AlertDialog.Builder(this)
                    .setTitle("New " + champ.name)
                    .setItems(wNames, (d2, w2) -> {
                        engine.manualTitleChange(champ.id, elig.get(w2).id);
                        toast("★ " + elig.get(w2).name + " is the new " + champ.name + "!");
                        showChampionships();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void pickChampionshipHistory() {
        String[] names = new String[engine.championships.size()];
        for (int i = 0; i < engine.championships.size(); i++)
            names[i] = engine.championships.get(i).name;
        new AlertDialog.Builder(this)
            .setTitle("Select Championship")
            .setItems(names, (d, which) -> showTitleHistory(engine.championships.get(which)))
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showTitleHistory(Championship c) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, c.name, "Title History");
        if (c.history.isEmpty()) {
            appendLine(sb, "  No title changes recorded yet.", C_DIM, false, 0.9f);
        } else {
            for (int i = c.history.size()-1; i >= 0; i--) {
                Championship.TitleChange tc = c.history.get(i);
                appendLine(sb, "  ★ " + tc.newChamp, C_GOLD, true, 1.0f);
                appendLine(sb, "       def. " + tc.prevChamp, C_DIM, false, 0.85f);
                appendLine(sb, "       " + tc.showName + "  Wk" + tc.week, C_DIM, false, 0.8f);
                sb.append("\n");
            }
        }
        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();
        addBtn("←  Back", C_SURF2, C_DIM, v -> showChampionships());
    }

    // ═════════════════════════════════════════════════════════════════════
    // FEUDS
    // ═════════════════════════════════════════════════════════════════════

    private void showFeuds() {
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, "FEUDS & STORYLINES", engine.feuds.size() + " active rivalries");

        if (engine.feuds.isEmpty()) {
            appendLine(sb, "  No active feuds. Create one to boost match ratings!", C_DIM, false, 0.9f);
        } else {
            for (int i = 0; i < engine.feuds.size(); i++) {
                Feud f = engine.feuds.get(i);
                Wrestler w1 = engine.findWrestler(f.wrestler1Id);
                Wrestler w2 = engine.findWrestler(f.wrestler2Id);
                int intCol = f.intensity >= 75 ? C_BRED : f.intensity >= 50 ? C_ORANGE : C_CYAN;
                appendLine(sb, "  " + (i+1) + ". " + f.name, C_GOLD, true, 0.95f);
                appendLine(sb,
                    "       " + (w1!=null?w1.name:"?") + " vs " + (w2!=null?w2.name:"?"),
                    C_TEXT, false, 1.0f);
                appendLine(sb,
                    "       [" + f.brand + "]  Intensity: " + f.intensity +
                    "/100  +" + f.matchBonus() + " match pts  " + f.weeksActive + " wks",
                    intCol, false, 0.78f);
                appendLine(sb, "       " + intensityBar(f.intensity), intCol, false, 0.75f);
                sb.append("\n");
            }
        }

        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();
        addBtn("➕  Create New Feud", C_RED, C_TEXT, v -> showCreateFeud());
        if (!engine.feuds.isEmpty()) {
            addBtn("⚡  Escalate Feud", C_SURF2, C_ORANGE, v -> pickFeudAction("escalate"));
            addBtn("✕  End Feud",       C_SURF2, C_BRED,   v -> pickFeudAction("end"));
        }
        addBtn("←  Back", C_SURF2, C_DIM, v -> showMainMenu());
    }

    private void pickFeudAction(String action) {
        String[] names = new String[engine.feuds.size()];
        for (int i = 0; i < engine.feuds.size(); i++) {
            Feud f = engine.feuds.get(i);
            names[i] = f.name + " (Intensity: " + f.intensity + ")";
        }
        new AlertDialog.Builder(this)
            .setTitle("escalate".equals(action) ? "Escalate Feud" : "End Feud")
            .setItems(names, (d, which) -> {
                if ("escalate".equals(action)) {
                    engine.escalateFeud(engine.feuds.get(which).id);
                    toast("Feud escalated! Intensity: " + engine.feuds.get(which).intensity);
                    showFeuds();
                } else {
                    new AlertDialog.Builder(this)
                        .setTitle("End Feud?")
                        .setMessage("End \"" + engine.feuds.get(which).name + "\"?")
                        .setPositiveButton("Yes", (d2, w2) -> {
                            engine.endFeud(engine.feuds.get(which).id);
                            toast("Feud ended.");
                            showFeuds();
                        })
                        .setNegativeButton("No", null)
                        .show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showCreateFeud() {
        // Step 1: name
        final android.widget.EditText et = new android.widget.EditText(this);
        et.setHint("Feud name / storyline title");
        et.setTextColor(C_TEXT);
        et.setHintTextColor(C_DIM);
        et.setBackgroundColor(C_SURF2);
        et.setPadding(24, 16, 24, 16);

        new AlertDialog.Builder(this)
            .setTitle("New Feud — Name")
            .setView(et)
            .setPositiveButton("Next", (d, w) -> {
                String feudName = et.getText().toString().trim();
                if (feudName.isEmpty()) { toast("Enter a name!"); return; }
                pickFeudWrestler1(feudName);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void pickFeudWrestler1(String feudName) {
        List<Wrestler> all = new ArrayList<>(engine.roster);
        all.sort((a, b) -> b.popularity - a.popularity);
        String[] names = new String[all.size()];
        for (int i = 0; i < all.size(); i++)
            names[i] = all.get(i).name + " [" + all.get(i).brand + "]";
        new AlertDialog.Builder(this)
            .setTitle("Superstar 1")
            .setItems(names, (d, which) -> pickFeudWrestler2(feudName, all.get(which).id))
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void pickFeudWrestler2(String feudName, int w1id) {
        List<Wrestler> all = new ArrayList<>(engine.roster);
        all.sort((a, b) -> b.popularity - a.popularity);
        String[] names = new String[all.size()];
        for (int i = 0; i < all.size(); i++)
            names[i] = all.get(i).name + " [" + all.get(i).brand + "]";
        new AlertDialog.Builder(this)
            .setTitle("Superstar 2")
            .setItems(names, (d, which) -> {
                int w2id = all.get(which).id;
                if (w2id == w1id) { toast("Select a different superstar!"); return; }
                Wrestler w1 = engine.findWrestler(w1id);
                Wrestler w2 = engine.findWrestler(w2id);
                String brand = "Both";
                if (w1 != null && w2 != null && w1.brand.equals(w2.brand)) brand = w1.brand;
                engine.createFeud(feudName, w1id, w2id, brand);
                toast("Feud created: " + feudName);
                showFeuds();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    // ═════════════════════════════════════════════════════════════════════
    // SHOW HISTORY
    // ═════════════════════════════════════════════════════════════════════

    private void showHistory() {
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, "SHOW HISTORY", engine.showHistory.size() + " shows booked");

        if (engine.showHistory.isEmpty()) {
            appendLine(sb, "  No shows booked yet.", C_DIM, false, 0.9f);
        } else {
            for (int i = engine.showHistory.size()-1; i >= 0; i--) {
                ShowRecord sh = engine.showHistory.get(i);
                int gc = gradeColor(sh.overallGrade);
                int brandCol = "RAW".equals(sh.brand) ? C_BRED : "SmackDown".equals(sh.brand) ? C_BLUE : C_GOLD;
                appendLine(sb, "  " + (i+1) + ". [" + sh.overallGrade + "]  " + sh.name, gc, true, 0.95f);
                appendLine(sb, "       [" + sh.brand + "]  Wk" + sh.week +
                        "  " + sh.viewersDisplay() + " viewers  " + sh.segments.size() + " segs",
                        brandCol, false, 0.8f);
                for (String tc : sh.titleChanges)
                    appendLine(sb, "       ★ " + tc, C_GOLD, false, 0.78f);
                sb.append("\n");
            }
        }

        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();
        if (!engine.showHistory.isEmpty())
            addBtn("🔍  View Show Detail", C_SURF2, C_TEXT, v -> pickShowDetail());
        addBtn("←  Back", C_SURF2, C_DIM, v -> showMainMenu());
    }

    private void pickShowDetail() {
        String[] names = new String[engine.showHistory.size()];
        for (int i = 0; i < engine.showHistory.size(); i++) {
            ShowRecord sh = engine.showHistory.get(i);
            names[i] = (i+1) + ". [" + sh.overallGrade + "] " + sh.name + " Wk" + sh.week;
        }
        new AlertDialog.Builder(this)
            .setTitle("Select Show")
            .setItems(names, (d, which) -> showResults(engine.showHistory.get(which)))
            .setNegativeButton("Cancel", null)
            .show();
    }

    // ═════════════════════════════════════════════════════════════════════
    // ADVANCE WEEK
    // ═════════════════════════════════════════════════════════════════════

    private void doAdvanceWeek() {
        int prev = engine.currentWeek;
        engine.advanceWeek();
        updateWeekBar();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        appendHeader(sb, "WEEK ADVANCED",
                "Now Week " + engine.currentWeek + "  ·  " + engine.getWeekDate());
        appendLine(sb, "  Previous: Week " + prev, C_DIM, false, 0.85f);
        appendLine(sb, "  Current:  Week " + engine.currentWeek + "  " + engine.getWeekDate(),
                C_TEXT, true, 0.95f);
        sb.append("\n");

        GameEngine.PPVEvent next = engine.getNextPPV();
        if (next != null && next.week == engine.currentWeek) {
            appendLine(sb, "  ★★★  PPV WEEK: " + next.name + "  ★★★", C_GOLD, true, 1.1f);
            appendLine(sb, "  " + next.venue, C_DIM, false, 0.85f);
        } else if (next != null) {
            appendLine(sb, "  Next PPV: " + next.name + " in " + (next.week - engine.currentWeek) + " week(s)",
                    C_GOLD, false, 0.9f);
        }

        // Injury recovery report
        for (Wrestler w : engine.roster)
            if (!w.injured && w.injuryWeeksLeft == 0 && w.wins + w.losses > 0)
                ; // skip
        sb.append("\n");
        appendLine(sb, "  Feuds updated. Popularity shifts applied.", C_DIM, false, 0.8f);

        tvOutput.setText(sb);
        scrollToTop();
        clearButtons();
        addBtn("📋  Book a Show",   C_RED,   C_TEXT, v -> showBookMenu());
        addBtn("📊  Dashboard",     C_SURF2, C_TEXT, v -> showDashboard());
        addBtn("←  Main Menu",      C_SURF2, C_DIM,  v -> showMainMenu());
    }

    // ═════════════════════════════════════════════════════════════════════
    // HELPER: SPANNABLE BUILDERS
    // ═════════════════════════════════════════════════════════════════════

    private void appendLine(SpannableStringBuilder sb, String text, int color,
                            boolean bold, float sizeRel) {
        int start = sb.length();
        sb.append(text).append("\n");
        int end = sb.length();
        sb.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (bold)
            sb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (sizeRel != 1.0f)
            sb.setSpan(new RelativeSizeSpan(sizeRel), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void appendHeader(SpannableStringBuilder sb, String title, String sub) {
        appendLine(sb, "════════════════════════════════════════", C_GOLD, false, 0.7f);
        appendLine(sb, "  " + title, C_TEXT, true, 1.3f);
        if (sub != null && !sub.isEmpty())
            appendLine(sb, "  " + sub, C_DIM, false, 0.85f);
        appendLine(sb, "════════════════════════════════════════", C_GOLD, false, 0.7f);
        sb.append("\n");
    }

    private void appendSectionHead(SpannableStringBuilder sb, String title) {
        sb.append("\n");
        appendLine(sb, "  ── " + title + " ──", C_GOLD, true, 0.9f);
        appendLine(sb, "  ────────────────────────────────────", C_BORDER, false, 0.7f);
    }

    private int gradeColor(String g) {
        switch (g) {
            case "A*": return C_GOLD;
            case "A":  return C_GREEN;
            case "B":  return C_BLUE;
            case "C":  return C_ORANGE;
            case "D":  return 0xFFFF7043;
            case "E":  return 0xFFEF5350;
            default:   return 0xFFB71C1C;
        }
    }

    private String intensityBar(int v) {
        StringBuilder bar = new StringBuilder("  [");
        int filled = v * 20 / 100;
        for (int i = 0; i < 20; i++) bar.append(i < filled ? "█" : "░");
        bar.append("] ").append(v).append("/100");
        return bar.toString();
    }

    private int minParticipants(String mt) {
        switch (mt) {
            case "Triple Threat":        return 3;
            case "Fatal 4-Way":          return 4;
            case "Royal Rumble":         return 2;
            case "Elimination Chamber":  return 4;
            case "Tag Team Match":       return 4;
            default:                     return 2;
        }
    }

    private int maxParticipants(String mt) {
        switch (mt) {
            case "Triple Threat":        return 3;
            case "Fatal 4-Way":          return 4;
            case "Royal Rumble":         return 6;
            case "Elimination Chamber":  return 6;
            case "Tag Team Match":       return 4;
            default:                     return 2;
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // HELPER: BUTTON BUILDERS
    // ═════════════════════════════════════════════════════════════════════

    private void clearButtons() {
        btnGrid.removeAllViews();
    }

    private Button makeBtn(String label, int bgColor, int textColor, View.OnClickListener listener) {
        Button btn = new Button(this);
        btn.setText(label);
        btn.setTextColor(textColor);
        btn.setBackgroundColor(bgColor);
        btn.setAllCaps(false);
        btn.setTextSize(14f);
        btn.setTypeface(Typeface.DEFAULT_BOLD);
        btn.setPadding(24, 20, 24, 20);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(8, 4, 8, 4);
        btn.setLayoutParams(lp);
        btn.setOnClickListener(listener);
        return btn;
    }

    private void addBtn(String label, int bg, int fg, View.OnClickListener l) {
        btnGrid.addView(makeBtn(label, bg, fg, l));
    }

    private void addBtnRow(String[] labels, int[] bgs, View.OnClickListener[] listeners) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowLp.setMargins(4, 2, 4, 2);
        row.setLayoutParams(rowLp);

        for (int i = 0; i < labels.length; i++) {
            Button btn = new Button(this);
            btn.setText(labels[i]);
            btn.setTextColor(C_TEXT);
            btn.setBackgroundColor(bgs[i]);
            btn.setAllCaps(false);
            btn.setTextSize(12f);
            btn.setTypeface(Typeface.DEFAULT_BOLD);
            btn.setPadding(8, 14, 8, 14);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            lp.setMargins(4, 0, 4, 0);
            btn.setLayoutParams(lp);
            btn.setOnClickListener(listeners[i]);
            row.addView(btn);
        }
        btnGrid.addView(row);
    }

    // ═════════════════════════════════════════════════════════════════════
    // MISC UTILS
    // ═════════════════════════════════════════════════════════════════════

    private void updateWeekBar() {
        tvWeek.setText("WEEK " + engine.currentWeek);
    }

    private void scrollToTop() {
        scrollView.post(() -> scrollView.scrollTo(0, 0));
    }

    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private static final int C_BORDER = 0xFF252540;
}
