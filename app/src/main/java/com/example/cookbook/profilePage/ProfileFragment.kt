package com.example.cookbook.profilePage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.fragment.app.Fragment

import com.bumptech.glide.Glide
import com.example.cookbook.R

import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private var fbAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser:FirebaseUser?=null


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = fbAuth.currentUser
    }
    //inflate fragment recipe
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_profile, container, false)

    //configure fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       listenerOnLogOutFab()
    }

    companion object {

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    private fun listenerOnLogOutFab(){
        fab_logout.setOnClickListener{
            fbAuth.signOut()
        }

        fbAuth.addAuthStateListener {
            if (fbAuth.currentUser == null){
                activity?.finish()
            }
        }
    }

    // ------------------------------------ UI ------------------------------------
    //private void configureBackground() {
    //    Glide.with(this)
    //            .load(R.drawable.no_photo)
    //            .fitCenter()
    //            .into(mUserProfilePhoto);


}
