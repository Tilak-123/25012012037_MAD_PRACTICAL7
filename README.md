# Mobile Application Development - Practical 7

**Project Name:** `25012012037_MAD_PRACTICAL7`  
**Topic:** REST API Data Parsing, SQLite Local Database Storage, RecyclerView, and Google Maps Intent Integration

---

## 📌 Overview

This Android application demonstrates end-to-end integration of networking, local database persistence, and external intent handling in Kotlin. The app fetches user profile data from a RESTful API in JSON format using background coroutines, parses and stores the records locally in an **SQLite** database, renders the items in a **RecyclerView**, and enables location viewing on **Google Maps** via explicit/implicit intents.

---

## 🚀 Key Features

- 🌐 **REST API Request**: Uses HTTP Connection (`HttpURLConnection`) handled inside background threads via Kotlin Coroutines (`Dispatchers.IO`).
- 📄 **JSON Parsing**: Custom secondary constructor in the `Person` model to parse nested JSON objects (`profile`, `location`, `name`, `email`, `phone`, `address`, `lat`, `long`).
- 💾 **SQLite Local Database Persistence**: Implements `SQLiteOpenHelper` (`DatabaseHelper`) for offline data storage supporting `INSERT`, `QUERY`, `UPDATE`, and `DELETE` operations.
- 📱 **RecyclerView & Custom Adapter**: Displays the list of contact items (`singleitem.xml`) with action buttons for Map location navigation and item deletion.
- 🗺️ **Google Maps Intent Integration**: Passes the `Person` data model as a `Serializable` extra to `MapActivity`, which launches Google Maps via the `geo:` URI intent scheme with web fallback.
- 🔄 **Offline Fallback & Refresh**: Handles offline mode or API failures with fallback sample data and features a Floating Action Button (`fabRefresh`) to re-sync data.

---

## 📂 Project Structure & Key Classes

```text
app/src/main/java/com/example/a25012012037_mad_practical7/
├── Person.kt          # Data model implementing Serializable with JSON parsing constructor
├── HttpRequest.kt     # Helper class for making HTTP GET service calls
├── DatabaseHelper.kt  # SQLiteOpenHelper implementation for SQLite CRUD operations
├── PersonAdapter.kt   # RecyclerView.Adapter binding person items to singleitem.xml
├── MainActivity.kt    # Main screen managing API fetch, DB sync, RecyclerView, and UI states
└── MapActivity.kt     # Details screen displaying contact coordinates & triggering Map Intent
```

### File Details

1. [Person.kt](file:///C:/Users/pandy/AndroidStudioProjects/25012012037_MAD_PRACTICAL7/app/src/main/java/com/example/a25012012037_mad_practical7/Person.kt)
   - Implements `Serializable` to pass object instances across Activity Intents.
   - Includes custom JSON parsing constructor for mapping nested JSON fields.

2. [DatabaseHelper.kt](file:///C:/Users/pandy/AndroidStudioProjects/25012012037_MAD_PRACTICAL7/app/src/main/java/com/example/a25012012037_mad_practical7/DatabaseHelper.kt)
   - Manages the `persons_db` database and `persons` table.
   - Operations: `insertPerson()`, `getPerson()`, `allPersons`, `personsCount`, `updatePerson()`, `deletePerson()`.

3. [HttpRequest.kt](file:///C:/Users/pandy/AndroidStudioProjects/25012012037_MAD_PRACTICAL7/app/src/main/java/com/example/a25012012037_mad_practical7/HttpRequest.kt)
   - Establishes `HttpURLConnection` for fetching remote network data with optional Bearer header token support.

4. [PersonAdapter.kt](file:///C:/Users/pandy/AndroidStudioProjects/25012012037_MAD_PRACTICAL7/app/src/main/java/com/example/a25012012037_mad_practical7/PersonAdapter.kt)
   - Custom `RecyclerView.Adapter` binding contact data to UI views.
   - Includes click listeners for opening location in `MapActivity` and deleting contact from SQLite.

5. [MainActivity.kt](file:///C:/Users/pandy/AndroidStudioProjects/25012012037_MAD_PRACTICAL7/app/src/main/java/com/example/a25012012037_mad_practical7/MainActivity.kt)
   - Main entry point activity.
   - Executes Coroutines for background network calls, populates SQLite database, and updates `RecyclerView`.

6. [MapActivity.kt](file:///C:/Users/pandy/AndroidStudioProjects/25012012037_MAD_PRACTICAL7/app/src/main/java/com/example/a25012012037_mad_practical7/MapActivity.kt)
   - Receives `Person` object from `Intent` extra.
   - Displays full profile details and coordinates, launching Google Maps with `Uri.parse("geo:lat,long")`.

---

## 🛠️ Tech Stack & Dependencies

- **Language:** Kotlin
- **UI Components:** AndroidX AppCompat, Material Design 3, RecyclerView, ConstraintLayout
- **Asynchronous Programming:** Kotlin Coroutines (`Dispatchers.IO`, `Dispatchers.Main`)
- **Database:** SQLite (`SQLiteOpenHelper`)
- **Network & Data:** `HttpURLConnection`, `org.json.JSONObject`, `org.json.JSONArray`
- **Permissions:** `android.permission.INTERNET`

---

## 📋 How to Run the Project

1. **Clone or Open in Android Studio:**
   Open project folder `25012012037_MAD_PRACTICAL7` in Android Studio.

2. **Build Project:**
   Build the Gradle project and verify dependencies (`Sync Project with Gradle Files`).

3. **Run on Emulator or Device:**
   Select an Android Virtual Device (AVD) or physical device running Android 7.0 (API level 24) or higher and click **Run** (`Shift + F10`).

4. **Test Key Interactions:**
   - On initial launch, if the DB is empty, data is automatically fetched from the REST API (or fallback dataset) and saved to SQLite.
   - Click the **Map button** on any contact item to open contact details and view location on Google Maps.
   - Click the **Delete icon** to remove an item from the SQLite database.
   - Tap the **Floating Action Button (Refresh)** to fetch and add new/updated contacts from the API.
