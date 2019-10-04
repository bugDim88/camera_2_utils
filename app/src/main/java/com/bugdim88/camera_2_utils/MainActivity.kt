package com.bugdim88.camera_2_utils

import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.bugdim88.library.Camera2Fragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import kotlin.math.ln
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CameraFragment(), null).commit()
        qualitiy_seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                tv_qualitiy.text = (p1 + 1).toString()
            }
        })
    }

    fun getPhotoQuality(): Int = tv_qualitiy.text.toString().toIntOrNull() ?: 100

    class CameraFragment : Camera2Fragment() {
        private val TAG = "CameraFragment"
        private val maxFilesCount = 5
        private var currentFileIndex = 0
            set(value) {
                field = if (value > 5) 1 else value
            }

        override fun onPhotoButtonClick() {
            outputJPEGQuality = (activity as? MainActivity)?.getPhotoQuality() ?: 100
            super.onPhotoButtonClick()
        }

        override fun saveCapturedImage(image: Image) {
            context?.let {
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val photoSize = humanReadableByteCount(bytes.size.toLong(), false)
                var output: FileOutputStream? = null
                val filePath = DIR_NAME + File.separator + (++currentFileIndex).toString() + "_$outputJPEGQuality" + ".jpg"
                val file = File(filePath)
                file.parentFile.mkdirs()
                file.createNewFile()
                try {
                    output = FileOutputStream(file).apply {
                        write(bytes)
                    }
                } catch (e: IOException) {
                    Log.e(TAG, e.toString())
                } finally {
                    image.close()
                    output?.let {
                        try {
                            it.close()
                        } catch (e: IOException) {
                            Log.e(TAG, e.toString())
                        }
                    }
                }
                Toast.makeText(
                    it, "Size: $photoSize photo captured!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun exceptionCallback(e: Exception) {
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            view?.postDelayed({
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, CameraFragment(), null)?.commit()
            }, 1000)
        }

    }
}

fun humanReadableByteCount(bytes: Long, si: Boolean): String {
    val unit = if (si) 1000 else 1024
    if (bytes < unit) return "$bytes B"
    val exp = (ln(bytes.toDouble()) / ln(unit.toDouble())).toInt()
    val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
    return String.format("%.1f %sB", bytes / unit.toDouble().pow(exp.toDouble()), pre)
}

val DIR_NAME = Environment.getExternalStorageDirectory().toString() + File.separator + "PhotoSample"
