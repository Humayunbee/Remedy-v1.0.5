package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.model.BrandModel
import java.util.*
import kotlin.collections.ArrayList

class BrandAdapter(items : List<BrandModel>) : RecyclerView.Adapter<BrandAdapter.ViewHolder>()
{
    private var brandList : ArrayList<BrandModel> = items as ArrayList<BrandModel>
    private var filterList =  ArrayList<BrandModel>()

    init {
        filterList.addAll(brandList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_company_name, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : BrandModel = brandList[position]
        holder.tvBrandName.text = model.brandName
        holder.cbSelection.isChecked = model.isSelected

        holder.itemView.setOnClickListener {
            model.isSelected = !model.isSelected
            notifyDataSetChanged()
        }

        holder.cbSelection.setOnClickListener {
            model.isSelected = !model.isSelected
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return brandList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvBrandName: TextView = view.findViewById(R.id.tv_brand_name)
        var cbSelection: CheckBox = view.findViewById(R.id.cb_selection)
    }

    fun filterList(text: String) {
        val searchKey = text.toLowerCase(Locale.getDefault())
        brandList.clear()
        if (searchKey.isEmpty()) {
            brandList.addAll(filterList)
        } else {
            for (model in filterList) {
                if (model.brandName.toLowerCase(Locale.getDefault()).contains(searchKey))
                    brandList.add(model)
            }
        }
        notifyDataSetChanged()
    }
}