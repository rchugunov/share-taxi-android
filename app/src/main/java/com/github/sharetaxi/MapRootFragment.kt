package com.github.sharetaxi

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.sharetaxi.general.Constants
import com.github.sharetaxi.map.GoogleMapContainer
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds


class MapRootFragment : Fragment(), GoogleMapContainer.Callback {

    private val tvLocation by lazy { view!!.findViewById<TextView>(R.id.tv_location) }
    private val groupBottomBar by lazy { view!!.findViewById<Group>(R.id.group_bottom_bar) }

    private val mapContainer by lazy { GoogleMapContainer(childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment) }

    private val searchFragment by lazy {
        childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as SupportPlaceAutocompleteFragment
    }

    private lateinit var resultReceiver: AddressResultReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_root_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultReceiver = AddressResultReceiver(Handler())

        searchFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) = placeSelectedFromSearchFragment(place)

            override fun onError(p0: Status?) = placeNotFoundFromSearchFragment(p0)
        })

        mapContainer.setup(this)

        getLocationPermission()
    }

    private fun placeSelectedFromSearchFragment(place: Place) {
        tvLocation.text = place.address
        groupBottomBar.visibility = if (place.address == null) View.GONE else View.VISIBLE

        mapContainer.placeSelected(place.latLng)
    }

    private fun placeNotFoundFromSearchFragment(status: Status?) {
        status?.apply {
            Toast.makeText(requireContext(), statusMessage, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    override fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mapContainer.permissionGranted(true)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        mapContainer.permissionGranted(false)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapContainer.permissionGranted(true)
                }
            }
        }
    }

    override fun clickedOnMap(position: LatLng) = runGeocodingService(position)

    override fun mapBoundsChanged(newBounds: LatLngBounds) = searchFragment.setBoundsBias(newBounds)

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

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}