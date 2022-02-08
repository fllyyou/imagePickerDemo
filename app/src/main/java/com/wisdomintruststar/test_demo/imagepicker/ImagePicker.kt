package com.wisdomintruststar.test_demo.imagepicker

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.PictureWindowAnimationStyle
import com.lxj.xpopup.XPopup
import java.lang.ref.WeakReference
import java.util.*

class ImagePicker private constructor(){

    enum class PickerType {
        CAMERA, PHOTO, ALERT
    }

    private var weakActivity: WeakReference<AppCompatActivity>? = null
    private var weakFragment: WeakReference<Fragment>? = null
    private var isGif: Boolean = false
    private var isCrop: Boolean = false
    private var isVideo: Boolean = false
    private var num: Int = 0
    private var block: ((MutableList<LocalMedia>?) -> Unit)? = null

    companion object {

        private val picker by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ImagePicker()
        }

        val shared: ImagePicker
            get() = picker

    }

    fun picker(fragment: Fragment,
               photoNum: Int = 1,
               isCrop: Boolean = false,
               isVideo: Boolean = false,
               isGif: Boolean = false,
               type: PickerType = PickerType.ALERT,
               block:((list: MutableList<LocalMedia>?)->Unit)? = null) {
        this.isGif = isGif
        this.isCrop = isCrop
        this.num = if (isCrop) {
            1
        } else {
            photoNum
        }
        this.isVideo = isVideo
        this.weakFragment = WeakReference(fragment)
        this.block = block
        when (type) {
            PickerType.CAMERA -> {
                pickerForCamera()
            }
            PickerType.PHOTO -> {
                pickerForAlbum()
            }
            PickerType.ALERT -> {
                fragment.requireContext().let {
                    XPopup.Builder(it)
                        .isDestroyOnDismiss(true)
                        .asCustom(ImagePickerActionView(it){ actionType ->
                            when (actionType) {
                                ImagePickerActionView.ImagePickerActionType.CAMERA -> {
                                    shared.pickerForCamera()
                                }
                                ImagePickerActionView.ImagePickerActionType.PHOTO -> {
                                    shared.pickerForAlbum()
                                }
                                else -> reset()
                            }
                        })
                        .show()
                }
            }
        }


    }

    fun picker(activity: AppCompatActivity,
               photoNum: Int = 1,
               isCrop: Boolean = false,
               isVideo: Boolean = false,
               isGif: Boolean = false,
               type: PickerType = PickerType.ALERT,
               block:((list: MutableList<LocalMedia>?)->Unit)? = null) {
        this.isGif = isGif
        this.isCrop = isCrop
        this.num = if (isCrop) {
            1
        } else {
            photoNum
        }
        this.isVideo = isVideo
        this.weakActivity = WeakReference(activity)
        this.block = block
        when (type) {
            PickerType.CAMERA -> {
                pickerForCamera()
            }
            PickerType.PHOTO -> {
                pickerForAlbum()
            }
            PickerType.ALERT -> {
                XPopup.Builder(activity)
                    .autoDismiss(true)
                    .isDestroyOnDismiss(true)
                    .asCustom(ImagePickerActionView(activity){ actionType ->
                        when (actionType) {
                            ImagePickerActionView.ImagePickerActionType.CAMERA -> {
                                pickerForCamera()
                            }
                            ImagePickerActionView.ImagePickerActionType.PHOTO -> {
                                pickerForAlbum()
                            }
                            else -> reset()
                        }
                    }).show()
            }
        }
    }

    private fun pickerForCamera() {
        ///二者会有其中一个启动
        weakActivity?.get()?.let {
            PictureSelector
                .create(it)
                .run {
                    if (isVideo) {
                        openCamera(SelectMimeType.ofVideo())
                    } else {
                        openCamera(SelectMimeType.ofImage())
                    }
                }
//                .setCameraInterceptListener(CameraInterceptListener())
                .apply {
                    if (isCrop) {
                        setCropEngine(ImageCropEngine())
                    }
                }
                .setCompressEngine(ImageCompressEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia> {

                    override fun onCancel() {
                        reset()
                    }

                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        shared.block?.let { it(result) }
                        reset()
                    }

                })

        }
        weakFragment?.get()?.let {
            PictureSelector
                .create(it)
                .run {
                    if (isVideo) {
                        openCamera(SelectMimeType.ofVideo())
                    } else {
                        openCamera(SelectMimeType.ofImage())
                    }
                }
//                .setCameraInterceptListener(CameraInterceptListener())
                .apply {
                    if (isCrop) {
                        setCropEngine(ImageCropEngine())
                    }
                }
                .setCompressEngine(ImageCompressEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia> {

                    override fun onCancel() {
                        reset()
                    }

                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        shared.block?.let { it(result) }
                        reset()
                    }

                })

        }
    }

    private fun pickerForAlbum() {
        ///二者会有其中一个启动
        weakActivity?.get()?.let {
            PictureSelector
                .create(it)
                .run {
                if (isVideo) {
                    openGallery(SelectMimeType.ofVideo())
                } else {
                    openGallery(SelectMimeType.ofImage())
                }
            }
                .setImageEngine(CoilEngine())
                .apply {
                    if (isCrop) {
                        setCropEngine(ImageCropEngine())
                    }
                }
                .setCompressEngine(ImageCompressEngine())
                .setSelectorUIStyle(PictureSelectorStyle().apply {
                    windowAnimationStyle = PictureWindowAnimationStyle().apply {
                        activityEnterAnimation = com.luck.picture.lib.R.anim.ps_anim_up_in
                        activityExitAnimation = com.luck.picture.lib.R.anim.ps_anim_down_out
                    }
                })
                .isDisplayCamera(false)
                .isGif(isGif)
                .isPreviewVideo(false)
                .setMaxSelectNum(num)
                .setMaxVideoSelectNum(1)
                .isSyncCover(false)
                .forResult(object : OnResultCallbackListener<LocalMedia> {

                    override fun onCancel() {
                        reset()
                    }

                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        shared.block?.let { it(result) }
                        reset()
                    }

                })

        }
        weakFragment?.get()?.let {
            PictureSelector
                .create(it)
                .run {
                    if (isVideo) {
                        openGallery(SelectMimeType.ofVideo())
                    } else {
                        openGallery(SelectMimeType.ofImage())
                    }
                }
                .setImageEngine(CoilEngine())
                .apply {
                    if (isCrop) {
                        setCropEngine(ImageCropEngine())
                    }
                }
                .setCompressEngine(ImageCompressEngine())
                .setSelectorUIStyle(PictureSelectorStyle().apply {
                    windowAnimationStyle = PictureWindowAnimationStyle().apply {
                        activityEnterAnimation = com.luck.picture.lib.R.anim.ps_anim_up_in
                        activityExitAnimation = com.luck.picture.lib.R.anim.ps_anim_down_out
                    }
                })
                .isDisplayCamera(false)
                .isGif(isGif)
                .isPreviewVideo(false)
                .setMaxSelectNum(num)
                .setMaxVideoSelectNum(1)
                .isSyncCover(false)
                .forResult(object : OnResultCallbackListener<LocalMedia> {

                    override fun onCancel() {
                        reset()
                    }

                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        shared.block?.let { it(result) }
                        reset()
                    }

                })

        }
    }

    private fun reset() {
        weakActivity = null
        weakFragment = null
        isGif = false
        num = 0
        isVideo = false
        isCrop = false
        block = null
    }

}