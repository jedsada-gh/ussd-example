package com.example.ussd_example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnCheckPhoneNumber.setOnClickListener {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CALL_PHONE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            if (ContextCompat.checkSelfPermission(this@MainActivity,
                                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(Intent(Intent.ACTION_CALL, "*103*8*9#".getUSSD()))
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?,
                                                                        token: PermissionToken?) {
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {}
                    }).check()
        }
    }

    fun String.getUSSD(): Uri {
        var uriString = ""
        if (!this.startsWith("tel:")) uriString += "tel:"
        for (c in this.toCharArray()) {
            uriString += if (c == '#') Uri.encode("#") else c
        }
        return Uri.parse(uriString)
    }
}
