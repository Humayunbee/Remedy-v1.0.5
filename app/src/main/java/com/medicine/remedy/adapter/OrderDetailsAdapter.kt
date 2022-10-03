package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.data_model.OrderDetailsModel
import com.medicine.remedy.utils.MathematicsUtils.DOUBLE_VALUE_FORMAT
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlCouponDiscount
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlDeliveryCharge
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlDisplayGrandTotal
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlDisplaySubTotal
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlDisplaySubTotalDiscount
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlGrandTotal
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlSubTotal
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlTotalDiscount

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/7/2021
  * Email: humayunfarid1997@gmail.com *
 * *** Happy Coding ***
 */
open class OrderDetailsAdapter(mContext: Context, items: List<OrderDetailsModel>) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>()
{
    protected var context = mContext
    protected var orderItems : List<OrderDetailsModel> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_details_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : OrderDetailsModel = orderItems[position]

        holder.tvProductName.text = model.productName
        val totalPrice = (model.rate * model.qty)
        holder.tvPrice.text = "${context.resources.getString(R.string.taka)} ${DOUBLE_VALUE_FORMAT(totalPrice)}"
        holder.tvQuantity.text = "${model.qty}"

        calculateAmount()
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }

    open fun calculateAmount()
    {
        var subTotal = 0.00
        var totalDiscount = 0.00

        for(model in orderItems)
        {
            subTotal+=(model.rate * model.qty)
            totalDiscount+=(model.discount * model.qty)
        }

        mlSubTotal.value = subTotal
        mlTotalDiscount.value = totalDiscount + mlCouponDiscount.value!!
        mlGrandTotal.value = subTotal - (totalDiscount + mlCouponDiscount.value!!) + mlDeliveryCharge.value!!

        mlDisplaySubTotal.value = DOUBLE_VALUE_FORMAT(subTotal) + " " + context.resources.getString(R.string.taka)
        mlDisplaySubTotalDiscount.value = DOUBLE_VALUE_FORMAT(mlTotalDiscount.value!!) + " " + context.resources.getString(R.string.taka)
        mlDisplayGrandTotal.value = DOUBLE_VALUE_FORMAT(mlGrandTotal.value!!) + " " + context.resources.getString(R.string.taka)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvProductName: TextView = view.findViewById(R.id.tv_product_name)
        var tvPrice: TextView = view.findViewById(R.id.tv_price)
        var tvQuantity: TextView = view.findViewById(R.id.tv_quantity)
    }
}