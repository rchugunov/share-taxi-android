package com.github.sharetaxi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapActivity : AppCompatActivity() {

    private val btnOk by lazy { findViewById<FloatingActionButton>(R.id.btn_ok) }
    private val tvLocation by lazy { findViewById<TextView>(R.id.tv_location) }
    private val mapFragment by lazy { supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment }
    private var marker: Marker? = null
    private lateinit var resultReceiver: AddressResultReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        resultReceiver = AddressResultReceiver(Handler())

        mapFragment.getMapAsync { googleMap ->
            googleMap.setOnMapClickListener { latLng ->
                if (marker == null) {
                    marker = googleMap.addMarker(MarkerOptions().position(latLng))
                } else {
                    marker?.position = latLng
                }
                runGeocodingService(latLng)
            }
        }

        btnOk.setOnClickListener {
            val intent = Intent().apply {
                putExtra(Constants.LOCATION_DATA_EXTRA, marker?.position)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
        return true
    }

    private fun runGeocodingService(latLng: LatLng) {
        tvLocation.visibility = View.GONE
        val intent = Intent(this, GeocodingService::class.java).apply {
            putExtra(Constants.RECEIVER, resultReceiver)
            putExtra(Constants.LOCATION_DATA_EXTRA, latLng)
        }
        startService(intent)
    }

    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            val addressOutput = resultData?.getString(Constants.RESULT_DATA_KEY)
            tvLocation.text = addressOutput
            tvLocation.visibility = if (addressOutput == null) View.GONE else View.VISIBLE
            btnOk.visibility = if (addressOutput == null) View.GONE else View.VISIBLE
        }
    }

    companion object {
        fun startForResult(activity: AppCompatActivity, requestCode: Int) {
            val intent = Intent(activity, MapActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }
}