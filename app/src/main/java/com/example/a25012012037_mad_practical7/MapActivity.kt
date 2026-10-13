package com.example.a25012012037_mad_practical7

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class MapActivity : AppCompatActivity() {

    private val TAG = "MapActivity"

    private var lat: Double = 0.0
    private var log: Double = 0.0
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)

        val mapRoot = findViewById<View>(R.id.mapRoot)
        ViewCompat.setOnApplyWindowInsetsListener(mapRoot) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbarMap = findViewById<MaterialToolbar>(R.id.toolbarMap)
        val textName = findViewById<TextView>(R.id.textName)
        val textPhone = findViewById<TextView>(R.id.textPhone)
        val textEmail = findViewById<TextView>(R.id.textEmail)
        val textAddress = findViewById<TextView>(R.id.textAddress)
        val textCoordinates = findViewById<TextView>(R.id.textCoordinates)
        val btnOpenMap = findViewById<MaterialButton>(R.id.btnOpenMap)

        toolbarMap.setNavigationOnClickListener {
            finish()
        }

        @Suppress("DEPRECATION")
        val obj = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("Object", Person::class.java)
        } else {
            intent.getSerializableExtra("Object") as? Person
        }

        if (obj != null) {
            // Retrieve Person object passed as Serializable in Intent (matching practical slides)
            Log.i(TAG, "onCreate: Object:$obj")
            lat = obj.latitude
            log = obj.longitude
            title = obj.name

            textName.text = obj.name
            textPhone.text = obj.phoneNo
            textEmail.text = obj.emailId
            textAddress.text = obj.address
            textCoordinates.text = "Latitude: $lat\nLongitude: $log"

            btnOpenMap.setOnClickListener {
                val uri = Uri.parse("geo:$lat,$log?q=$lat,$log(${Uri.encode(title)})")
                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                try {
                    startActivity(mapIntent)
                } catch (e: Exception) {
                    val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$log")
                    val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                    startActivity(webIntent)
                }
            }
        } else {
            Toast.makeText(this, "Failed to load Person data", Toast.LENGTH_SHORT).show()
        }
    }
}
