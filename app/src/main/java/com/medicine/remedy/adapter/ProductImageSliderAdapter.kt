package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.medicine.remedy.R
import com.medicine.remedy.utils.ImageUtils
import com.smarteist.autoimageslider.SliderViewAdapter

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/2/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ProductImageSliderAdapter (mContext: Context, imageUrl: List<String>) : SliderViewAdapter<ProductImageSliderAdapter.SliderAdapterVH>()
{
    private var context: Context = mContext
    private var imageUrls: List<String> = imageUrl

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        @SuppressLint("InflateParams")
        val inflate: View = LayoutInflater.from(context).inflate(R.layout.model_slider_image, parent, false)
        return SliderAdapterVH(inflate)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val slideModel : String = imageUrls[position]
        ImageUtils.displayImage(context as Activity, viewHolder.ivSliderImage, slideModel)
    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    open class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var ivSliderImage : ImageView = itemView.findViewById(R.id.iv_slider_image)
    }
}