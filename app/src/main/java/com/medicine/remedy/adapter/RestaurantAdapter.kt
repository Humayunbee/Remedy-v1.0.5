package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medicine.remedy.R
import com.medicine.remedy.data_model.RestaurantModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/4/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class RestaurantAdapter(mContext: Context, items : List<RestaurantModel>) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>()
{
    private var context = mContext
    private var restaurantList : List<RestaurantModel> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_restaurant, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurantModel = restaurantList[position]
        Glide.with(context)
            .load("https://ajp.com.au/wp-content/uploads/2017/10/43833281_l.jpg")
            .error(context.getDrawable(R.drawable.ic_image_place_holder))
            .placeholder(context.getDrawable(R.drawable.ic_image_place_holder))
            .into(holder.ivImage)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivImage: ImageView = view.findViewById(R.id.iv_image)
    }
}
