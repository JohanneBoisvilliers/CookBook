package com.example.cookbook.profilePage


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentProfileBinding
import com.example.cookbook.loginPage.LandingPageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private var fbAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = null
    private var name: String? = null
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private var photoUrl: String? = null
    private lateinit var actualContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actualContext = context
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {

            photoUrl = it.photoUrl.toString()
            profileViewModel.name.value = if (!it.displayName.isNullOrEmpty()) it.displayName else "Unknown"
            profileViewModel.creationDate.value = it.metadata?.creationTimestamp

            binding.profileUsername.text = if (profileViewModel.name.value!!.isNotEmpty()) profileViewModel.name.value else "unknown"
            binding.profileRegisterDate.text = getString(R.string.register_date, profileViewModel.getDate())
            configureBackground()
        }
    }

    //inflate fragment recipe
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    //configure fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureProfileViewModel()
        listenerOnLogOutFab()
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ------------------------------------ UI ------------------------------------

    private fun configureBackground() {
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this)
                .load(photoUrl)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.no_photo)
                .centerCrop()
                .into(background_user_profile)
    }

    // ------------------------------------ SETTINGS ------------------------------------
    private fun listenerOnLogOutFab() {
        fab_logout.setOnClickListener {
            fbAuth.signOut()
        }

        fbAuth.addAuthStateListener {
            if (fbAuth.currentUser == null) {
                val intent = Intent(actualContext, LandingPageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun configureProfileViewModel() {
        profileViewModel = ViewModelProviders.of(activity!!).get(ProfileViewModel::class.java)
    }

}
