package com.github.rchugunov.weather

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val btnAdd by lazy { findViewById<FloatingActionButton>(R.id.btn_add) }
    private val rvList by lazy { findViewById<EpoxyRecyclerView>(R.id.rv_forecast_data) }
    private val vm by lazy { ViewModelProviders.of(this).get(ForecastsViewModel::class.java) }
    private val controller = ForecastsEpoxyController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd.setOnClickListener { MapActivity.startForResult(this, Constants.REQUEST_CODE_NEW_LOCATION) }

        initRecyclerView()

        vm.forecastsHistoryLiveData.observe(this, Observer { list -> controller.setData(list) })

        if (savedInstanceState == null) {
            vm.loadHistoryResults()
        }
    }

    private fun initRecyclerView() {
        rvList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvList.setController(controller)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_CODE_NEW_LOCATION && resultCode == Activity.RESULT_OK) {
            val location = data?.getParcelableExtra<LatLng>(Constants.LOCATION_DATA_EXTRA)
            vm.addNewLocation(location)
        }
    }
}
