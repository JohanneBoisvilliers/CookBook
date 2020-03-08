package com.example.cookbook.loginPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cookbook.MainActivity
import com.example.cookbook.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.android.synthetic.main.activity_landing_page.*
import kotlinx.android.synthetic.main.bottom_sheet_login_page.*


class LandingPageActivity : AppCompatActivity() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var isExpanded: Boolean = false
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 999
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var snackbar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        this.init()
        if (fbAuth.currentUser != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
            finish()
        }
    }

    private fun init() {
        fbAuth = FirebaseAuth.getInstance()
        this.configureGoogleSignInOptions()
        this.paramBottomSheetBehavior()
        this.listenerOnRegisterWord()
        this.listenerOnClickableWord()
        this.listenerOnLogin()
        this.listenerOnGoogleButton()
        this.listenerOnTwitterButton()
    }

    // open/close function to bottom sheet
    private fun slideUpDownBottomSheet() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    // Set listener on text in bottom landing page
    private fun listenerOnRegisterWord() {
        register_word_landing.setOnClickListener {
            paramBottomsheetUiInfos(
                    getString(R.string.register_button),
                    getString(R.string.title_register_page),
                    getString(R.string.login_word))
        }
    }

    // Set listener on text in bottom sheet
    private fun listenerOnClickableWord() {
        clickable_word.setOnClickListener {
            // set title, button text, bottom word depending to where we're come from
            if (clickable_word.text == getString(R.string.register_word)) {
                paramBottomsheetUiInfos(
                        getString(R.string.register_button),
                        getString(R.string.title_register_page),
                        getString(R.string.login_word))
            } else {
                paramBottomsheetUiInfos(
                        getString(R.string.login_button),
                        getString(R.string.title_login_page),
                        getString(R.string.register_word))
            }

        }
    }

    // Set listener on Login button
    private fun listenerOnLogin() {
        login_button.setOnClickListener {
            paramBottomsheetUiInfos(
                    getString(R.string.login_button),
                    getString(R.string.title_login_page),
                    getString(R.string.register_word))
        }
    }

    private fun listenerOnGoogleButton() {
        google_button.setOnClickListener { signInWithGoogle() }
    }
    private fun listenerOnTwitterButton() {
        twitter_button.setOnClickListener { signInWithTwitter() }
    }

    // set title, button text, bottom sheet word
    private fun paramBottomsheetUiInfos(buttonText: String, titleText: String, wordText: String) {
        slideUpDownBottomSheet()
        send_pass_button.text = buttonText
        title_text.text = titleText
        clickable_word.text = wordText
    }

    // handle callback for bottom sheet
    private fun paramBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from<ConstraintLayout>(bottom_sheet_register)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    //set isExpanded bool when bottom sheet is open with login button or register text
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        isExpanded = true
                    }
                    //when bottom sheet is collapsed , check if isExpanded is true
                    //if it's true, it's mean that we clicked on word in bottom sheet
                    //so we have to close bottom sheet and re open it with good informations
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        if (isExpanded) {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }
                    //if we close bottom sheet with dragging action, set isExpanded to false
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        isExpanded = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }

    fun configSnackbar(message: String) {
        snackbar = Snackbar.make(root_layout, message, Snackbar.LENGTH_INDEFINITE)
    }

    fun configureGoogleSignInOptions() {
        // Configure Google Sign In
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    fun signInWithGoogle() {
        configSnackbar("Authenticating...")
        snackbar.show()

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    fun signInWithTwitter() {
        configSnackbar("Authenticating...")
        snackbar.show()
        val provider = OAuthProvider.newBuilder("twitter.com")

        fbAuth
                .startActivityForSignInWithProvider( /* activity= */this, provider.build())
                .addOnSuccessListener(
                        OnSuccessListener<AuthResult?> {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            snackbar.dismiss()
                            finish()
                        })
                .addOnFailureListener(
                        OnFailureListener {
                            // Handle failure.
                        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
                snackbar.dismiss()
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                snackbar.dismiss()
                configSnackbar("Google sign in failed, try again")
                snackbar.show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        configSnackbar("bad credential")
                        snackbar.show()
                    }
                }
    }

}
