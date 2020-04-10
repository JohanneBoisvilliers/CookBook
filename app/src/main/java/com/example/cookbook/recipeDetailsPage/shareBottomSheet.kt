package com.example.cookbook.recipeDetailsPage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.cookbook.R
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShareBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: RecipeViewModel

    companion object {
        const val TAG = "ModalShareBottomSheet"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_share_bottom_sheet, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    //------------------ INIT ------------------

    //get recipe view model for requests
    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }

    //extension for using lambda for onTextChanged function
    fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0.toString())
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
    }

}