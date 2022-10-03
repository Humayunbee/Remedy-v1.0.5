package com.medicine.remedy.config

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.medicine.remedy.R
import com.medicine.remedy.interfaces.InitComponent
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.AppConstants.Companion.NO_INTERNET_CONNECTION
import com.medicine.remedy.utils.LoadingUtils
import com.google.android.material.snackbar.Snackbar
import io.github.inflationx.viewpump.ViewPumpContextWrapper



/**
 * Date 12/22/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
abstract class BaseActivity : AppCompatActivity() , InitComponent
{
    protected lateinit var spManager : SharedPreferenceManager
    protected lateinit var views : List<View>

    protected lateinit var alertDialog : AlertDialog
    protected lateinit var progressDialog : LoadingUtils

    protected lateinit var mContext : Context
    protected lateinit var mActivity : Activity
    protected lateinit var loadingUtils: LoadingUtils
    protected lateinit var loadingDialog : AlertDialog
    protected lateinit var layoutManager: LinearLayoutManager

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.animation_slide_in_left, R.anim.animation_slide_out_right)
    }

    /**
     * ...initialize all component
     * ...create object
     * ...call some necessary method
     */
    abstract override fun initialize()

    abstract override fun setToolbarTitle(title: String)

    /**
     * ...load fragment in activity
     * ...set animation on transaction
     * ...transaction commit
     */
    fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        //transaction.setCustomAnimations(R.anim.animation_slide_in_left, R.anim.animation_slide_out_right, R.anim.animation_slide_in_right, R.anim.animation_slide_out_left)
        transaction.replace(R.id.fl_container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    /**
     * ...load fragment in activity
     * ...set animation on transaction
     * ...transaction commit
     */
    fun showFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.animation_slide_in_right,
            R.anim.animation_slide_out_left,
            R.anim.animation_slide_in_left,
            R.anim.animation_slide_out_right
        )
        fragmentTransaction.replace(R.id.fl_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    /**
     * ...set toolbar title
     * ...modify title and textView if needed
     * @param title toolbar title
     * @param tvTitle textView object
     */
    fun setToolbarTitle(title: String, tvTitle: TextView)
    {
        tvTitle.text = title
    }

    /**
     * ...Display a snackBar
     * ...If device is not connected to the internet, this snackBar will be displayed
     * @param view root view
     */
    fun noInternetConnection(view: View)
    {
        Snackbar.make(view, NO_INTERNET_CONNECTION, Snackbar.LENGTH_LONG).show()
    }

    /**
     * ...if internet connection is not available
     * ...it will be show
     * @param view view id
     * @param resendRequest resend request object
     */
    open fun retrySnackBar(view: View, resendRequest: OnRetryCallback) {
        val snackBar: Snackbar = Snackbar.make(
            view,
            NO_INTERNET_CONNECTION,
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("Retry") { v: View? ->
            resendRequest.onRetry()
            snackBar.dismiss()
        }
        snackBar.show()
    }

    /**
     * ...only visible on warning
     * ...or if any error occur
     */
    fun warningSnackBar(view: View, warning: String)
    {
        Snackbar.make(view, warning, Snackbar.LENGTH_LONG).show()
    }

    /**
     * ...change icon background on click bottom menu.
     * ...check android os version
     * ...set selected background for selected menu and null for other
     * @param view icon view
     */
    fun onSelectBottomMenu(view: View,views : List<View>)
    {
        val sdk : Int = android.os.Build.VERSION.SDK_INT
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {//check sdk version
            for(v in views)
            {
                if(v.id == view.id)
                    view.setBackgroundResource(R.drawable.bg_selected_bottom_menu) //set selected menu background
                else
                    v.setBackgroundDrawable(null)//set empty menu background
            }
        }else {
            for(v in views)
            {
                if(v.id == view.id)
                    view.background = (ContextCompat.getDrawable(this, R.drawable.bg_selected_bottom_menu)) //set selected menu background
                else
                    v.background = null//set empty menu background
            }
        }
    }


    /**
     * setting listener to get callback for load more
     */
    protected open fun setUpLoadMoreListener() {}


    protected var pageNumber = 1
    protected var lastVisibleItem = 0
    protected  var totalItemCount:Int = 0
    protected val VISIBLE_THRESHOLD = 1
    protected var isAnyMoreDataAvailable = true


    /**
     * Override Method to active calligraphy font in this activity
     * @param newBase - activity base
     */
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
}