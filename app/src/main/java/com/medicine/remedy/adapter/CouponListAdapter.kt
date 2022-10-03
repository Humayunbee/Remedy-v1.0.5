package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.data_model.CouponModel
import com.medicine.remedy.model.OrderRepository
import com.medicine.remedy.utils.AppConstants.Companion.ERROR_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.MM_DD
import com.medicine.remedy.utils.AppConstants.Companion.NO_INTERNET_CONNECTION
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.AppConstants.Companion.YYYY_MM_DD
import com.medicine.remedy.utils.DateTimeUtils.Companion.dateFormat
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.LoadingUtils
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.view_model.OrderViewModel.Companion.selectedCouponModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class CouponListAdapter(mContext: Context, items: List<CouponModel>) : RecyclerView.Adapter<CouponListAdapter.ViewHolder>()
{
    private var context = mContext
    private var couponList : List<CouponModel> = items
    private var progressDialog = LoadingUtils(mContext)
    private val repository = OrderRepository()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_coupon, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : CouponModel = couponList[position]

        holder.tvCoupon.text = model.couponCode
        holder.tvExpiryDate.text = "Expire: ${dateFormat(model.expiryDate,YYYY_MM_DD,MM_DD)}"

        if(model.flagDiscount == 0.0)
            holder.tvDiscount.text = "Save: ${model.discount} % Max: ${context.resources.getString(R.string.taka)} ${model.maxDiscount}"
        else holder.tvDiscount.text = "Save: ${context.resources.getString(R.string.taka)} ${model.flagDiscount}"

        holder.btnApply.setOnClickListener {
            applyCoupon(couponList[position])
        }
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvCoupon: TextView = view.findViewById(R.id.tv_coupon)
        var tvDiscount: TextView = view.findViewById(R.id.tv_discount)
        var tvExpiryDate: TextView = view.findViewById(R.id.tv_expiry_date)
        var btnApply: Button = view.findViewById(R.id.btn_apply)
    }

    /**
     * ...apply coupons
     */
    private fun applyCoupon(model : CouponModel)
    {
        if(!ExtraUtils.isOnline(context))
        {
            showToast(context as Activity, NO_INTERNET_CONNECTION,ERROR_DIALOG)
            return
        }

        progressDialog.showProgressDialog()

        repository.applyCoupon(model.couponCode).observe(context as LifecycleOwner, {
            if(it.responseCode == RESPONSE_SUCCESS_CODE)
            {
                model.isApplied = true
                selectedCouponModel.value = model
                ExtraUtils.startActivityOut(context as Activity)
            }
        })
    }

}
