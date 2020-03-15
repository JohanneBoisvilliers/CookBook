package com.example.cookbook.addRecipePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cookbook.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetPhoto : BottomSheetDialogFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bottom_sheet_photo, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}