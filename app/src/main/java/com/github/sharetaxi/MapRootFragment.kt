package com.github.sharetaxi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.sharetaxi.general.Constants
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapRootFragment : Fragment() {

    private val tvLocation by lazy { view!!.findViewById<TextView>(R.id.tv_location) }
    private val mapFragment by lazy { childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment }
    private var marker: Marker? = null
    private lateinit var resultReceiver: AddressResultReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_root_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    private fun runGeocodingService(latLng: LatLng) {
        tvLocation.visibility = View.GONE
        val intent = Intent(activity!!, GeocodingService::class.java).apply {
            putExtra(Constants.RECEIVER, resultReceiver)
            putExtra(Constants.LOCATION_DATA_EXTRA, latLng)
        }
        activity?.startService(intent)
    }

    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            val addressOutput = resultData?.getString(Constants.RESULT_DATA_KEY)
            tvLocation.text = addressOutput
            tvLocation.visibility = if (addressOutput == null) View.GONE else View.VISIBLE
        }
    }
}