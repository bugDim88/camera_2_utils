package com.bugdim88.camera_2_utils

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bugdim88.library.Camera2Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CameraFragment(), null).commit()
    }

    class CameraFragment : Camera2Fragment() {
        override fun saveCapturedImage(image: Image) {
            context?.let {
                Toast.makeText(
                    it, "${image.width} x ${image.height} photo captured!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
