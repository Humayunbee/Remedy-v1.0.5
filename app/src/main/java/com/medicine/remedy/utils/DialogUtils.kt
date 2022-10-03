package com.medicine.remedy.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.adapter.SubDistrictSelectionAdapter
import com.medicine.remedy.data_model.SubDistrictModel
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.ViewUtils.Companion.verticalRecyclerView
import java.util.*

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 3/31/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class DialogUtils
{
    companion object{
        /**
         * ...Display no internet connection error
         * @param activity application context
         * @param callBack resendRequestCall object
         * @return alert dialog object
         */
        @JvmStatic
        fun noInternetDialogFullScreen(activity: Activity, callBack: OnRetryCallback): AlertDialog {
            @SuppressLint("InflateParams")
            val view: View = LayoutInflater.from(activity).inflate(
                R.layout.dialog_no_internet_connection,
                null
            )
            val alertDialog: AlertDialog
            val tvButton = view.findViewById<TextView>(R.id.tv_retry)
            val builder: AlertDialog.Builder = AlertDialog.Builder(
                activity,
                R.style.FullScreenWithStatusBar
            )
            builder.setView(view)
            builder.setCancelable(false)
            alertDialog = builder.create()
            tvButton.setOnClickListener { view1: View ->
                callBack.onRetry()
                alertDialog.dismiss()
            }
            return alertDialog
        }

        /**
         * ...Show SubDistrictModel selection dialog selection dialog
         * ...load item list in recycler view
         * ...search SubDistrictModel from recycler view
         * ...handle search view
         * @param activity  activity context
         * @param editText  editText view object
         * @param models item list model
         */
        @SuppressLint("SetTextI18n")
        fun subDistrictSelectionDialog(activity: Activity, editText: EditText, models: List<SubDistrictModel>) {
            @SuppressLint("InflateParams") val view = LayoutInflater.from(activity).inflate(R.layout.dialog_selection, null)
            val alertDialog: AlertDialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder.setView(view)
            builder.setCancelable(false)
            val ivCancel = view.findViewById<ImageView>(R.id.iv_cancel)
            val tvTitle = view.findViewById<TextView>(R.id.tv_title)
            val recyclerView: RecyclerView = view.findViewById(R.id.rv_list)
            val etSearch = view.findViewById<EditText>(R.id.et_search)
            val searchView: SearchView = view.findViewById(R.id.sv_search)
            tvTitle.text = "Select Micro union"

            alertDialog = builder.create()
            Objects.requireNonNull(alertDialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show() // show the dialog

            alertDialog.setOnDismissListener {
                etSearch.setText("")
            }

            val adapter = SubDistrictSelectionAdapter(models, editText, alertDialog)
            verticalRecyclerView(activity,recyclerView).adapter = adapter


            //search field
            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    val text = etSearch.text.toString()
                    searchView.setQuery(text, false)
                }
            })

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    adapter.filterSubDistrictList(newText)
                    return false
                }
            })
            ivCancel.setOnClickListener { v: View? -> alertDialog.dismiss() }
        }
    }

}