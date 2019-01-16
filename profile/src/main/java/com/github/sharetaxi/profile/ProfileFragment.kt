package com.github.sharetaxi.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.sharetaxi.profile.vm.ProfileViewModel
import com.github.sharetaxi.profile.vm.ProfileViewState
import com.squareup.picasso.Picasso

internal class ProfileFragment : Fragment() {

    private val ivProfile by lazy { view!!.findViewById<ImageView>(R.id.iv_profile) }
    private val tvFirstLastName by lazy { view!!.findViewById<TextView>(R.id.tv_name) }
    private val tvEmail by lazy { view!!.findViewById<TextView>(R.id.tv_email) }
    private val vm by lazy { ViewModelProviders.of(this).get(ProfileViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ivProfile.setImageResource(R.drawable.ic_mtrl_chip_checked_circle)
        vm.viewState.observe(this, Observer { render(it) })
        vm.loadProfile()
    }

    private fun render(viewState: ProfileViewState) {
        tvEmail.text = viewState.email
        if (viewState.firstName != null && viewState.secondName != null) {
            @SuppressLint("SetTextI18n")
            tvFirstLastName.text = "${viewState.firstName} ${viewState.secondName}"
        }

        viewState.photoPreviewUrl?.apply {
            Picasso.get().load(viewState.photoPreviewUrl).into(ivProfile)
        }
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}