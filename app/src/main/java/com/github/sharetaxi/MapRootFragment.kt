package com.github.sharetaxi

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.sharetaxi.general.Constants
import com.github.sharetaxi.map.GoogleMapContainer
import com.github.sharetaxi.map.vm.MapViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.fragment_root_map.*
import org.koin.android.ext.android.inject


class MapRootFragment : Fragment(), GoogleMapContainer.Callback {

    private val mapContainer by lazy { GoogleMapContainer(childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment) }

    private val searchFragment by lazy {
        childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as SupportPlaceAutocompleteFragment
    }

    private lateinit var resultReceiver: AddressResultReceiver
    private val vm by inject<MapViewModel>()

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

        btnFrom.setOnClickListener {
            mapContainer.selection()?.apply { vm.placeFromSelected(this) }
            tvFrom.text = tvLocation.text
            if (tvFrom.text == tvTo.text) {
                tvTo.text = null
            }
            checkIfReadyToBuildRoute()
        }
        btnTo.setOnClickListener {
            mapContainer.selection()?.apply { vm.placeToSelected(this) }
            tvTo.text = tvLocation.text
            if (tvFrom.text == tvTo.text) {
                tvFrom.text = null
            }
            checkIfReadyToBuildRoute()
        }

        btnClear.setOnClickListener {
            tvTo.text = null
            tvFrom.text = null
            btnClear.visibility = View.GONE
            btnBuildRoute.visibility = View.GONE
        }
        btnBuildRoute.setOnClickListener { vm.performSearch() }

        getLocationPermission()
    }

    private fun checkIfReadyToBuildRoute() {
        if (tvFrom.text.isNotEmpty() && tvTo.text.isNotEmpty()) {
            mapContainer.showRoute()
            bottomBarCardView.visibility = View.GONE
            btnClear.visibility = View.VISIBLE
            btnBuildRoute.visibility = View.VISIBLE
        }
    }

    private fun placeSelectedFromSearchFragment(place: Place) {
        updateSelectedPlace(place.address)
        mapContainer.placeSelected(place.latLng)
    }

    private fun updateSelectedPlace(address: CharSequence?) {
        tvLocation.text = address
        bottomBarCardView.visibility = if (address == null) View.GONE else View.VISIBLE
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
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mapContainer.permissionGranted(true)
        } else {
            requestPermissions(
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
        bottomBarCardView.visibility = View.GONE
        val intent = Intent(activity!!, GeocodingService::class.java).apply {
            putExtra(Constants.RECEIVER, resultReceiver)
            putExtra(Constants.LOCATION_DATA_EXTRA, latLng)
        }
        activity?.startService(intent)
    }

    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            val addressOutput = resultData?.getString(Constants.RESULT_DATA_KEY)
            updateSelectedPlace(addressOutput)
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}