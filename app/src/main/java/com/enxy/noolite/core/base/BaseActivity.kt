package com.enxy.noolite.core.base

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.enxy.noolite.R
import com.enxy.noolite.core.utils.extension.withColor
import com.enxy.noolite.core.utils.extension.withColorPrimary
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_base.*


abstract class BaseActivity : AppCompatActivity() {
    internal fun notify(@StringRes message: Int) {
        Snackbar.make(fragmentHolder, message, Snackbar.LENGTH_SHORT)
            .withColorPrimary()
            .show()
    }

    internal fun notify(message: String) {
        Snackbar.make(fragmentHolder, message, Snackbar.LENGTH_SHORT)
            .withColorPrimary()
            .show()
    }

    internal fun notifyError(@StringRes message: Int) {
        Snackbar.make(fragmentHolder, message, Snackbar.LENGTH_SHORT)
            .withColor(ContextCompat.getColor(this, R.color.colorError))
            .show()
    }

    // https://stackoverflow.com/questions/4828636/edittext-clear-focus-on-touch-outside/8766475
    // Clears EditText focus on outside touch. Applies to all EditText in current activity.
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view: View? = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}