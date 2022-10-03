package com.medicine.remedy.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Date 3/15/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
open class KeyboardUtils
{


    companion object{
        fun dpToPx(context: Context, valueInDp: Float): Float {
            val metrics = context.resources.displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
        }

        @JvmStatic fun showKeyboard(mEtSearch: EditText, context: Context) {
            mEtSearch.requestFocus()
            val imm = (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }

        @JvmStatic fun hideSoftKeyboard(mEtSearch: EditText, context: Context) {
            mEtSearch.clearFocus()
            val imm = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            imm.hideSoftInputFromWindow(mEtSearch.windowToken, 0)
        }

        @JvmStatic fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * Disable Soft keyboard from activity in order to show the custom keyboard
         * @param activity - activity
         */
        @JvmStatic fun disableKeyboard(activity: Activity) {
            // hide soft keyboard
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        }

        /**
         * Check keyboard is open or not
         * If keyboard is open hide view will be hide otherwise not
         * @param context, application context
         * @param view, root layout
         * @param hideView, hide view
         */
        @JvmStatic fun checkKeyboardIsOpen(context: Context, view: View, hideView: View) {
            view.viewTreeObserver.addOnGlobalLayoutListener {
                val heightDiff = view.rootView.height - view.height
                if (heightDiff > dpToPx(context, 200f)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    hideView.visibility = View.GONE
                } else hideView.visibility = View.VISIBLE
            }
        }

        /**
         * Check keyboard is open or not
         * @param context application context
         * @param view view
         * @return true or false
         */
        @JvmStatic fun checkIsKeyboardOpen(context: Context, view: View): Boolean {
            val isOpen = AtomicBoolean(false)
            view.viewTreeObserver.addOnGlobalLayoutListener {
                val heightDiff = view.rootView.height - view.height
                if (heightDiff > dpToPx(context, 200f)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    isOpen.set(true)
                } else isOpen.set(false)
            }
            return isOpen.get()
        }
    }
}