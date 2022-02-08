package com.wisdomintruststar.test_demo.imagepicker

import android.content.Context
import android.view.View
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.wisdomintruststar.test_demo.R

class ImagePickerActionView @JvmOverloads constructor(context: Context, private val block: ((type: ImagePickerActionType) -> Unit)? = null): BottomPopupView(context) {

    enum class ImagePickerActionType {
        CAMERA, PHOTO, CANCEL
    }

    override fun getImplLayoutId(): Int {
        return R.layout.popup_image_picker
    }

    override fun onCreate() {
        super.onCreate()
        findViewById<View>(R.id.xj).setOnClickListener {
            dismissWith {
                block?.let { it(ImagePickerActionType.CAMERA) }
            }
        }

        findViewById<View>(R.id.xc).setOnClickListener {
            dismissWith {
                block?.let { it(ImagePickerActionType.PHOTO) }
            }
        }

        findViewById<View>(R.id.cancel).setOnClickListener {
            dismissWith {
                block?.let { it(ImagePickerActionType.CANCEL) }
            }
        }

    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getAppHeight(context) * 0.85f).toInt()
    }

}