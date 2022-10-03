package com.medicine.remedy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.data_model.SubDistrictModel
import com.medicine.remedy.view_model.UserInformationViewModel.Companion.mlSubDistrictId
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class SubDistrictSelectionAdapter(items: List<SubDistrictModel>, editText: EditText, dialog: AlertDialog) : RecyclerView.Adapter<SubDistrictSelectionAdapter.ViewHolder>() {
    private val subDistrictList: ArrayList<SubDistrictModel> = items as ArrayList<SubDistrictModel>
    private var filterSubDistrictList = ArrayList<SubDistrictModel>()
    private val textView: EditText = editText
    private val alertDialog: AlertDialog = dialog

    init {
        filterSubDistrictList.addAll(subDistrictList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_selection_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItem.text = subDistrictList[position].name

        holder.itemView.setOnClickListener { v: View? ->
            textView.setText(subDistrictList[position].name)
            mlSubDistrictId.value = subDistrictList[position].subDistrictId
            alertDialog.dismiss() //dismiss alert dialog
        }
    }

    override fun getItemCount(): Int {
        return subDistrictList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItem: TextView = itemView.findViewById(R.id.tv_item)
    }

    // Filter district
    fun filterSubDistrictList(text: String) {
        var text = text
        text = text.toLowerCase(Locale.getDefault())
        subDistrictList.clear()
        if (text.isEmpty()) {
            subDistrictList.addAll(filterSubDistrictList)
        } else {
            for (model in filterSubDistrictList) {
                if (model.name.toLowerCase(Locale.getDefault()).contains(text)) {
                    subDistrictList.add(model)
                }
            }
        }
        notifyDataSetChanged()
    }
}