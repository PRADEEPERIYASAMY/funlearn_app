package com.example.funlearnv2.views.widgets

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

class PaintView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var btmBackground: Bitmap? = null
    private var btmView: Bitmap? = null
    private var image: Bitmap? = null
    private var captureImage: Bitmap? = null
    private var originalImage: Bitmap? = null
    private var rotateImage: Bitmap? = null
    private val mPaint = Paint()
    private var mPath = Path()
    private var colorBackground = 0
    private var sizeBrush = 0
    private var sizeEraser = 0
    private var mX = 0f
    private var mY = 0f
    private var mCanvas: Canvas? = null
    private val DIFFERENCE_SPACE = 4
    private val listAction = ArrayList<Bitmap>()
    private var leftImage = 50
    private var topImage = 50
    private var toResize = false
    private var refX = 0f
    private var refY = 0f
    private var xCentre = 0
    private var yCentre = 0
    private var xRotate = 0f
    private var yRotate = 0f
    private var angle = 0
    init {
        sizeBrush = 12
        sizeEraser = sizeBrush
        colorBackground = Color.WHITE
        mPaint.color = Color.BLACK
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeWidth = toPx(sizeBrush)

/*Drawable drawable = getResources ().getDrawable ( R.drawable.ic_rotate_right_black_24dp );
        rotateImage = drawableToBitmap(drawable);
        captureImage = BitmapFactory.decodeResource ( getResources (), R.drawable.ic_photo_camera_black_24dp);*/
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        drawable.setBounds(0, 0, c.width, c.height)
        drawable.draw(c)
        return bitmap
    }

    private fun toPx(sizeBrush: Int): Float {
        return sizeBrush * resources.displayMetrics.density
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        btmBackground = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        /*if (PaintingOneActivity.file != null) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, PaintingOneActivity.file)
                btmView = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            btmView = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        }*/
        btmView = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(btmView!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(colorBackground)
        canvas.drawBitmap(btmBackground!!, 0f, 0f, null)
        if (image != null && toMove) {
            drawImage(canvas)
            xCentre = leftImage + image!!.width / 2 - captureImage!!.width / 2
            yCentre = topImage + image!!.height / 2 - captureImage!!.height / 2
            xRotate = leftImage + image!!.width + toPx(10)
            yRotate = topImage - toPx(10)
            canvas.drawBitmap(rotateImage!!, xRotate, yRotate, null)
            canvas.drawBitmap(captureImage!!, xCentre.toFloat(), yCentre.toFloat(), null)
        }
        canvas.drawBitmap(btmView!!, 0f, 0f, null)
    }

    private fun drawImage(canvas: Canvas) {
        val matrix = Matrix()
        matrix.setRotate(angle.toFloat(), image!!.width / 2.toFloat(), image!!.height / 2.toFloat())
        matrix.postTranslate(leftImage.toFloat(), topImage.toFloat())
        canvas.drawBitmap(image!!, matrix, null)
    }

    fun setColorBackground(color: Int) {
        colorBackground = color
        invalidate()
    }

    fun setSizeBrush(sizeBrush: Int) {
        this.sizeBrush = sizeBrush
        mPaint.strokeWidth = toPx(sizeEraser)
    }

    fun setBrushColor(color: Int) {
        mPaint.color = color
    }

    fun setSizeEraser(sizeEraser: Int) {
        this.sizeEraser = sizeEraser
        mPaint.strokeWidth = toPx(sizeEraser)
    }

    fun enableEraser() {
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    fun disableEraser() {
        mPaint.xfermode = null
        mPaint.shader = null
        mPaint.maskFilter = null
    }

    fun addLastAction(bitmap: Bitmap) {
        listAction.add(bitmap)
    }

    fun returnLastAction() {
        if (listAction.size > 0) {
            listAction.removeAt(listAction.size - 1)
            btmView = if (listAction.size > 0) {
                listAction[listAction.size - 1]
            } else {
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            }
            mCanvas = Canvas(btmView!!)
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                refX = x
                refY = y
                if (toMove) {
                    toResize = isToResize(refX, refY)
                    if (refX >= xCentre && refX < xCentre + captureImage!!.width && refY >= yCentre && refY < yCentre + captureImage!!.height) {
                        val newCanvas = Canvas(btmBackground!!)
                        drawImage(newCanvas)
                        invalidate()
                    }
                    if (refX >= xRotate && refX <= xRotate + rotateImage!!.width && refY >= yRotate && refY <= yRotate + rotateImage!!.height) {
                        angle += 45
                        invalidate()
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> if (!toMove) {
                touchMove(x, y)
            } else {
                val nX = event.x
                val nY = event.y
                if (toResize) {
                    var xScale = 0
                    var yScale = 0
                    xScale = if (nX > refX) {
                        (image!!.width + (nX - refX)).toInt()
                    } else {
                        (image!!.width - (nX - refX)).toInt()
                    }
                    yScale = if (nY > refY) {
                        (image!!.height + (nY - refY)).toInt()
                    } else {
                        (image!!.height - (nY - refY)).toInt()
                    }
                    if (xScale > 0 && yScale > 0) {
                        image = Bitmap.createScaledBitmap(originalImage!!, xScale, yScale, false)
                    }
                } else {
                    leftImage += (nX - refX).toInt()
                    topImage += (nY - refY).toInt()
                }
                refX = nX
                refY = nY
                invalidate()
            }
            MotionEvent.ACTION_UP -> if (!toMove) {
                touchUp()
                addLastAction(bitmap)
            }
        }
        return true
    }

    private fun isToResize(refX: Float, refY: Float): Boolean = refX >= leftImage && refX < leftImage + image!!.width && (refY >= topImage && refY <= topImage + 20 || refY >= topImage + image!!.height - 20 && refY <= topImage + image!!.height)

    private fun touchUp() {
        mPath.reset()
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= DIFFERENCE_SPACE || dy >= DIFFERENCE_SPACE) {
            mPath.quadTo(x, y, (x + mX) / 2, (y + mY) / 2)
            mY = y
            mX = x
            mCanvas!!.drawPath(mPath, mPaint)
            invalidate()
        }
    }

    private fun touchStart(x: Float, y: Float) {
        mPath.moveTo(x, y)
        mX = x
        mY = y
    }

    val bitmap: Bitmap
        get() {
            this.isDrawingCacheEnabled = true
            this.buildDrawingCache()
            val bitmap = Bitmap.createBitmap(this.drawingCache)
            this.isDrawingCacheEnabled = false
            return bitmap
        }

    fun setImage(bitmap: Bitmap?) {
        toMove = true
        image = Bitmap.createScaledBitmap(bitmap!!, width / 2, height / 2, true)
        originalImage = image
        invalidate()
    }

    fun clear(bitmap: Bitmap?) {
        mPath = Path()
        val clearPaint = Paint()
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mCanvas!!.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), clearPaint)
    }

    companion object {
        var toMove = false
    }
}
