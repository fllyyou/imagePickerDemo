package com.wisdomintruststar.test_demo.imagepicker

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.interfaces.OnCallbackListener

class CoilEngine: ImageEngine {

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(url)
    }

    override fun loadImageBitmap(
        context: Context,
        url: String,
        maxWidth: Int,
        maxHeight: Int,
        call: OnCallbackListener<Bitmap>?
    ) {
        context.imageLoader.enqueue(ImageRequest.Builder(context)
            .target {
                call?.onCall(it.toBitmap())
            }
            .build())
    }

    override fun loadAlbumCover(context: Context, url: String, imageView: ImageView) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(url)
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(url)
    }

    override fun pauseRequests(context: Context?) {
        
    }

    override fun resumeRequests(context: Context?) {

    }
}