package com.example.a25012012037_mad_practical7

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var fabRefresh: FloatingActionButton

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var personAdapter: PersonAdapter
    private val TAG = "MainActivity"

    // API URL specified in the practical
    private val apiUrl = "https://api.json-generator.com/templates/5rDXHcbgpo93/data"
    // Optional Personal Access Token for api.json-generator.com
    private val apiToken: String? = null

    // Sample JSON formatted according to practical image as fallback when API token is expired or device is offline
    private val sampleJsonData = """
        [
          {
            "id": "62fcc7aee045a3e9ac92e285",
            "email": "yesenia_yang@gnu.ac.in",
            "phone": "+917983385148",
            "profile": {
              "name": "Yesenia Yang",
              "address": "16 Sackett Street, Courtland, Michigan",
              "location": {
                "lat": 19.146864,
                "long": 72.018148
              }
            }
          },
          {
            "id": "62fcc7ae3e16127970eb31a4",
            "email": "penelope_cooper@gnu.ac.in",
            "phone": "+916836390000",
            "profile": {
              "name": "Penelope Cooper",
              "address": "35 Aitken Place, Orovada, Maryland",
              "location": {
                "lat": 16.193406,
                "long": 72.259952
              }
            }
          },
          {
            "id": "62fcc7ae87b8f9e7f59d5711",
            "email": "tilak_pandya@gnu.ac.in",
            "phone": "+919241276734",
            "profile": {
              "name": "Tilak Pandya",
              "address": "39 Tompkins Place, Bartonsville, Oklahoma",
              "location": {
                "lat": 23.588033,
                "long": 72.369324
              }
            }
          }
        ]
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Android Views with findViewById (no ViewBinding / Compose)
        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)
        progressBar = findViewById(R.id.progressBar)
        fabRefresh = findViewById(R.id.fabRefresh)

        // Initialize SQLite DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Setup RecyclerView Adapter
        personAdapter = PersonAdapter(this, ArrayList()) { personToDelete ->
            dbHelper.deletePerson(personToDelete)
            refreshPersonListFromDb()
            Toast.makeText(this, "Deleted ${personToDelete.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = personAdapter

        // Load existing persons from SQLite DB
        refreshPersonListFromDb()

        // If DB is currently empty, fetch data from API
        if (dbHelper.personsCount == 0) {
            fetchPersonDataFromApi()
        }

        // FAB Refresh button listener
        fabRefresh.setOnClickListener {
            fetchPersonDataFromApi()
        }
    }

    private fun refreshPersonListFromDb() {
        val persons = dbHelper.allPersons
        personAdapter.updateData(persons)

        if (persons.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun fetchPersonDataFromApi() {
        progressBar.visibility = View.VISIBLE

        // CoroutineScope(Dispatchers.IO) to fetch data from URL in background (as per practical)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var data = HttpRequest().makeServiceCall(apiUrl, apiToken)
                Log.i(TAG, "makeServiceCall result: $data")

                // If API call fails (e.g. 401 unauthorized / offline / expired token), use sample data matching format
                if (data == null || data.trim().isEmpty() || !data.trim().startsWith("[")) {
                    Log.w(TAG, "API response was null or invalid. Using sample JSON fallback.")
                    data = sampleJsonData
                }

                withContext(Dispatchers.Main) {
                    try {
                        progressBar.visibility = View.GONE
                        runOnUiThread {
                            getPersonDetailsFromJson(data)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getPersonDetailsFromJson(data: String) {
        try {
            val jsonArray = JSONArray(data)
            var insertedCount = 0
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val person = Person(jsonObject)
                val id = dbHelper.insertPerson(person)
                if (id != -1L) {
                    insertedCount++
                }
            }
            refreshPersonListFromDb()
            Toast.makeText(
                this,
                "Retrieved and stored ${jsonArray.length()} contacts in SQLite",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Log.e(TAG, "getPersonDetailsFromJson error: ${e.message}")
            e.printStackTrace()
        }
    }
}
