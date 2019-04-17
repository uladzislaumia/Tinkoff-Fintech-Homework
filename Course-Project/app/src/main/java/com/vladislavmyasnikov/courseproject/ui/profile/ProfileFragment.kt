package com.vladislavmyasnikov.courseproject.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vladislavmyasnikov.courseproject.R
import com.vladislavmyasnikov.courseproject.ui.main.GeneralFragment
import com.vladislavmyasnikov.courseproject.ui.viewmodels.ProfileViewModel

class ProfileFragment : GeneralFragment() {

    private val mProfileViewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        mSwipeRefreshLayout = SwipeRefreshLayout(inflater.context)
        mSwipeRefreshLayout.id = R.id.swipe_refresh_layout
        mSwipeRefreshLayout.addView(view)

        return mSwipeRefreshLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mFragmentListener?.setToolbarTitle(R.string.profile_toolbar_title)

        mSwipeRefreshLayout.setOnRefreshListener { mProfileViewModel.updateProfile() }

        val firstNameField = view.findViewById<TextView>(R.id.name_field)
        val lastNameField = view.findViewById<TextView>(R.id.surname_field)
        val middleNameField = view.findViewById<TextView>(R.id.patronymic_field)
        val avatarView = view.findViewById<ImageView>(R.id.avatar)

        mProfileViewModel.profileData.observe(this, Observer { profile ->
            firstNameField.text = profile.firstName
            lastNameField.text = profile.lastName
            middleNameField.text = profile.middleName
            mProfileViewModel.profileAvatar?.into(avatarView)
        })

        mProfileViewModel.messageState.observe(this, Observer { message ->
            if (message != null) {
                if (message != "") {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }
                mSwipeRefreshLayout.isRefreshing = false
            }
        })

        mProfileViewModel.uploadProfile()
        mProfileViewModel.updateProfile()
        mSwipeRefreshLayout.isRefreshing = true
    }

    override fun onDestroy() {
        super.onDestroy()
        mProfileViewModel.resetMessageState()
    }



    companion object {

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
