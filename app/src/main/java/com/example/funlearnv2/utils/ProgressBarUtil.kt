package com.example.funlearnv2.utils

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.example.funlearnv2.R

fun ProgressBar.startProgress() {
    this.visibility = View.VISIBLE
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.indeterminateDrawable =
            ContextCompat.getDrawable(context, R.drawable.animated_progressbar)
        val anim = this.indeterminateDrawable as AnimatedVectorDrawable
        anim.start()
        anim.registerAnimationCallback(
            object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    anim.start()
                }
            }
        )
    }
}

fun ProgressBar.endProgress() {
    if (this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val anim = this.indeterminateDrawable as AnimatedVectorDrawable
            anim.stop()
        }
    }
}
