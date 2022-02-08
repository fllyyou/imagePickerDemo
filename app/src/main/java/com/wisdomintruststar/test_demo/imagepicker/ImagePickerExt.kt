package com.wisdomintruststar.test_demo.imagepicker

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.luck.picture.lib.entity.LocalMedia
import com.wisdomintruststar.test_demo.imagepicker.ImagePicker

///扩展函数 获取文件路径
fun LocalMedia.filePath(): String {
    if (isCompressed) {
        return compressPath
    }
    if (isCut) {
        return cutPath
    }
    return realPath
}

///选取图片
fun Fragment.imagePicker(
    photoNum: Int = 1,
    isCrop: Boolean = false,
    isVideo: Boolean = false,
    isGif: Boolean = false,
    type: ImagePicker.PickerType = ImagePicker.PickerType.ALERT,
    block: ((list: MutableList<LocalMedia>?) -> Unit)? = null
) {
    ImagePicker.shared.picker(
        this,
        photoNum, isCrop, isVideo, isGif, type, block
    )
}

///选取图片
fun AppCompatActivity.imagePicker(
    photoNum: Int = 1,
    isCrop: Boolean = false,
    isVideo: Boolean = false,
    isGif: Boolean = false,
    type: ImagePicker.PickerType = ImagePicker.PickerType.ALERT,
    block: ((list: MutableList<LocalMedia>?) -> Unit)? = null
) {
    ImagePicker.shared.picker(
        this,
        photoNum, isCrop, isVideo, isGif, type, block
    )
}