package com.example.cookbook.settingsPage

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.cookbook.R
import com.example.cookbook.injections.Injections
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SettingsActivity : AppCompatActivity() {

    lateinit var viewModel:SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        initViewModel()
    }

    private fun initViewModel(){
        val viewModelFactory = Injections.provideViewModelFactory(this)
        viewModel =  ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        val user = FirebaseAuth.getInstance().currentUser
        var usernamePreference: EditTextPreference? = null
        var photoPreference: EditTextPreference? = null
        lateinit var viewModel:SettingsViewModel

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            initViewModel()
            initPreferencesValues(sharedPreferences)
            listenerOnPhotoUrlPreference()
            listenerOnUsernamePreference()
        }

        // ---------------------------- INIT ----------------------------

        //get recipe view model for requests
        private fun initViewModel(){
            viewModel = ViewModelProviders.of(requireActivity()).get(SettingsViewModel::class.java)
        }

        private fun initPreferencesValues(sharedpref:SharedPreferences) {
            usernamePreference = findPreference("username")
            photoPreference = findPreference("profile_url")
            // set username summary with current user display name
            usernamePreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> {
                // get username in shared preferences
                val savedUsername = sharedpref.getString("username","")
                // if there is no username saved in shared preferences (user put an empty name or the first time user open settings page),
                // get the user display name, else get the username from shared preferences
                if (savedUsername?.isEmpty()!!) {
                    user?.displayName
                }else{
                    savedUsername
                }
            }
            // set photo url summary with current user photo url
            photoPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> {
                // get username in shared preferences
                val savedPhotoUrl = sharedpref.getString("profile_url","")
                // if there is no username saved in shared preferences (user put an empty name or the first time user open settings page),
                // get the user display name, else get the username from shared preferences
                if (savedPhotoUrl?.isEmpty()!!) {
                    user?.photoUrl.toString()
                }else{
                    savedPhotoUrl.toString()
                }
            }
        }

        // ---------------------------- UTILS ----------------------------

        private fun updateUserProfile(username:String,photoUrl:String) {
            viewModel.updateUserProfile(username,photoUrl)
        }

        // ---------------------------- LISTENERS  ----------------------------

        private fun listenerOnUsernamePreference(){
            usernamePreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _ , newValue ->
                usernamePreference?.text = newValue as String
                updateUserProfile(usernamePreference?.text!!,photoPreference?.text!!)
                false
            }
        }

        private fun listenerOnPhotoUrlPreference(){
            photoPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _ , newValue ->
                photoPreference?.text = newValue as String
                updateUserProfile(usernamePreference?.text!!,photoPreference?.text!!)
                false
            }
        }
    }
}