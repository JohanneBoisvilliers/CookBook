package com.example.cookbook.profilePage


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentProfileBinding
import com.example.cookbook.loginPage.LandingPageActivity
import com.example.cookbook.settingsPage.SettingsActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private var fbAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = null
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
            profileViewModel.name.value = if (!it.displayName.isNullOrEmpty()) it.displayName else it.email
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
        menu_more.setOnClickListener {
            showPopUp(it)
        }

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
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
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
//        fab_logout.setOnClickListener {
//            fbAuth.signOut()
//            fbAuth.addAuthStateListener {
//                if (fbAuth.currentUser == null) {
//                    try {
//                        val intent = Intent(actualContext, LandingPageActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
//                    } catch (e: Exception) {
//                        print(e)
//                    }
//                }
//            }
//        }
    }

    private fun showPopUp(v: View) {
        PopupMenu(actualContext, v).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener(this@ProfileFragment)
            inflate(R.menu.profile_menu)
            show()
        }
    }

    private fun configureProfileViewModel() {
        profileViewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel::class.java)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(actualContext,SettingsActivity::class.java))
                true
            }
            R.id.action_logout -> {
                fbAuth.signOut()
                fbAuth.addAuthStateListener {
                    if (fbAuth.currentUser == null) {
                        try {
                            val intent = Intent(actualContext, LandingPageActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } catch (e: Exception) {
                            print(e)
                        }
                    }
                }
                true
            }
            else -> false
        }
    }

}
