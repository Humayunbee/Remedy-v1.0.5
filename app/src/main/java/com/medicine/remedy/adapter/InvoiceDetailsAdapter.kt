package com.medicine.remedy.adapter

import android.content.Context
import com.medicine.remedy.R
import com.medicine.remedy.data_model.OrderDetailsModel
import com.medicine.remedy.utils.MathematicsUtils
import com.medicine.remedy.view_model.OrderHistoryViewModel
import com.medicine.remedy.view_model.OrderHistoryViewModel.Companion.mlCouponDiscount

/**
 * Date 9/12/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class InvoiceDetailsAdapter(mContext: Context, items: List<OrderDetailsModel>) : OrderDetailsAdapter(mContext, items)
{
    override fun calculateAmount()
    {
        var subTotal = 0.00
        var totalDiscount = 0.00

        for(model in orderItems)
        {
            subTotal+=(model.rate * model.qty)
            totalDiscount+=(model.discount * model.qty)
        }
        OrderHistoryViewModel.mlDisplaySubTotal.value = MathematicsUtils.DOUBLE_VALUE_FORMAT(subTotal) + " " + context.resources.getString(R.string.taka)
        val discount = totalDiscount + mlCouponDiscount.value!!
        OrderHistoryViewModel.mlDisplaySubTotalDiscount.value = MathematicsUtils.DOUBLE_VALUE_FORMAT(discount) + " " + context.resources.getString(R.string.taka)
        OrderHistoryViewModel.mlDisplayGrandTotal.value = MathematicsUtils.DOUBLE_VALUE_FORMAT(subTotal - discount) + " " + context.resources.getString(R.string.taka)
    }
}