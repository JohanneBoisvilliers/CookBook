package com.example.cookbook.loginPage

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cookbook.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_landing_page.*
import kotlinx.android.synthetic.main.bottom_sheet_login_page.*

class LandingPageActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var isExpanded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        this.init()
    }

    private fun init() {
        this.paramBottomSheetBehavior()
        this.listenerOnRegisterWord()
        this.listenerOnClickableWord()
        this.listenerOnLogin()
    }

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
    //set title, button text, bottom sheet word
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
}
