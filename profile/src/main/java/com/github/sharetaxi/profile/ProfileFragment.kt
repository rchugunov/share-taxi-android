package com.github.sharetaxi.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.sharetaxi.general.LogoutCallback
import com.github.sharetaxi.profile.vm.ProfileViewModel
import com.github.sharetaxi.profile.vm.ProfileViewState
import com.squareup.picasso.Picasso
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
internal class ProfileFragment : Fragment() {

    private val ivProfile by lazy { view!!.findViewById<ImageView>(R.id.iv_profile) }
    private val tvFirstLastName by lazy { view!!.findViewById<TextView>(R.id.tv_name) }
    private val tvEmail by lazy { view!!.findViewById<TextView>(R.id.tv_email) }
    private val btnLogout by lazy { view!!.findViewById<Button>(R.id.btn_logout) }
    private val progressbar by lazy { view!!.findViewById<ProgressBar>(R.id.progressbar) }
    private val vm by inject<ProfileViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ivProfile.setImageResource(R.drawable.ic_mtrl_chip_checked_circle)
        vm.viewState.observe(this, Observer { render(it) })
        vm.loadProfile()

        btnLogout.setOnClickListener { vm.logout() }
    }

    private fun render(viewState: ProfileViewState) {
//        btnLogout.visibility = if (viewState.user != null) View.VISIBLE else View.GONE

        if (viewState.loggedOut) {
            if (activity is LogoutCallback) {
                (activity as LogoutCallback).logout()
            }
            return
        }

        progressbar.visibility = if (viewState.isUserProfileLoading) View.VISIBLE else View.GONE

        tvEmail.text = viewState.user?.email
        if (viewState.user?.firstName != null && viewState.user.secondName != null) {
            @SuppressLint("SetTextI18n")
            tvFirstLastName.text = "${viewState.user.firstName} ${viewState.user.secondName}"
        }

        viewState.user?.photoUrl?.apply {
            if (!isBlank()) {
                Picasso.get().load(this).into(ivProfile)
            }
        }

        viewState.userProfileException?.apply {
            Toast.makeText(requireActivity(), this.message ?: "Something was wrong", Toast.LENGTH_SHORT).show()
            Log.e(TAG, this.message, this)
        }
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}