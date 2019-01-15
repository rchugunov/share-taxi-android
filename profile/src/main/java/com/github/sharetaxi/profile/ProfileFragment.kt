package com.github.sharetaxi.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.sharetaxi.profile.vm.ProfileViewModel

internal class ProfileFragment : Fragment() {

    private val ivProfile by lazy { view!!.findViewById<ImageView>(R.id.iv_profile) }
    private val vm by lazy { ViewModelProviders.of(this).get(ProfileViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}