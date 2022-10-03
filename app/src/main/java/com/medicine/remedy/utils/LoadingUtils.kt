package com.medicine.remedy.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import com.medicine.remedy.R


/**
 * Date 12/26/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class LoadingUtils(context: Context)
{
    private var progressDialog = ProgressDialog(context)

    init {
        progressDialog.setMessage("Loading...")
        progressDialog.setTitle("Please wait")
    }

    /**
     * @ Display progress dialog
     */
    fun showProgressDialog() {
        progressDialog.show()
    }

    /**
     * @ Display progress dialog
     */
    fun progressDialog(): ProgressDialog {
        return progressDialog
    }

    /**
     * Display progress dialog with title and message
     * @param title progress title
     * @param message progress message
     */
    fun showProgressDialog(title: String?, message: String?) {
        //progressDialog.setCancelable(false);
        progressDialog.setTitle(title)
        progressDialog.setMessage(message)
        progressDialog.show()
    }

    /**
     *
     * @ Dismiss progress dialog
     */
    fun dismissProgressDialog() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }

    companion object{
        fun loadingView(context: Context) : AlertDialog{
            @SuppressLint("InflateParams")
            val view: View = LayoutInflater.from(context).inflate(R.layout.layout_loading_view,null)
            val alertDialog: AlertDialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(context,R.style.FullScreenWithStatusBar)
            builder.setView(view)
            builder.setCancelable(false)
            alertDialog = builder.create()
            //alertDialog.show()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            return alertDialog
        }
    }
}