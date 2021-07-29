package com.worldwidemobiledevelopment.recipes.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.worldwidemobiledevelopment.recipes.Application
import com.worldwidemobiledevelopment.recipes.R
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class Utils {

    @Inject
    lateinit var context: Context

    init {
        Application.application.appComponent.inject(this)
    }

    companion object {
        private val context = Utils().context

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
            screenDimensions[0] =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) x else y // width
            screenDimensions[1] =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) y else x // height
            return screenDimensions
        }

        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showSnackBar(
            view: View,
            text: String,
            anchorView: Int = R.id.bottom_nav,
            duration: Int = Snackbar.LENGTH_LONG
        ) {
            Snackbar.make(view, text, duration)
                .show()
        }

        fun showSnackBarWithAction(
            view: View,
            text: String,
            actionText: String,
            anchorView: Int?,
            duration: Int = Snackbar.LENGTH_INDEFINITE,
            listener: View.OnClickListener
        ): Snackbar {
            val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
                .setAction(actionText, listener)
            if (anchorView != null) {
                snackbar.setAnchorView(anchorView)
            }
            return snackbar
        }

        fun Int.toDp(): Int {
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            return (this / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
        }

        fun Int.toPx(): Int {
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            return (this * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
        }

        fun stringFromTemplate(path: Int, vararg parameter: Any): String {
            return String.format(context.resources.getString(path), parameter)
        }

        fun changeStatusBarColor(activity: Activity, color: Int) {
            val window: Window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }

        fun hideBottomNavigation(activity: Activity, bottomNavigationId: Int = R.id.bottom_nav) {
            activity.findViewById<BottomNavigationView>(bottomNavigationId).visibility = View.GONE
        }

        fun showBottomNavigation(activity: Activity, bottomNavigationId: Int = R.id.bottom_nav) {
            activity.findViewById<BottomNavigationView>(bottomNavigationId).visibility =
                View.VISIBLE
        }

        fun getUnixTime() : Long{
            return System.currentTimeMillis()/1000
        }

    }

}