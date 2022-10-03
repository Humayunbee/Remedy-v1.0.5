package com.medicine.remedy.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.databinding.LayoutOrderConfirmationBinding
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.LoadingUtils
import com.medicine.remedy.view.activity.OrderActivity
import com.medicine.remedy.view.activity.OrderActivity.Companion.orderActivity
import com.medicine.remedy.view.activity.OrderHistoryActivity
import com.medicine.remedy.view_model.OrderViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class OrderConfirmationFragment : BaseFragment()
{
    companion object{
        var isOrderPlaced = false
    }

    private lateinit var binding : LayoutOrderConfirmationBinding
    private lateinit var viewModel : OrderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_order_confirmation, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        binding.lifecycleOwner = this
        ButterKnife.bind(this, view)

        initialize()
        return view
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ln_order_history)
    fun onClickListener()
    {
        ExtraUtils.startActivityIn(requireActivity(), Intent(requireActivity(), OrderHistoryActivity::class.java))
        orderActivity.finish()
    }

    @SuppressLint("SetTextI18n")
    override fun initialize() {
        isOrderPlaced = true
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        (activity as OrderActivity).hideToolbar()//hide white toolbar

        val invoiceNo = requireArguments().getString(AppConstants.invoiceNo)

        binding.tvInvoiceNo.text = "Your order $invoiceNo was placed successfully. For more details, please check your order history menu. \n\nTHANK YOU"
    }

}