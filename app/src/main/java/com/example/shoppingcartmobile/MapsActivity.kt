package com.example.shoppingcartmobile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Get the map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        val granada = LatLng(37.1765, -3.5979)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(granada, 14f))

        mMap.addMarker(
            MarkerOptions()
                .position(granada)
                .title("Your Location")
                .snippet("Granada City Center")
        )

        val stores = listOf(
            Triple(LatLng(37.1780, -3.6000), "Main Store", "Granada City Center"),
            Triple(LatLng(37.1750, -3.5950), "Warehouse", "Near Train Station"),
            Triple(LatLng(37.1800, -3.6050), "Express Store", "Shopping Area")
        )

        // Add markers for each store
        stores.forEach { (location, name, description) ->
            mMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(name)
                    .snippet(description)
            )
        }

        Toast.makeText(this, "Showing stores in Granada", Toast.LENGTH_LONG).show()
    }
}