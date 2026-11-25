package com.abd.universal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color
import android.view.Gravity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(15, 15, 22)
        window.navigationBarColor = Color.rgb(15, 15, 22)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
            setBackgroundColor(Color.rgb(15, 15, 22))
        }
        val title = TextView(this).apply {
            text = "ABD UNIVERSAL"
            textSize = 26f
            setTextColor(Color.rgb(190, 150, 255))
            gravity = Gravity.CENTER
        }
        val info = TextView(this).apply {
            text = "Minimal overlay starter\nEnable display-over-other-apps permission, then launch the panel."
            textSize = 14f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 18, 0, 22)
        }
        val permission = Button(this).apply {
            text = "Grant overlay permission"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")))
            }
        }
        val launch = Button(this).apply {
            text = "Open ABD panel"
            setOnClickListener {
                if (Settings.canDrawOverlays(this@MainActivity)) {
                    startService(Intent(this@MainActivity, OverlayService::class.java))
                } else {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")))
                }
            }
        }
        layout.addView(title)
        layout.addView(info)
        layout.addView(permission)
        layout.addView(launch)
        setContentView(layout)
    }

    // Controller key events can be handled while this activity has focus only.
    // Android apps cannot globally capture Quest controller X over another VR game.
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BUTTON_X) {
            if (Settings.canDrawOverlays(this)) {
                startService(Intent(this, OverlayService::class.java))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
