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
import androidx.lifecycle.Observer
import com.example.cookbook.databinding.FragmentProfileBinding
import com.example.cookbook.injections.Injections
import com.example.cookbook.loginPage.LandingPageActivity
import com.example.cookbook.models.Recipe
import com.example.cookbook.settingsPage.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


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
            binding.profileRegisterDate.text = getString(R.string.register_date,getDate())
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
        initProfileViewModel()
        observerOnRecipeList()
        listenerOnMenu()
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

    // ------------------------------------ UTILS ------------------------------------

    private fun showPopUp(v: View) {
        PopupMenu(actualContext, v).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener(this@ProfileFragment)
            inflate(R.menu.profile_menu)
            show()
        }
    }

    private fun getDate():String{
        val nonFormatDate = Date(profileViewModel.creationDate.value!!)
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return  formatter.format(nonFormatDate)
    }

    private fun getStatistic(){
        binding.starterQuantity.text = profileViewModel.starterNumber.value.toString()
        binding.mainQuantity.text = profileViewModel.mainNumber.value.toString()
        binding.dessertQuantity.text= profileViewModel.dessertNumber.value.toString()
        binding.drinkQuantity.text = profileViewModel.drinkNumber.value.toString()
    }

    private fun initStatistic(recipes:List<Recipe>){
        var starterCount = 0
        var mainCount = 0
        var drinkCount = 0
        var dessertCount = 0
        recipes.forEach {
            when(it.baseDataRecipe?.category){
                "Starter" -> starterCount += 1
                "Main" ->mainCount +=1
                "Dessert" ->dessertCount += 1
                "Drink" ->drinkCount += 1
            }
        }
        profileViewModel.starterNumber.value = starterCount
        profileViewModel.mainNumber.value = mainCount
        profileViewModel.dessertNumber.value = dessertCount
        profileViewModel.drinkNumber.value=drinkCount
        getStatistic()
    }

    // ------------------------------------ OBSERVERS ------------------------------------

    private fun observerOnRecipeList(){
        profileViewModel.recipes.observe(viewLifecycleOwner, Observer {
            initStatistic(it)
        })
    }

    // ------------------------------------ INIT ------------------------------------

    private fun initProfileViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(actualContext)
        profileViewModel = ViewModelProviders.of(requireActivity(),viewModelFactory).get(ProfileViewModel::class.java)
    }

    // ------------------------------------ LISTENER ------------------------------------

    private fun listenerOnMenu(){
        menu_more.setOnClickListener {
            showPopUp(it)
        }
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
