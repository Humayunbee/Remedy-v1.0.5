package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.view.activity.ProductListActivity
import com.medicine.remedy.view_model.DashboardViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 9/11/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class SelectCharacterAdapter(mContext: Context, items: List<String>) : RecyclerView.Adapter<SelectCharacterAdapter.ViewHolder>()
{
    private var context = mContext
    private var alphabets : List<String> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_character, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = alphabets[position]
        holder.tvText.text = model

        holder.itemView.setOnClickListener {
            DashboardViewModel.productSearchKeyword.value = model
            val intent = Intent(context as Activity, ProductListActivity::class.java)
            intent.putExtra(AppConstants.fragmentId, AppConstants.productList)
            intent.putExtra(AppConstants.categoryId, "")
            intent.putExtra(AppConstants.isFilterApply, "0")
            intent.putExtra(AppConstants.toolbarTitle, "Products")
            ExtraUtils.startActivityIn(context as Activity, intent)
        }

    }

    override fun getItemCount(): Int {
        return alphabets.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvText: TextView = view.findViewById(R.id.tv_text)
    }
}
