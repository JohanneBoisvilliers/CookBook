package com.example.cookbook.addRecipePage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.example.cookbook.R
import com.example.cookbook.models.Photo
import com.example.cookbook.recipesPage.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet_photo.*
import java.io.File

class PhotoBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: RecipeViewModel
    var PICK_IMAGE_MULTIPLE = 1
    var PICK_IMAGE_CAMERA = 2
    private var mUri: Uri? = null
    lateinit var imagePath: String
    var imagesPathList: MutableList<String> = arrayListOf()

    companion object {
        const val TAG = "ModalPhotoBottomSheet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bottom_sheet_photo, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenerOnPickFromGallery()
        listenerOnCameraButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
                && null != data
        ) {
            if (data.getClipData() != null) {
                var count = data.clipData?.itemCount
                for (i in 0 until count!!) {
                    var imageUri: Uri = data.clipData?.getItemAt(i)?.uri!!
                    getPathFromURI(imageUri)
                    val photo = Photo(photoUrl = imagePath, recipeId = viewModel.actualRecipe.value?.baseDataRecipe?.baseRecipeId!!)
                    viewModel.photoList.plus(photo)
                    viewModel.insertPhoto(photo)
                }
            } else if (data.data != null) {
                var uri: Uri = data.data!!
                getPathFromURI(uri)
                val photo = Photo(photoUrl = imagePath, recipeId = viewModel.actualRecipe.value?.baseDataRecipe?.baseRecipeId!!)
                viewModel.photoList.plus(photo)
                viewModel.insertPhoto(photo)
            }
        }
    }

    // ---------------- INIT -------------------

    //get recipe view model for requests
    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(RecipeViewModel::class.java)
    }

    // ---------------- UTILS -------------------

    private fun getPathFromURI(uri: Uri) {
        var path: String = uri.path!! // uri = any content Uri

        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (path.contains("/document/image:")) { // files selected from "Documents"
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else { // files selected from all other sources, especially on Samsung devices
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val projection = arrayOf(
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.ORIENTATION,
                    MediaStore.Images.Media.DATE_TAKEN
            ) // some example data you can query
            val cursor = activity?.contentResolver?.query(
                    databaseUri,
                    projection, selection, selectionArgs, null
            )
            if (cursor?.moveToFirst()!!) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                imagePath = cursor.getString(columnIndex)
                imagesPathList.add(imagePath)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
    }

    // ---------------- LISTENERS -------------------

    private fun listenerOnPickFromGallery() {
        pick_from_gallery_container.setOnClickListener {
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
        }
    }

    private fun listenerOnCameraButton() {
        pick_camera_container.setOnClickListener {
            val capturedImage = File(activity?.externalCacheDir, "My_Captured_Photo.jpg")
            if (capturedImage.exists()) {
                capturedImage.delete()
            }
            capturedImage.createNewFile()
            mUri = if (Build.VERSION.SDK_INT >= 24) {
                FileProvider.getUriForFile(activity!!, "com.example.cookbook.fileprovider",
                        capturedImage)
            } else {
                Uri.fromFile(capturedImage)
            }

            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
            startActivityForResult(intent, PICK_IMAGE_CAMERA)
        }

    }

    // ---------------- EXTENSIONS -------------------

    //extension for adding an item in a mutablelivedata list
    operator fun <T> MutableLiveData<MutableList<T>>.plus(values: T) {
        val value = this.value ?: arrayListOf()
        value.add(values)
        this.value = value
    }
}