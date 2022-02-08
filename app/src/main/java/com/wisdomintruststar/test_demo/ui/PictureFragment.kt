package com.wisdomintruststar.test_demo.ui

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.wisdomintruststar.test_demo.R
import com.wisdomintruststar.test_demo.databinding.HomeFragmentBinding
import com.wisdomintruststar.test_demo.databinding.PictureFragmentBinding
import com.wisdomintruststar.test_demo.imagepicker.ImagePicker
import com.wisdomintruststar.test_demo.imagepicker.imagePicker
import java.io.File

class PictureFragment : Fragment() {

    companion object {
        fun newInstance() = PictureFragment()
    }

    private lateinit var viewModel: PictureViewModel
    private lateinit var binder: PictureFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binder = DataBindingUtil.inflate<PictureFragmentBinding>(inflater, R.layout.picture_fragment, container, false)
        binder.action = PictureFragmentAction()
        this.binder = binder
        return binder.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PictureViewModel::class.java)
        // TODO: Use the ViewModel
    }

    inner class PictureFragmentAction {

        fun takePhoto() {
            imagePicker(isCrop = true) {
                it?.let { list ->
                    this@PictureFragment.binder.pictureView.setImageURI(Uri.fromFile(File(list.first().cutPath)))
                    this@PictureFragment.binder.executePendingBindings()
                }
            }
        }

    }

}