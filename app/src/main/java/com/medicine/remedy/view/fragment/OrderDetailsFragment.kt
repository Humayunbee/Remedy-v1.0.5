package com.medicine.remedy.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.adapter.OrderDetailsAdapter
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.data_model.OrderDetailsModel
import com.medicine.remedy.databinding.LayoutOrderDetailsBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.*
import com.medicine.remedy.utils.ExtraUtils.Companion.stringAmountToDouble
import com.medicine.remedy.view.activity.OrderActivity
import com.medicine.remedy.view.fragment.ProductCartFragment.Companion.cartItems
import com.medicine.remedy.view_model.OrderViewModel
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlCouponDiscount
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlDeliveryCharge
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlGrandTotal

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/7/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class OrderDetailsFragment : BaseFragment()
{
    private lateinit var binding : LayoutOrderDetailsBinding
    private lateinit var viewModel : OrderViewModel
    companion object{
        var productList = ArrayList<OrderDetailsModel>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_order_details, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        binding.lifecycleOwner = this
        binding.order = viewModel
        ButterKnife.bind(this, view)

        initialize()
        return view
    }

    override fun onStart() {
        super.onStart()
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }else orderDetailsObserver()
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_checkout)
    fun onClickListener()
    {
        if(validationOfCheckoutRequiredData())
        {
            val fragment: Fragment = PaymentMethodFragment()
            val args = Bundle()
            args.putString(AppConstants.addressId, viewModel.mlShippingAddressId.value)
            args.putString(AppConstants.couponId, viewModel.mlCouponCodeId.value)
            args.putString(AppConstants.grandTotal, "${mlGrandTotal.value}")
            args.putString(AppConstants.couponDiscount, "${mlCouponDiscount.value}")
            fragment.arguments = args
            ExtraUtils.showFragment(context as FragmentActivity, fragment)
        }
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        (activity as OrderActivity).setToolbarTitle(AppConstants.checkout)//set toolbar title

        viewModel.mlShippingAddress.value = requireArguments().getString(AppConstants.address)
        viewModel.mlCouponCodeId.value = requireArguments().getString(AppConstants.couponId)
        viewModel.mlShippingAddressId.value = requireArguments().getString(AppConstants.addressId)
        mlCouponDiscount.value = stringAmountToDouble(requireArguments().getString(AppConstants.couponDiscount).toString())
        mlDeliveryCharge.value = requireArguments().getInt(AppConstants.deliveryCharge)
        viewModel.mlCouponCode.value = requireArguments().getString(AppConstants.couponCode)
        viewModel.mlDisplayDeliveryCharge.value = "${mlDeliveryCharge.value} ${mContext.resources.getString(R.string.taka)}"

        if(viewModel.mlCouponCode.value != null && viewModel.mlCouponCode.value != "")
        {
            binding.tvCoupon.visibility = VISIBLE
            viewModel.mlCouponCode.value = requireArguments().getString(AppConstants.couponCode)+ " coupon is applied"
        }
    }

    /**
     * ...validation of required data.
     */
    private fun validationOfCheckoutRequiredData() : Boolean
    {
        when(viewModel.validationOfCheckoutRequiredData())
        {
            1->{
                errorSnackBar(binding.root, "Please add some items and retry")
                return  false
            }

            2->{
                errorSnackBar(binding.root, "Address information not found")
                return false
            }

            3->{
                errorSnackBar(binding.root, "Grand total 0 or less than 0 is not allowed")
                return  false
            }

            4->{
                errorSnackBar(binding.root, "Coupon information not found")
                return false
            }
        }


        return true
    }

    /**
     * ...display address list on recyclerview
     * ...check internet connection
     * ...display/hide loading view
     */
    private fun orderDetailsObserver()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        if(cartItems.isEmpty())
        {
            binding.clRoot.visibility = View.GONE
            binding.emptyView.visibility = VISIBLE
            return
        }

        loadingDialog.show()

        viewModel.getUpdateProductPrice().observe(viewLifecycleOwner, {
            if (it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE) {
                binding.clRoot.visibility = VISIBLE
                binding.emptyView.visibility = View.GONE
                productList = it.updateProductRates as ArrayList<OrderDetailsModel>
                val adapter = OrderDetailsAdapter(mContext, it.updateProductRates)
                binding.rvList.isNestedScrollingEnabled = false
                ViewUtils.verticalRecyclerView(mActivity, binding.rvList).adapter = adapter
            } else {
                binding.clRoot.visibility = View.GONE
                binding.emptyView.visibility = VISIBLE
            }

            loadingDialog.dismiss()
        })
    }

    private val callBack = (object : OnRetryCallback {
        override fun onRetry() {
            orderDetailsObserver()
        }
    })
}