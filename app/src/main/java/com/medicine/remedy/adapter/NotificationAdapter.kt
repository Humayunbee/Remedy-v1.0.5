package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.data_model.NotificationModel
import com.medicine.remedy.utils.DateTimeUtils.Companion.timeAgo
import com.medicine.remedy.utils.ExtraUtils.Companion.htmlToPlainText

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/9/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class NotificationAdapter(private val onItemClick: (item: NotificationModel) -> Unit) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>()
{
    var items = listOf<NotificationModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_notification, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : NotificationModel = items[position]
        holder.itemView.setOnClickListener { onItemClick(model) }

        holder.tvDate.text = timeAgo(model.createdAt)
        holder.tvTitle.text = model.title
        holder.tvBody.text = htmlToPlainText(model.description)

        holder.ivRead.isVisible = model.readStatus == null
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tv_title)
        var tvBody: TextView = view.findViewById(R.id.tv_body)
        var tvDate: TextView = view.findViewById(R.id.tv_date)
        var ivRead: ImageView = view.findViewById(R.id.iv_read_icon)
    }
}
