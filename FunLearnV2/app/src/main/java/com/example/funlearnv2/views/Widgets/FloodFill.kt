package com.example.funlearnv2.views.Widgets

import android.graphics.Bitmap
import android.graphics.Point
import java.util.*

object FloodFill {
    fun floodFill(bitmap: Bitmap, point: Point, targetColor: Int, newColor: Int) {
        var point = point
        val width = bitmap.width
        val height = bitmap.height
        if (targetColor != newColor) {
            val queue: Queue<Point> = LinkedList()
            do {
                var x = point.x
                val y = point.y
                while (x > 0 && bitmap.getPixel(x - 1, y) == targetColor) {
                    x--
                }
                var spanUp = false
                var spanDown = false
                while (x < width && bitmap.getPixel(x, y) == targetColor) {
                    bitmap.setPixel(x, y, newColor)
                    if (!spanUp && y > 0 && bitmap.getPixel(x, y - 1) == targetColor) {
                        queue.add(Point(x, y - 1))
                        spanUp = true
                    } else if (spanUp && y > 0 && bitmap.getPixel(x, y - 1) != targetColor) {
                        spanUp = false
                    }
                    if (!spanDown && y < height - 1 && bitmap.getPixel(x, y + 1) == targetColor) {
                        queue.add(Point(x, y + 1))
                        spanDown = true
                    } else if (spanDown && y < height - 1 && bitmap.getPixel(x, y + 1) != targetColor) {
                        spanDown = false
                    }
                    x++
                }
            } while (queue.poll().also { point = it } != null)
        }
    }
}