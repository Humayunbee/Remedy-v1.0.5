package com.medicine.remedy.config

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.medicine.remedy.interfaces.InitComponent
import com.medicine.remedy.utils.AppConstants.Companion.NO_INTERNET_CONNECTION
import com.medicine.remedy.utils.DataValidation
import com.medicine.remedy.utils.LoadingUtils

/**
 * Date 1/3/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
abstract class BaseFragment : Fragment() , InitComponent
{
    protected lateinit var spManager : SharedPreferenceManager
    protected lateinit var loadingUtils: LoadingUtils
    protected lateinit var layoutManager: LinearLayoutManager
    protected lateinit var dataValidation: DataValidation

    protected lateinit var loadingDialog : AlertDialog
    protected lateinit var mContext : Context
    protected lateinit var mActivity : Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setToolbarTitle(title: String) { }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * ...initialize all component
     * ...create object
     * ...call some necessary method
     */
    abstract override fun initialize()

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
     * ...Display a snackBar
     * ...If device is not connected to the internet, this snackBar will be displayed
     * @param view root view
     */
    fun noInternetConnection(view: View)
    {
        Snackbar.make(view, NO_INTERNET_CONNECTION, Snackbar.LENGTH_LONG).show()
    }

    /**
     * ...Display a snackBar
     * ...If any error is encountered, then show this SnackBar
     * @param view root view
     */
    fun errorSnackBar(view: View, message: String)
    {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    /**
     * ...disable button
     * @param button button view
     */
    protected open fun onButtonDisable(button: Button) {
        button.isClickable = false
    }

    /**
     * ...disable button
     * @param button button view
     */
    protected open fun onButtonDisable(button: TextView) {
        button.isClickable = false
    }

    /**
     * ...enable button
     * @param button button view
     */
    protected open fun onButtonEnable(button: Button) {
        button.isClickable = true
    }

    /**
     * ...enable button
     * @param button button view
     */
    protected open fun onButtonEnable(button: TextView) {
        button.isClickable = true
    }
}