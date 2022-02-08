package com.wisdomintruststar.test_demo.imagepicker

import androidx.fragment.app.Fragment
import coil.load
import com.luck.lib.camerax.SimpleCameraX
import com.luck.picture.lib.interfaces.OnCameraInterceptListener

///自定义相机
class CameraInterceptListener: OnCameraInterceptListener {

    override fun openCamera(fragment: Fragment?, cameraMode: Int, requestCode: Int) {
        if (fragment == null) {
            return
        }
        val cameraX = SimpleCameraX.of()
        cameraX.setCameraMode(cameraMode)
        cameraX.setVideoBitRate(3 * 1024 * 1024)
        cameraX.setVideoFrameRate(25)
        cameraX.isDisplayRecordChangeTime(true)
        cameraX.setOutputPathDir(fragment.requireContext().sandboxPath())
        cameraX.setImageEngine { _, url, imageView ->
            imageView.load(url)
        }
        cameraX.start(fragment.requireContext(), fragment, requestCode)
    }
}