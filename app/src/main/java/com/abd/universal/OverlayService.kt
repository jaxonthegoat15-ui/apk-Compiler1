package com.abd.universal

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class OverlayService : Service() {
    private lateinit var wm: WindowManager
    private var panel: View? = null

    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        showPanel()
    }

    private fun showPanel() {
        if (panel != null) return
        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 14, 18, 14)
            setBackgroundColor(Color.rgb(22, 22, 32))
        }
        val header = TextView(this).apply {
            text = "ABD UNIVERSAL     ⋮"
            textSize = 19f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(190, 150, 255))
            setPadding(4, 4, 4, 14)
        }
        // Drag the panel by holding its header.
        header.setOnTouchListener(DragListener())
        outer.addView(header)

        val tabs = listOf("MOVEMENT", "PLAYER", "VISUALS", "FUN", "SETTINGS")
        tabs.forEach { tab ->
            val label = TextView(this).apply {
                text = "— $tab —"
                textSize = 12f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.rgb(190, 150, 255))
                setPadding(4, 12, 4, 5)
            }
            outer.addView(label)
            val description = when (tab) {
                "MOVEMENT" -> "Game-specific movement modules appear here."
                "PLAYER" -> "Player options appear here."
                "VISUALS" -> "Visual options appear here."
                "FUN" -> "Safe cosmetic options appear here."
                else -> "Theme, scale and configuration."
            }
            outer.addView(TextView(this).apply {
                text = description
                textSize = 13f
                setTextColor(Color.LTGRAY)
                setPadding(4, 2, 4, 4)
            })
        }
        outer.addView(TextView(this).apply {
            text = "CLOSE"
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setPadding(12, 14, 12, 8)
            setOnClickListener { stopSelf() }
        })

        val scroll = ScrollView(this).apply { addView(outer) }
        val params = WindowManager.LayoutParams(
            430, 560,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
            x = 0
            y = 0
        }
        panel = scroll
        try {
            wm.addView(scroll, params)
        } catch (_: Exception) {
            panel = null
            stopSelf()
        }
    }

    private inner class DragListener : View.OnTouchListener {
        private var startX = 0
        private var startY = 0
        private var touchX = 0f
        private var touchY = 0f
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val view = panel ?: return false
            val lp = view.layoutParams as WindowManager.LayoutParams
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = lp.x; startY = lp.y
                    touchX = event.rawX; touchY = event.rawY
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    lp.x = startX + (event.rawX - touchX).toInt()
                    lp.y = startY + (event.rawY - touchY).toInt()
                    wm.updateViewLayout(view, lp)
                    return true
                }
            }
            return false
        }
    }

    override fun onDestroy() {
        panel?.let { try { wm.removeView(it) } catch (_: Exception) {} }
        panel = null
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
