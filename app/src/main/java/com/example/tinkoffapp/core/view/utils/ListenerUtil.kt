package com.example.tinkoffapp.core.view.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

private const val PRESSED_OPACITY = 0.7f
private const val FULL_OPACITY = 1f

object ListenersUtil {
    @SuppressLint("ClickableViewAccessibility")
    fun onTouch(): View.OnTouchListener {
        return View.OnTouchListener { view: View, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
                view.alpha = PRESSED_OPACITY
            } else if (event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP) {
                view.alpha = FULL_OPACITY
            }
            return@OnTouchListener false
        }
    }
}