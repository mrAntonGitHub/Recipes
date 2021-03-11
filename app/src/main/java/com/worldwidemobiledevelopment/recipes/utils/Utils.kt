package com.worldwidemobiledevelopment.recipes.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.util.TypedValue
import android.view.WindowManager


class Utils {

    companion object{
        fun getScreenSize(context: Context): IntArray {
            // return Array where width[0], height[1]
            val screenDimensions = IntArray(2)
            val x: Int
            val y: Int
            val orientation = context.resources.configuration.orientation
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val screenSize = Point()
            display.getRealSize(screenSize)
            x = screenSize.x
            y = screenSize.y
            screenDimensions[0] = if (orientation == Configuration.ORIENTATION_PORTRAIT) x else y // width
            screenDimensions[1] = if (orientation == Configuration.ORIENTATION_PORTRAIT) y else x // height
            return screenDimensions
        }
    }

}