package com.wisdomintruststar.test_demo.imagepicker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.engine.CropEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.utils.DateUtils
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import java.io.File
import java.util.*

class ImageCropEngine: CropEngine {

    override fun onStartCrop(
        fragment: Fragment?,
        currentLocalMedia: LocalMedia?,
        dataSource: ArrayList<LocalMedia>?,
        requestCode: Int
    ) {
        if (fragment == null) {
            return
        }
        currentLocalMedia?.availablePath?.let { path ->
            val inputUri = if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
                Uri.parse(path)
            } else {
                Uri.fromFile(File(path))
            }
            val fileName = DateUtils.getCreateFileName("CROP_") + ".jpg"
            val sandBoxPath = fragment.requireContext().sandboxPath()
            val destUri = Uri.fromFile(File(sandBoxPath, fileName))
            val data = mutableListOf<String>()
            dataSource?.forEach {
                data.add(it.availablePath)
            }
            val crop = UCrop.of(inputUri, destUri, data as ArrayList<String>)
            crop.withAspectRatio(1f, 1f)
            crop.withOptions(UCrop.Options().apply {
                setHideBottomControls(true)
                setShowCropFrame(true)
                setShowCropGrid(false)
                withAspectRatio(1.0f, 1.0f)
                setCropOutputPathDir(sandBoxPath)
                setStatusBarColor(Color.parseColor("#393a3e"))
                setToolbarColor(Color.parseColor("#393a3e"))
                setToolbarWidgetColor(Color.WHITE)
            })
            crop.setImageEngine(ImageEngine())
            crop.start(fragment.requireContext(), fragment, requestCode)

        }
    }

    inner class ImageEngine: UCropImageEngine {
        override fun loadImage(context: Context?, url: String?, imageView: ImageView?) {
            imageView?.load(url)
        }

        override fun loadImage(
            context: Context?,
            url: Uri?,
            maxWidth: Int,
            maxHeight: Int,
            call: UCropImageEngine.OnCallbackListener<Bitmap>?
        ) {
            context?.apply {
                imageLoader.enqueue(
                    ImageRequest.Builder(this)
                        .data(url)
                        .target {
                            call?.onCall(it.toBitmap())
                        }
                        .build()
                )
            }
        }

    }

}