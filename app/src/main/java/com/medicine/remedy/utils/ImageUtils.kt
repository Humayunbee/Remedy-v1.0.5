package com.medicine.remedy.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.medicine.remedy.R
import com.medicine.remedy.utils.AppConstants.Companion.baseImageUrl

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/1/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ImageUtils
{
    companion object{

        /**
         * ...display image on image view
         */
        @SuppressLint("UseCompatLoadingForDrawables")
        fun displayImage(activity: Activity, imageView: ImageView, imageUrl: String)
        {
            Glide.with(activity)
                    .load("$baseImageUrl$imageUrl")
                    .error(activity.resources.getDrawable(R.drawable.logo))
                    .placeholder(activity.resources.getDrawable(R.drawable.logo))
                    .into(imageView)
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun displayImage(activity: Activity, imageView: ImageView, imageUrl: String, name : String )
        {
            Glide.with(activity)
                .load(imageUrl)
                .placeholder(activity.resources.getDrawable(R.drawable.logo))
                .into(imageView)
        }
    }
}