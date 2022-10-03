package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.data_model.OrderHistoryModel
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.AppConstants.Companion.MMM_DD_YYYY
import com.medicine.remedy.utils.AppConstants.Companion.YYYY_MM_DD
import com.medicine.remedy.utils.AppConstants.Companion.YYYY_MM_DD_HH_MM_SS
import com.medicine.remedy.utils.DateTimeUtils.Companion.dateFormat
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ExtraUtils.Companion.getTintedDrawable
import com.medicine.remedy.utils.MathematicsUtils.DOUBLE_VALUE_FORMAT
import com.medicine.remedy.view.fragment.OrderTrackFragment

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class OrderHistoryAdapter(mContext: Context, items: List<OrderHistoryModel>) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>()
{
    private var context = mContext
    private var historyList : List<OrderHistoryModel> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_order_history, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : OrderHistoryModel = historyList[position]

        holder.tvInvoice.text = "#ORD-${model.orderId}"
        holder.tvOrderDate.text = dateFormat(model.orderDate, YYYY_MM_DD,MMM_DD_YYYY)
        holder.tvTotalBill.text = "Amount: ${DOUBLE_VALUE_FORMAT(model.totalAmount)} ${context.resources.getString(R.string.taka)}"
        if(model.couponCode != null)
            holder.tvCoupon.text = Html.fromHtml("<font color='#FF000000'><b>${model.couponCode}</b></font> coupon is applied")
        else holder.tvCoupon.visibility = GONE
        holder.tvPaymentType.text = Html.fromHtml("Payment type: "+"<font color='#FF000000'><b>COD</b></font>")
        holder.tvAddress.text = if(model.address != null) model.address.toString() else "Unknown address"

        when (model.orderStatus) {
            0 -> {
               // holder.tvStatus.background = getTintedDrawable(context.resources.getDrawable(R.drawable.bg_order_status), context.resources.getColor(R.color.error_color))
                holder.tvStatus.setTextColor(context.resources.getColor(R.color.error_color))
                holder.tvStatus.text = "Cancel"
            }

            1 -> {
               // holder.tvStatus.background = getTintedDrawable(context.resources.getDrawable(R.drawable.bg_order_status), context.resources.getColor(R.color.colorAccent))
                holder.tvStatus.setTextColor(context.resources.getColor(R.color.colorAccent))
                holder.tvStatus.text = "Pending"
            }

            2 -> {
                //holder.tvStatus.background = getTintedDrawable(context.resources.getDrawable(R.drawable.bg_order_status), context.resources.getColor(R.color.success_color))
                holder.tvStatus.setTextColor(context.resources.getColor(R.color.success_color))
                holder.tvStatus.text = "Confirmed"
            }

            3 -> {
                //holder.tvStatus.background = getTintedDrawable(context.resources.getDrawable(R.drawable.bg_order_status), context.resources.getColor(R.color.picked_color))
                holder.tvStatus.setTextColor(context.resources.getColor(R.color.picked_color))
                holder.tvStatus.text = "Picked"
            }

            4 -> {
                //holder.tvStatus.background = getTintedDrawable(context.resources.getDrawable(R.drawable.bg_order_status), context.resources.getColor(R.color.shipped_color))
                holder.tvStatus.setTextColor(context.resources.getColor(R.color.shipped_color))
                holder.tvStatus.text = "Delivered"
            }

            else -> holder.tvStatus.visibility = GONE

        }

        holder.itemView.setOnClickListener {
            val fragment: Fragment = OrderTrackFragment()
            val args = Bundle()
            args.putString(AppConstants.orderId, model.orderId)
            fragment.arguments = args
            ExtraUtils.showFragment(context as FragmentActivity, fragment)

        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvInvoice: TextView = view.findViewById(R.id.tv_invoice)
        var tvOrderDate: TextView = view.findViewById(R.id.tv_order_date)
        var tvAddress: TextView = view.findViewById(R.id.tv_address)
        var tvCoupon: TextView = view.findViewById(R.id.tv_coupon_code)
        var tvPaymentType: TextView = view.findViewById(R.id.tv_payment_type)
        var tvTotalBill: TextView = view.findViewById(R.id.tv_total_bill)
        var tvStatus: TextView = view.findViewById(R.id.tv_status)
    }
}
