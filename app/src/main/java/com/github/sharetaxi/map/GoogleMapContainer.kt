package com.github.sharetaxi.map

import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapContainer(private val mapFragment: SupportMapFragment) {

    private val mapAccessor by lazy { MapAccessor(mapFragment) }
    private var marker: Marker? = null
//    // Construct a GeoDataClient.
//    private val mGeoDataClient by lazy { Places.getGeoDataClient(mapFragment.requireActivity()) }
//
//    // Construct a PlaceDetectionClient.
//    private val mPlaceDetectionClient by lazy { Places.getPlaceDetectionClient(mapFragment.requireActivity()) }

    // Construct a FusedLocationProviderClient.
    private val mFusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(mapFragment.requireActivity()) }

    private var mLocationPermissionGranted: Boolean = false

    private var callback: Callback? = null

    fun setup(callback: Callback? = null) {
        this.callback = callback
        mapAccessor.map {

//            uiSettings.isMapToolbarEnabled = true
            uiSettings.setAllGesturesEnabled(true)
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isCompassEnabled = false
            uiSettings.isMapToolbarEnabled = false

            setOnMapClickListener { latLng ->
                if (marker == null) {
                    marker = addMarker(MarkerOptions().position(latLng))
                } else {
                    marker?.position = latLng
                }
                callback?.clickedOnMap(latLng)
            }


            setOnCameraIdleListener {
                val bounds = projection.visibleRegion.latLngBounds
                callback?.mapBoundsChanged(bounds)
            }

            updateMyLocationUI(true)
        }
    }

    fun map(callback: GoogleMap.() -> Unit) = mapAccessor.map(callback)

    private fun GoogleMap.updateMyLocationUI(navigateToMe: Boolean) {
        try {
            if (mLocationPermissionGranted) {
                isMyLocationEnabled = true
                if (navigateToMe) {
                    mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 16f))
                    }
                }
//                uiSettings.isMyLocationButtonEnabled = true
            } else {
                isMyLocationEnabled = false
//                uiSettings.isMyLocationButtonEnabled = false
                callback?.getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    fun placeSelected(latLng: LatLng) {
        if (marker == null) {
            map { marker = addMarker(MarkerOptions().position(latLng)) }
        } else {
            marker?.position = latLng
        }

        map {
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
    }

    fun permissionGranted(isPermitted: Boolean) {
        mLocationPermissionGranted = isPermitted

        if (isPermitted) {
            map { updateMyLocationUI(true) }
        }
    }

    fun clear() {
        
    }

    fun showRoute() {

    }

    fun selection() = marker?.position

    private class MapAccessor(private val mapFragment: SupportMapFragment) {
        private var map: GoogleMap? = null

        fun map(callback: GoogleMap.() -> Unit) {
            if (map == null) {
                (mapFragment).getMapAsync {
                    map = it
                    callback(map!!)
                }
            } else {
                callback(map!!)
            }
        }
    }

    interface Callback {
        fun clickedOnMap(position: LatLng)

        fun mapBoundsChanged(newBounds: LatLngBounds)
        fun getLocationPermission()
    }
}
