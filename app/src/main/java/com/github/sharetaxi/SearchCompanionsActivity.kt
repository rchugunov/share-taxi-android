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
        supportFragmentManager.beginTransaction().add(R.id.container, profileFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container, mapFragment).commit()
        supportFragmentManager.beginTransaction().hide(profileFragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_map -> setFragment(mapFragment)
            R.id.btn_profile -> setFragment(profileFragment)
        }

        return true
    }

    private fun setFragment(fragment: Fragment) {
        val existingFragment = supportFragmentManager.findFragmentById(R.id.container)
        existingFragment?.apply { supportFragmentManager.beginTransaction().hide(this).commit() }
        supportFragmentManager.beginTransaction().show(fragment).commit()
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
