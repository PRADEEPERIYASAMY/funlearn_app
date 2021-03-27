package com.example.funlearnv2.views.widgets
/*

import android.content.Context
import android.graphics.*
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.taskfour.Widget.FloodFill
import com.example.taskfour.PaintingTwoActivity
import java.io.IOException
import java.net.URL
import java.util.*

class ColorView(private val context: Context, attrs: AttributeSet?) : View(context, attrs) {
    var bitmap: Bitmap? = null
    var defaultBitmap: Bitmap? = null
    private val listAction = ArrayList<Bitmap>()
    private val mCanvas: Canvas? = null
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (PaintingTwoActivity.fileId != null) {
            try {
                val bitmaptemp = MediaStore.Images.Media.getBitmap(context.contentResolver, PaintingTwoActivity.fileId)
                bitmap = bitmaptemp.copy(Bitmap.Config.ARGB_8888, true)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            if (bitmap == null) {
                try {
                    val url = URL(PaintingTwoActivity.imageid)
                    val srcBitmap = BitmapFactory.decodeStream(url.openStream())
                    bitmap = Bitmap.createScaledBitmap(srcBitmap, w, h, false)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                for (i in 0 until bitmap!!.width) {
                    for (j in 0 until bitmap!!.height) {
                        val alpha = 255 - brightness(bitmap!!.getPixel(i, j))
                        if (alpha < 200) {
                            bitmap!!.setPixel(i, j, Color.WHITE)
                        } else {
                            bitmap!!.setPixel(i, j, Color.BLACK)
                        }
                    }
                }
                if (defaultBitmap == null) {
                    defaultBitmap = Bitmap.createBitmap(bitmap!!)
                }
            }
        }
    }

    private fun brightness(color: Int): Int {
        return color shr 16 and 0xff
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        paint(event.x.toInt(), event.y.toInt())
        if (PaintingTwoActivity.mediaPlayer3 != null) {
            PaintingTwoActivity.mediaPlayer3.start()
        }
        return true
    }

    private fun paint(x: Int, y: Int) {
        if (x < 0 || y < 0 || x >= bitmap!!.width || y >= bitmap!!.height) {
            return
        }
        val targetColor = bitmap!!.getPixel(x, y)
        if (targetColor != Color.BLACK) {
            FloodFill.floodFill(bitmap, Point(x, y), targetColor, PaintingTwoActivity.selectedColor)
            addLastAction(Bitmap.createBitmap(getBitmap()))
            invalidate()
        }
    }

    fun getBitmap(): Bitmap {
        this.isDrawingCacheEnabled = true
        this.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(this.drawingCache)
        this.isDrawingCacheEnabled = false
        return bitmap
    }

    fun addLastAction(bitmap: Bitmap) {
        listAction.add(bitmap)
    }

    fun returnLastAction() {
        if (listAction.size > 0) {
            listAction.removeAt(listAction.size - 1)
            bitmap = if (listAction.size > 0) {
                listAction[listAction.size - 1]
            } else {
                Bitmap.createBitmap(defaultBitmap!!)
            }
            invalidate()
        }
    }
}*/
