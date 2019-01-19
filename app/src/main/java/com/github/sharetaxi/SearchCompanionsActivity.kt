package com.github.sharetaxi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.sharetaxi.general.LogoutCallback
import com.github.sharetaxi.profile.ProfileRootFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchCompanionsActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    LogoutCallback {

    private val btmNavigation by lazy { findViewById<BottomNavigationView>(R.id.btm_navigation) }
    private val profileFragment by lazy { ProfileRootFragment() }
    private val mapFragment by lazy { MapRootFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_companions)
        btmNavigation.setOnNavigationItemSelectedListener(this)
        btmNavigation.selectedItemId = R.id.btn_map
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_map -> setFragment(mapFragment)
            R.id.btn_profile -> setFragment(profileFragment)
        }

        return true
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SearchCompanionsActivity::class.java))
        }
    }
}
