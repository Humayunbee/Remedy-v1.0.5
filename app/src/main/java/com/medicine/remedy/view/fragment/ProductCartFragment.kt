package com.medicine.remedy.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.adapter.CartItemListAdapter
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.database.entities.CartItemEntities
import com.medicine.remedy.databinding.LayoutCartItemsBinding
import com.medicine.remedy.utils.*
import com.medicine.remedy.utils.AppConstants.Companion.NO_INTERNET_CONNECTION
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.AppConstants.Companion.WARNING_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.isViewOnly
import com.medicine.remedy.utils.LoadingUtils.Companion.loadingView
import com.medicine.remedy.utils.MathematicsUtils.DISCOUNT_CALCULATION
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.utils.ViewUtils.Companion.verticalRecyclerView
import com.medicine.remedy.view.activity.AddressActivity
import com.medicine.remedy.view.activity.CouponsActivity
import com.medicine.remedy.view.activity.OrderActivity
import com.medicine.remedy.view.activity.UserAuthenticationActivity
import com.medicine.remedy.view_model.OrderViewModel
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlCouponDiscount
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlDeliveryCharge
import com.medicine.remedy.view_model.OrderViewModel.Companion.mlSubTotal
import com.medicine.remedy.view_model.OrderViewModel.Companion.selectedAddressModel
import com.medicine.remedy.view_model.OrderViewModel.Companion.selectedCouponModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ProductCartFragment : BaseFragment()
{
    private lateinit var binding : LayoutCartItemsBinding
    private lateinit var viewModel : OrderViewModel

    companion object{
        var cartItems =  ArrayList<CartItemEntities>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_cart_items, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        binding.lifecycleOwner = this
        binding.cartItem = viewModel
        ButterKnife.bind(this, view)

        initialize()
        cartItemsObserver()

        return view
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(requireContext())
        loadingUtils = LoadingUtils(mContext)
        loadingDialog = loadingView(mContext)
        (activity as OrderActivity).setToolbarTitle(AppConstants.cart)//set toolbar title

        if(spManager.getIsUserLoggedIn())
            viewModel.getAddressInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewOnly = true
    }

    @OnClick(value = [R.id.tv_change,R.id.ln_coupon,R.id.tv_remove,R.id.tv_apply,R.id.btn_place_order])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.tv_change -> {
                isViewOnly = false
                ExtraUtils.startActivityIn(requireActivity(), Intent(requireActivity(), AddressActivity::class.java))
            }

            R.id.ln_coupon -> {
                isViewOnly = false
                ExtraUtils.startActivityIn(requireActivity(), Intent(requireActivity(), CouponsActivity::class.java))
            }

            R.id.tv_remove -> enableComponent(true)
            R.id.tv_apply -> applyCouponObserver()

            R.id.btn_place_order->{
                if(dataValidations())
                {
                    val fragment: Fragment = OrderDetailsFragment()
                    val args = Bundle()
                    args.putString(AppConstants.addressId, viewModel.mlShippingAddressId.value)
                    args.putString(AppConstants.address, viewModel.mlShippingAddress.value)

                    args.putString(AppConstants.couponId, viewModel.mlCouponCodeId.value)
                    args.putString(AppConstants.couponCode, viewModel.mlCouponCode.value)
                    args.putString(AppConstants.couponDiscount, mlCouponDiscount.value.toString())

                    args.putInt(AppConstants.deliveryCharge, mlDeliveryCharge.value!!)
                    fragment.arguments = args
                    ExtraUtils.showFragment(context as FragmentActivity, fragment)
                }
            }
        }
    }

    /**
     * ...validation of required data.
     */
    private fun dataValidations() : Boolean
    {
        when(viewModel.validationOfRequiredData())
        {
            1->{
                showToast(mContext as Activity,"Please add some items and retry", WARNING_DIALOG)
                return  false
            }

            2->{
                showToast(mContext as Activity,"Empty address not allowed", WARNING_DIALOG)
                return false
            }

            3->{
                showToast(mContext as Activity,"Grand total 0 or less than 0 is not allowed", WARNING_DIALOG)
                return  false
            }

            4->{
                binding.etCoupon.error = "Invalid coupon"
                return false
            }
        }

        if(!spManager.getIsUserLoggedIn())
        {
            ExtraUtils.startActivityIn(requireActivity(), Intent(requireActivity(), UserAuthenticationActivity::class.java))
            return false
        }

        return true
    }

    /**
     * ...calculation observer
     */
    private fun calculationObserver()
    {
        selectedAddressModel.observe(viewLifecycleOwner, {
            spManager.saveDefaultAddressInfo(it)
            viewModel.mlShippingAddress.value = it.address
            mlDeliveryCharge.value = it.deliveryCharge
            viewModel.mlShippingAddressId.value = it.addressId
            viewModel.mlDisplayDeliveryCharge.value = "${it.deliveryCharge} ${mContext.resources.getString(R.string.taka)} "
        })

        selectedCouponModel.observe(viewLifecycleOwner, {
            if(mlSubTotal.value!! >= it.minOrderAmount)
            {
                if(it.isApplied)
                {
                    viewModel.mlCouponCode.value = it.couponCode
                    viewModel.mlCouponCodeId.value = it.couponId
                    mlCouponDiscount.value = DISCOUNT_CALCULATION(it.discount, it.flagDiscount, it.maxDiscount, mlSubTotal.value!!)

                    if(cartItems.isNotEmpty()) MathematicsUtils.calculateAmount(mContext, cartItems)
                    enableComponent(false)
                }
            }else {
                if(it.isApplied)
                showToast(mActivity, "Minimum Order amount is ${it.minOrderAmount}", WARNING_DIALOG)
            }

            Log.d("couponDiscount", "${mlCouponDiscount.value}")

        })
    }

    /**
     * ...enable and disable component
     */
    private fun enableComponent(isEnabled: Boolean)
    {
        binding.etCoupon.clearFocus()
        binding.etCoupon.isEnabled = isEnabled
        binding.tvApply.visibility = if(isEnabled) VISIBLE else GONE
        binding.tvRemove.visibility = if(isEnabled) GONE else VISIBLE

        if(isEnabled)
        {
            viewModel.mlCouponCode.value = ""
            viewModel.mlCouponCodeId.value = ""
            mlCouponDiscount.value = 0.0
            selectedCouponModel.value!!.isApplied = false
            if(cartItems.isNotEmpty())
                MathematicsUtils.calculateAmount(mContext, cartItems)
        }
    }

    /**
     * ...cartItemsObserver
     * ...cartItems list
     * ...check cartItems list is empty or not
     */
    private fun cartItemsObserver()
    {
        viewModel.getCartItems().observe(viewLifecycleOwner, {
            if(it.isNotEmpty())
            {
                binding.clRoot.visibility = VISIBLE
                binding.emptyView.visibility = GONE
                cartItems = it as ArrayList<CartItemEntities>
                val adapter = CartItemListAdapter(mContext, it)
                verticalRecyclerView(mContext,binding.rvList).adapter = adapter
                calculationObserver()
            }else {
                binding.clRoot.visibility = GONE
                binding.emptyView.visibility = VISIBLE
            }
        })
    }

    /**
     * ...apply coupons observer
     * ...check internet connection
     * ...show and hide progress dialog
     */
    private fun applyCouponObserver()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            errorSnackBar(binding.root,NO_INTERNET_CONNECTION)
            return
        }

        if(viewModel.mlCouponCode.value == null || TextUtils.isEmpty(viewModel.mlCouponCode.value.toString()))
        {
            errorSnackBar(binding.root, "Please insert coupon code")
            return
        }

        loadingUtils.showProgressDialog()

        viewModel.applyCoupon().observe(viewLifecycleOwner,{
            if(it.responseCode == RESPONSE_SUCCESS_CODE)
            {
                if(it.couponInfo.minOrderAmount <= mlSubTotal.value!!)
                {
                    it.couponInfo.isApplied = true
                    selectedCouponModel.value = it.couponInfo
                    enableComponent(false)
                }else showToast(mActivity, "Minimum Order amount is ${it.couponInfo.minOrderAmount}", WARNING_DIALOG)
            }
            else errorSnackBar(binding.root, "Failed to applied coupon, please retry")

            loadingUtils.dismissProgressDialog()
        })
    }
}