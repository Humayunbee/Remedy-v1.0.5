package com.medicine.remedy.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.databinding.LayoutPaymentMethodBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.*
import com.medicine.remedy.utils.AppConstants.Companion.ERROR_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.SUCCESS_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.WARNING_DIALOG
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.view.activity.OrderActivity
import com.medicine.remedy.view_model.DashboardViewModel
import com.medicine.remedy.view_model.OrderViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/7/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class PaymentMethodFragment : BaseFragment()
{
    private lateinit var binding : LayoutPaymentMethodBinding
    private lateinit var viewModel : OrderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_payment_method, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        binding.lifecycleOwner = this
        binding.payment = viewModel
        ButterKnife.bind(this, view)

        initialize()
        return view
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = [R.id.rl_bkash,R.id.rl_nagad,R.id.rl_card,R.id.rl_cod])
    fun onClickListener(view: View)
    {
        when(view.id)
        {
            R.id.rl_bkash->showToast(mContext as Activity, "Sorry, This method is Not Available.",WARNING_DIALOG)
            R.id.rl_nagad->showToast(mContext as Activity, "Sorry, This method is Not Available.",WARNING_DIALOG)
            R.id.rl_card->showToast(mContext as Activity, "Sorry, This method is Not Available.",WARNING_DIALOG)
            R.id.rl_cod->checkOutOrder()
        }
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        (activity as OrderActivity).setToolbarTitle(AppConstants.checkout)//set toolbar title

        viewModel.mlCouponCodeId.value = requireArguments().getString(AppConstants.couponId)
        viewModel.mlShippingAddressId.value = requireArguments().getString(AppConstants.addressId)
        viewModel.mlGrandTotalAmt.value = requireArguments().getString(AppConstants.grandTotal)
        OrderViewModel.mlCouponDiscount.value = ExtraUtils.stringAmountToDouble(requireArguments().getString(AppConstants.couponDiscount).toString())
        Log.d("grand_total_amt",OrderViewModel.mlGrandTotal.value.toString())
    }

    /**
     * ...checkout order
     * ...check internet connection
     * ...display/hide loading view
     */
    private fun checkOutOrder()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        loadingDialog.show()

        viewModel.placeOrder().observe(viewLifecycleOwner, {
            if (it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE) {
                viewModel.mlInvoiceNo.value = "#ORD-${it.orderId}"
                viewModel.doEmptyCart()
                DashboardViewModel.mlTotalCartItem.value = 0
                showToast(mContext as Activity, "Your Order has been placed.", SUCCESS_DIALOG)

                val fragment: Fragment = OrderConfirmationFragment()
                val args = Bundle()
                args.putString(AppConstants.invoiceNo, viewModel.mlInvoiceNo.value)
                fragment.arguments = args
                ExtraUtils.showFragment(context as FragmentActivity, fragment)

            } else showToast(mContext as Activity, "Failed to place order, please retry",ERROR_DIALOG)

            loadingDialog.dismiss()
        })
    }

    private val callBack = (object : OnRetryCallback {
        override fun onRetry() {
            checkOutOrder()
        }
    })
}