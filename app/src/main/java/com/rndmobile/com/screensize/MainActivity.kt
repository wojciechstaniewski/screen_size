package com.rndmobile.com.screensize

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var x: Double = 0.0

    var h: Int = 0

    var y: Double = 0.0

    var W: Double = 0.0

    var size: Float = 0.0f

    var dm: DisplayMetrics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        size = getDisplaySize(this)
        textInfo.text = String.format("Wielkość ekranu W = %f H = %d \n Maxymalna Gęstość ekanu = %d \n Przekątna ekranu = %f", W, h, dm!!.densityDpi, size)
    }


    fun getDisplaySize(context: Context): Float {
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        W = dm!!.widthPixels.toDouble()
        x = Math.pow(W, 2.0)
        val navH = getNavBarHeight(context)
        h = dm!!.heightPixels + navH
        y = Math.pow(h.toDouble(), 2.0)

        val factor: Float = if (h > 1280) {
            dm!!.xdpi
        } else {
            if(navH > 0){
                dm!!.densityDpi.toFloat()
            } else {
                dm!!.xdpi
            }
        }

        return (Math.sqrt(x + y) / factor).toFloat()
    }

    fun getNavBarHeight(c: Context): Int {
        val result = 0
        val hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)

        if (!hasMenuKey && !hasBackKey) {
            //The device has a navigation bar
            val resources = c.resources

            val orientation = resources.configuration.orientation
            val resourceId: Int
            if (isTablet(c)) {
                resourceId = resources.getIdentifier(if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_height_landscape", "dimen", "android")
            } else {
                resourceId = resources.getIdentifier(if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_width", "dimen", "android")
            }

            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    private fun isTablet(c: Context): Boolean {
        return c.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }
}
