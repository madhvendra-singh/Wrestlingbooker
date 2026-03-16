# WWE Booker Sim — Android  
## How to Build Your APK

---

### Option A — Android Studio (Recommended, ~10 minutes)

1. **Download Android Studio** (free)  
   → https://developer.android.com/studio

2. **Open the project**  
   - Launch Android Studio  
   - Choose **"Open"** (not "New Project")  
   - Navigate to this `wwe-booker-android/` folder and select it

3. **Wait for sync**  
   Android Studio will download Gradle and all dependencies automatically (~2 min on first run).

4. **Build the APK**  
   - Menu → **Build → Build Bundle(s) / APK(s) → Build APK(s)**  
   - Wait ~1 minute  
   - Click **"locate"** in the popup, or find it at:  
     `app/build/outputs/apk/debug/app-debug.apk`

5. **Install on your phone**  
   - Enable **"Install from unknown sources"** in phone Settings → Security  
   - Transfer the `.apk` to your phone (USB, email, Google Drive, etc.)  
   - Tap the file on your phone to install

---

### Option B — Command Line (if you have Java installed)

```bash
cd wwe-booker-android

# Linux / Mac
chmod +x gradlew
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

> **Note:** First run downloads Gradle (~100MB). Requires Java 8+ in PATH.

---

### Option C — GitHub Actions (no local install needed)

1. Create a free GitHub account  
2. Create a new repository  
3. Push this folder to it  
4. Create `.github/workflows/build.yml` with:

```yaml
name: Build APK
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Build APK
        run: cd wwe-booker-android && chmod +x gradlew && ./gradlew assembleDebug
      - uses: actions/upload-artifact@v3
        with:
          name: app-debug
          path: wwe-booker-android/app/build/outputs/apk/debug/app-debug.apk
```

5. Every push triggers a build — download the APK from the **Actions** tab.

---

## Project Requirements

| Tool           | Version  |
|----------------|----------|
| Android Studio | Hedgehog 2023.1+ (or later) |
| Java           | 8 or higher |
| Android SDK    | API 26+ (Android 8.0+)  |
| Gradle         | 8.4 (auto-downloaded)   |
| AGP            | 8.2.2 (auto-downloaded) |

---

## File Structure

```
wwe-booker-android/
├── BUILD_INSTRUCTIONS.md        ← You are here
├── build.gradle                 ← Root Gradle config
├── settings.gradle              ← Project settings
├── gradlew                      ← Unix build script
├── gradlew.bat                  ← Windows build script
├── gradle/wrapper/
│   └── gradle-wrapper.properties
└── app/
    ├── build.gradle             ← App module config (minSdk 26, targetSdk 34)
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/
        │   ├── layout/activity_main.xml
        │   ├── values/colors.xml
        │   ├── values/strings.xml
        │   ├── values/themes.xml
        │   └── drawable/ic_launcher.xml
        └── java/com/wwebooker/
            ├── MainActivity.java   ← All UI screens
            ├── GameEngine.java     ← All game logic, 51 wrestlers, 9 titles
            ├── Wrestler.java
            ├── Championship.java
            ├── Feud.java
            ├── Segment.java
            └── ShowRecord.java
```

---

## Game Features

- 📺 Book **Monday Night RAW** and **Friday Night SmackDown** weekly
- 🏆 **9 championships** with full title change history  
- 💪 **51 superstars** — complete current WWE roster with Pop/Ring/Mic/Look stats  
- ⚔️ **Feud system** — rivalries boost match grades by up to +9 points  
- 📅 **PPV calendar** — Royal Rumble → WrestleMania → SummerSlam and more  
- ⭐ **A\* through F match grading** — based on talent, stipulation, feuds, face/heel dynamic  
- 🔄 **Popularity shifts** — every show affects your roster's standing  
- ⚠️ **Random injuries** — hardcore matches risk putting stars on the shelf  
- 📊 **Full show history** — review every card you've ever booked
