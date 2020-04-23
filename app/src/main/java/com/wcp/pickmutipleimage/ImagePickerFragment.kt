package com.wcp.pickmutipleimage

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.wcp.pickmutipleimage.delegade.OnReturnImageUri
import com.wcp.pickmutipleimage.delegade.OnSelectImageFromCamera
import com.wcp.pickmutipleimage.delegade.OnSelectImageFromGallery
import kotlinx.android.synthetic.main.fragment_image_picker.*

/**
 * A simple [Fragment] subclass.
 */
class ImagePickerFragment : Fragment() {
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var onSelectImageFromGallery: OnSelectImageFromGallery
    private lateinit var onSelectImageFromCamera: OnSelectImageFromCamera

    companion object {
        const val FRAG = "ImagePickerFragment"
        fun newInstance(): ImagePickerFragment = ImagePickerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onSelectImageFromGallery = context as OnSelectImageFromGallery
        onSelectImageFromCamera = context as OnSelectImageFromCamera
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        btnGallery.setOnClickListener {
            onSelectImageFromGallery.onPickImage(object : OnReturnImageUri{
                override fun imageUri(imageUri: Uri) {
                    imageAdapter.setImageUri(imageUri)
                }
            })
        }
        btnCamera.setOnClickListener {
            onSelectImageFromCamera.onCaptureImage(object : OnReturnImageUri{
                override fun imageUri(imageUri: Uri) {
                    imageAdapter.setImageUri(imageUri)
                }
            })
        }
    }

    private fun setAdapter() {
        imageAdapter = ImageAdapter(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = imageAdapter
    }
}
