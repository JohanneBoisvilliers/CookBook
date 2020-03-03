package com.example.cookbook.loginPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cookbook.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_landing_page.*
import kotlinx.android.synthetic.main.bottom_sheet_login_page.*

class LandingPageActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        this.init()
    }

    private fun init() {
        // Set bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from<ConstraintLayout>(bottom_sheet_register)
        // Set listener on text "register"
        register_word_landing.setOnClickListener {
            slideUpDownBottomSheet()
            send_pass_button.text = getString(R.string.register_button)
            title_text.text = getString(R.string.title_register_page)
            clickable_word.text = getString(R.string.login_word)
        }
        // Set listener on Login button
        login_button.setOnClickListener {
            slideUpDownBottomSheet()
            send_pass_button.text = getString(R.string.login_button)
            title_text.text = getString(R.string.title_login_page)
            clickable_word.text = getString(R.string.register_word)
        }
    }

    private fun slideUpDownBottomSheet() {

        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }
}
