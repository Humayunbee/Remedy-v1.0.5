package com.medicine.remedy.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.utils.AppConstants.Companion.ERROR_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.INFO_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.SUCCESS_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.WARNING_DIALOG
import com.medicine.remedy.view.activity.NotificationActivity
import com.medicine.remedy.view.activity.SettingActivity


/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/3/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ViewUtils
{
    companion object{

        fun horizontalRecyclerView(context: Context, recyclerview: RecyclerView) : RecyclerView
        {
            val layoutManager = LinearLayoutManager(context)
            recyclerview.setHasFixedSize(true)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            recyclerview.layoutManager = layoutManager
            recyclerview.itemAnimator = DefaultItemAnimator()

            return recyclerview
        }

        fun horizontalMultiNumberItemRecyclerView(context: Context, recyclerview: RecyclerView, column: Int) : RecyclerView
        {
            recyclerview.setHasFixedSize(true)
            recyclerview.layoutManager = GridLayoutManager(context, column)
            recyclerview.itemAnimator = DefaultItemAnimator()

            return recyclerview
        }

        fun verticalRecyclerView(context: Context, recyclerview: RecyclerView) : RecyclerView
        {
            val layoutManager = LinearLayoutManager(context)
            recyclerview.setHasFixedSize(true)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerview.layoutManager = layoutManager
            recyclerview.itemAnimator = DefaultItemAnimator()

            return recyclerview
        }

        /**
         * ...click on toolbar menu item
         * ...start new activity
         */
        fun onClickToolbarMenu(activity : Activity, view : View)
        {
            val ivNotification = view.findViewById<ImageView>(R.id.iv_notification)
            val ivMenu = view.findViewById<ImageView>(R.id.iv_menu)

            ivNotification.setOnClickListener {
                ExtraUtils.startActivityIn(activity, Intent(activity, NotificationActivity::class.java))
            }

            ivMenu.setOnClickListener {
                ExtraUtils.startActivityIn(activity, Intent(activity, SettingActivity::class.java))
            }
        }

        /**
         * @ Display custom toast view for success message
         * @param text , text which is display on toast view
         * @param context, Application context
         */
        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        @JvmStatic
        fun showToast(context: Activity, text: String, toastType: Int) {
            val inflater = (context).layoutInflater
            val layout = inflater.inflate(
                R.layout.layout_toast_view,
                context.findViewById(R.id.toast_layout_root)
            )
            val textView = layout.findViewById<TextView>(R.id.tv_message)
            val tvTitle = layout.findViewById<TextView>(R.id.tv_title)
            val imgIcon = layout.findViewById<ImageView>(R.id.iv_icon)
            textView.text = text
            imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_info_toast))
            when (toastType) {
                SUCCESS_DIALOG -> {
                    tvTitle.text = "SUCCESS"
                    imgIcon.background = context.getResources().getDrawable(R.drawable.bg_toast_icon)
                    getDrawableBackgroundColor(context, imgIcon, R.color.success_bg_color)
                    imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_success_toast_icon))
                    tvTitle.setTextColor(context.getResources().getColor(R.color.success_color))
                }
                WARNING_DIALOG -> {
                    tvTitle.text = "WARNING"
                    imgIcon.background = context.getResources().getDrawable(R.drawable.bg_toast_icon)
                    getDrawableBackgroundColor(context, imgIcon, R.color.warning_bg_color)
                    imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_warning_toast))
                    tvTitle.setTextColor(context.getResources().getColor(R.color.warning_color))
                }
                ERROR_DIALOG -> {
                    tvTitle.text = "ERROR"
                    imgIcon.background = context.getResources().getDrawable(R.drawable.bg_toast_icon)
                    getDrawableBackgroundColor(context, imgIcon, R.color.error_bg_color)
                    imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_error_toast))
                    tvTitle.setTextColor(context.getResources().getColor(R.color.error_color))
                }
                INFO_DIALOG -> {
                    tvTitle.text = "INFO"
                    imgIcon.background = context.getResources().getDrawable(R.drawable.bg_toast_icon)
                    getDrawableBackgroundColor(context, imgIcon, R.color.info_bg_color)
                    imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_info_toast))
                    tvTitle.setTextColor(context.getResources().getColor(R.color.info_color))
                }
            }
            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, 0, 20)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }

        private fun getDrawableBackgroundColor(mContext: Context, mView: View, color: Int) {
            val gradientDrawable = mView.background as GradientDrawable
            gradientDrawable.setColor(mContext.resources.getColor(color))
            gradientDrawable.setStroke(0, mContext.resources.getColor(color))
            mView.invalidate()
        }
    }
}