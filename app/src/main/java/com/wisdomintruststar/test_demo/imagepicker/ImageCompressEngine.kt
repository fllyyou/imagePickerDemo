package com.wisdomintruststar.test_demo.imagepicker

import android.content.Context
import android.net.Uri
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.engine.CompressEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnCallbackListener
import com.luck.picture.lib.utils.DateUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.ArrayList
import com.luck.picture.lib.utils.SdkVersionUtils
import android.text.TextUtils

class ImageCompressEngine: CompressEngine {
    override fun onStartCompress(
        context: Context?,
        list: ArrayList<LocalMedia>?,
        listener: OnCallbackListener<ArrayList<LocalMedia>>?
    ) {
        val compress = mutableListOf<Uri>()
        list?.forEach {
            it.availablePath.let { path ->
                val uri = if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
                    Uri.parse(path)
                } else {
                    Uri.fromFile(File(path))
                }
                compress.add(uri)
            }
        }
        if (compress.size <= 0) {
            listener?.onCall(list)
            return
        }
        Luban.with(context)
            .load(compress)
            .ignoreBy(100)
            .filter {
                PictureMimeType.isUrlHasImage(it) && !PictureMimeType.isHasHttp(it);
            }
            .setRenameListener {
                val indexOf: Int = it.lastIndexOf(".")
                val postfix = if (indexOf != -1) it.substring(indexOf) else ".jpg"
                DateUtils.getCreateFileName("CMP_").toString() + postfix
            }
            .setCompressListener(object : OnCompressListener {

                override fun onStart() {
                }

                override fun onSuccess(index: Int, compressFile: File?) {
                    compressFile?.let { file ->
                        val media = list?.get(index)
                        media?.let {
                            if (file.exists() && !TextUtils.isEmpty(file.absolutePath)) {
                                media.isCompressed = true
                                media.compressPath = compressFile.absolutePath
                                media.sandboxPath = if (SdkVersionUtils.isQ()) media.compressPath else null
                            }
                        }
                        if (index == list!!.size - 1) {
                            listener!!.onCall(list)
                        }
                    }

                }

                override fun onError(index: Int, e: Throwable?) {
                    if (index != -1) {
                        val media = list!![index]
                        media.isCompressed = false
                        media.compressPath = null
                        media.sandboxPath = null
                        if (index == list.size - 1) {
                            listener!!.onCall(list)
                        }
                    }
                }
            })
            .launch()
    }
}