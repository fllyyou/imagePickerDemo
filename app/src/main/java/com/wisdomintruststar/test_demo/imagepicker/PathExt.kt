package com.wisdomintruststar.test_demo.imagepicker

import android.content.Context
import java.io.File

///获取沙盒路径
fun Context.sandboxPath(): String {
    val file = getExternalFilesDir("")
    var path = File.separator
    file?.let {
        val sandBoxFile = File(it.absolutePath, "SandBox")
        if (!sandBoxFile.exists()) {
            sandBoxFile.mkdirs()
        }
        path = sandBoxFile.absolutePath + path
    }
    return path
}