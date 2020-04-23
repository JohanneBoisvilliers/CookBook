package com.example.cookbook.recipeDetailsPage

import android.R.attr
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.example.cookbook.R
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.fragment_share_bottom_sheet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File


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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.listenerOnShareButton()
        this.listenerOnCancelShareButton()
        this.listenerOnDescriptionField()
        this.observerOnIsUploaded()
    }

    //------------------ INIT ------------------

    //get recipe view model for requests
    private fun initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity()).get(RecipeViewModel::class.java)
    }

    fun byteSizeOf(bitmap: Bitmap): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.allocationByteCount
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            bitmap.byteCount
        } else {
            bitmap.rowBytes * bitmap.height
        }
    }

    //------------------ LISTENER ------------------

    //take recipe saved in viewmodel and take input in share_description field then call shared function in viewmodel
    private fun listenerOnShareButton() {
        val compressedList: MutableList<File> = mutableListOf()
        share_button.setOnClickListener {
            val isFieldEmpty = viewModel.shareDescription.value?.length!! < 5

            when (isFieldEmpty) {
                true -> shared_description.error = getString(R.string.description_error)
                false -> {
                    val actualRecipe = viewModel.actualRecipe.value

                    lifecycleScope.launch {
                        withContext(Dispatchers.Default) {
                            actualRecipe?.photoList?.forEach {
                                launch {
                                    val compressedFile = Compressor.compress(activity!!, File(it.photoUrl)) {
                                        size(50_000)
                                    }
                                    compressedList.add(compressedFile)
                                }
                            }
                        }
                        viewModel.uploadPhoto(actualRecipe!!, compressedList)
                    }
                }
            }
        }
    }

    //close bottom sheet if user click on cancel button
    private fun listenerOnCancelShareButton() {
        cancel_share_button.setOnClickListener {
            dismiss()
        }
    }

    // save the description that user put into share_description field in view model
    private fun listenerOnDescriptionField() {
        shared_description.onTextChanged {
            viewModel.shareDescription.value = it
        }
    }

    //------------------ LISTENER ------------------

    private fun observerOnIsUploaded() {
        viewModel.isUploaded.observe(viewLifecycleOwner, Observer {
            viewModel.isUploaded.postValue(false)
            if (it) dismiss()
        })
    }

    //------------------ EXTENSION ------------------

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