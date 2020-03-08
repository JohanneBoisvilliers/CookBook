package com.example.cookbook.loginPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import com.example.cookbook.MainActivity
import com.example.cookbook.R
import com.example.cookbook.injections.Injections
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
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var isExpanded: Boolean = false
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 999
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var snackbar: Snackbar
    override fun onStart() {
        super.onStart()
        if (fbAuth.currentUser != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        this.init()


    }

    private fun init() {
        fbAuth = FirebaseAuth.getInstance()
        this.initLoginViewModel()
        this.initGoogleSignInOptions()
        this.initBottomSheetBehavior()
        this.listenerOnRegisterWord()
        this.listenerOnClickableWord()
        this.listenerOnLogin()
        this.listenerOnGoogleButton()
        this.listenerOnTwitterButton()
        this.listenerOnEmailButton()
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
        loginViewModel.isRegisterExpanded.value = true
        loginViewModel.isLoginExpanded.value = false
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
                loginViewModel.isRegisterExpanded.value = true
                loginViewModel.isLoginExpanded.value = false
                paramBottomsheetUiInfos(
                        getString(R.string.register_button),
                        getString(R.string.title_register_page),
                        getString(R.string.login_word))
            } else {
                loginViewModel.isLoginExpanded.value = true
                loginViewModel.isRegisterExpanded.value = false
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
            loginViewModel.isLoginExpanded.value = true
            loginViewModel.isRegisterExpanded.value = false
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

    private fun listenerOnEmailButton() {
        email_button.setOnClickListener {
            if (loginViewModel.isRegisterExpanded.value == true) {
                initSnackBar(getString(R.string.snackbar_register), Snackbar.LENGTH_INDEFINITE)
                fbAuth.createUserWithEmailAndPassword("${username_input.text}", "${pass_input.text}")
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                snackbar.dismiss()
                                initSnackBar(getString(R.string.snackbar_success), Snackbar.LENGTH_SHORT)
                                loginViewModel.isRegisterExpanded.value = false
                                loginViewModel.isLoginExpanded.value = false
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            } else {
                                snackbar.dismiss()
                                initSnackBar("${task.exception?.message}", Snackbar.LENGTH_LONG)
                            }
                        }
            } else {
                initSnackBar(getString(R.string.snackbar_auth), Snackbar.LENGTH_INDEFINITE)
                fbAuth.signInWithEmailAndPassword("${username_input.text}", "${pass_input.text}")
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                snackbar.dismiss()
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                initSnackBar("${task.exception?.message}", Snackbar.LENGTH_LONG)
                            }
                        }
            }
        }
    }

    // set title, button text, bottom sheet word
    private fun paramBottomsheetUiInfos(buttonText: String, titleText: String, wordText: String) {
        slideUpDownBottomSheet()
        send_pass_button.text = buttonText
        title_text.text = titleText
        clickable_word.text = wordText
    }

    // handle callback for bottom sheet
    private fun initBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from<ConstraintLayout>(bottom_sheet_register)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    //when bottom sheet is collapsed , check if isExpanded is true
                    //if it's true, it's mean that we clicked on word in bottom sheet
                    //so we have to close bottom sheet and re open it with good informations
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        if (loginViewModel.isLoginExpanded.value!! or loginViewModel.isRegisterExpanded.value!!) {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }
                    //if we close bottom sheet with dragging action, set isExpanded to false
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        //isExpanded = false
                        loginViewModel.isRegisterExpanded.value = false
                        loginViewModel.isLoginExpanded.value = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }

    fun initSnackBar(message: String, duration: Int) {
        snackbar = Snackbar.make(root_layout, message, duration)
        snackbar.show()
    }

    fun initGoogleSignInOptions() {
        // Configure Google Sign In
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    fun signInWithGoogle() {
        initSnackBar(getString(R.string.snackbar_auth), Snackbar.LENGTH_INDEFINITE)
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun signInWithTwitter() {
        initSnackBar(getString(R.string.snackbar_auth), Snackbar.LENGTH_INDEFINITE)
        val provider = OAuthProvider.newBuilder("twitter.com")
        val pendingResultTask = fbAuth.pendingAuthResult
        if (pendingResultTask != null) { // There's something already here! Finish the sign-in for your user.
            pendingResultTask.addOnSuccessListener(
                    OnSuccessListener<AuthResult?> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        snackbar.dismiss()
                        finish()
                    }).addOnFailureListener {
                        snackbar.dismiss()
                        initSnackBar("${it.message}", Snackbar.LENGTH_LONG)
                    }
        } else { // There's no pending result so you need to start the sign-in flow.
            fbAuth.startActivityForSignInWithProvider( /* activity= */this, provider.build())
                    .addOnSuccessListener(
                            OnSuccessListener<AuthResult?> {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                snackbar.dismiss()
                                finish()
                            }).addOnFailureListener(this) {
                        snackbar.dismiss()
                        initSnackBar("${it.message}", Snackbar.LENGTH_LONG)
                    }
        }

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
                initSnackBar("${task.exception?.message}", Snackbar.LENGTH_LONG)
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
                        initSnackBar("${it.exception?.message}", Snackbar.LENGTH_LONG)
                    }
                }
    }

    private fun initLoginViewModel() {
        val viewModelFactory = Injections.provideViewModelFactory(this)
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

}
