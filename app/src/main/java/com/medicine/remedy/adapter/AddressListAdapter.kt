package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.data_model.AddressModel
import com.medicine.remedy.utils.AppConstants.Companion.addressId
import com.medicine.remedy.utils.AppConstants.Companion.homeAddress
import com.medicine.remedy.utils.AppConstants.Companion.isViewOnly
import com.medicine.remedy.utils.AppConstants.Companion.officeAddress
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.view.fragment.EditAddressFragment
import com.medicine.remedy.view_model.OrderViewModel.Companion.selectedAddressModel
import com.medicine.remedy.view_model.UserInformationViewModel.Companion.addressModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class AddressListAdapter(mContext: Context, items: List<AddressModel>) : RecyclerView.Adapter<AddressListAdapter.ViewHolder>()
{
    private var context = mContext
    private var addressList : List<AddressModel> = items
    private var spManager = SharedPreferenceManager(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_address, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : AddressModel = addressList[position]

        holder.tvAddress.text = model.address

        if(model.isDefault == 1)
        {
            holder.tvTitle.text = "Default"
            holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_button_20dp_checked, 0, 0, 0)
            spManager.saveDefaultAddressInfo(model)
        }else {
            holder.tvTitle.text = "Secondary"
            holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_20dp_un_checked, 0, 0, 0)
        }

        when (model.addressType) {
            homeAddress -> holder.tvAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_20dp, 0, 0, 0)
            officeAddress -> holder.tvAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_office_20dp, 0, 0, 0)
            else -> holder.tvAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_other_address_20dp, 0, 0, 0)
        }

        holder.itemView.setOnClickListener {
            if(!isViewOnly)
            {
                selectedAddressModel.value = addressList[position]
                ExtraUtils.startActivityOut(context as Activity)
            }
        }

        holder.tvChange.setOnClickListener {
            addressModel = addressList[position]

            val fragment: Fragment = EditAddressFragment()
            val args = Bundle()
            args.putString(addressId, model.addressId)
            fragment.arguments = args
            ExtraUtils.showFragment(context as FragmentActivity, fragment)
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvAddress: TextView = view.findViewById(R.id.tv_address)
        var tvTitle: TextView = view.findViewById(R.id.tv_title)
        var tvChange: TextView = view.findViewById(R.id.tv_change)
    }
}
