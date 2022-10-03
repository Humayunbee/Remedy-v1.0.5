package com.medicine.remedy.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.medicine.remedy.R
import com.medicine.remedy.adapter.InvoiceDetailsAdapter
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.data_model.OrderHistoryModel
import com.medicine.remedy.databinding.LayoutOrderTrackBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.*
import com.medicine.remedy.utils.AppConstants.Companion.MMM_DD_YYYY
import com.medicine.remedy.utils.AppConstants.Companion.YYYY_MM_DD
import com.medicine.remedy.utils.DateTimeUtils.Companion.dateFormat
import com.medicine.remedy.view.activity.OrderHistoryActivity
import com.medicine.remedy.view_model.OrderHistoryViewModel
import com.medicine.remedy.view_model.OrderHistoryViewModel.Companion.mlCouponDiscount
import com.medicine.remedy.view_model.OrderHistoryViewModel.Companion.mlDisplaySubTotalDiscount

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/9/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class OrderTrackFragment : BaseFragment()
{
    private lateinit var binding : LayoutOrderTrackBinding
    private lateinit var viewModel : OrderHistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.layout_order_track, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel::class.java)
        binding.lifecycleOwner = this
        binding.track = viewModel
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
        }else orderTrackObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        OrderHistoryViewModel.mlDisplaySubTotal.value = ""
        OrderHistoryViewModel.mlDisplaySubTotalDiscount.value = ""
        OrderHistoryViewModel.mlDisplayGrandTotal.value = ""
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        (activity as OrderHistoryActivity).setToolbarTitle(AppConstants.orderHistory)//set toolbar title

        viewModel.mlOrderId.value = requireArguments().getString(AppConstants.orderId)
    }

    /**
     * ...display order track details
     * ...check internet connection
     * ...display/hide loading view
     */
    @SuppressLint("SetTextI18n")
    private fun orderTrackObserver()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        loadingDialog.show()

        viewModel.getOrderTrackInfo().observe(viewLifecycleOwner, {
            if(it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE)
            {
                viewModel.mlDeliveryAddress.value = it.addressModel.address
                binding.tvOrderNo.text = "#ORD-${it.orderHistory.orderId}"
                binding.tvOrderDate.text = dateFormat(it.orderHistory.orderDate, YYYY_MM_DD,MMM_DD_YYYY)
                viewTrackInfo(it.orderHistory)
                val adapter = InvoiceDetailsAdapter(mContext, it.orderDetails)
                binding.rvList.isNestedScrollingEnabled = false
                ViewUtils.verticalRecyclerView(mActivity, binding.rvList).adapter = adapter
            }else errorSnackBar(binding.root,"Order information not found")

            loadingDialog.dismiss()
        })
    }

    /**
     * ...display order tracking information
     */
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun viewTrackInfo(order : OrderHistoryModel)
    {
        when(order.orderStatus)
        {
            1->{
                binding.ivPlacedOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivPlacedOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
            }

            2->{
                binding.ivPlacedOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivProcessingOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivPlacedOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
                binding.ivProcessingOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))

            }

            3->{
                binding.ivPlacedOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivProcessingOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivPickedOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivDeliveringOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivPlacedOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
                binding.ivProcessingOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
                binding.ivPickedOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
                binding.ivDeliveringOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
            }

            4->{
                binding.ivPlacedOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivProcessingOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivPickedOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivDeliveringOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivDeliveredOrder.background = ExtraUtils.getTintedDrawable(mContext.resources.getDrawable(R.drawable.bg_circle_solid_red), mContext.resources.getColor(R.color.colorPrimary))
                binding.ivPlacedOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
                binding.ivProcessingOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
                binding.ivPickedOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
                binding.ivDeliveringOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
                binding.ivDeliveredOrder.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_done_white))
            }
        }

        binding.tvPlacedOrder.text = "Your order #ORD-${order.orderId} was placed on ${dateFormat(order.orderDate,YYYY_MM_DD, MMM_DD_YYYY)}."
        binding.tvProcessingOrder.text = "Your order #ORD-${order.orderId} still needs to be processed by our store before sending it to you."
        binding.tvPickedOrder.text = "Your order #ORD-${order.orderId} has been picked up by one of our rider and its on your way."
        binding.tvDeliveringOrder.text = "Your order #ORD-${order.orderId} package is on your way. Make sure to be at the location to meet the courier."
        binding.tvDeliveredOrder.text = "Your order #ORD-${order.orderId}, It seems like the package has arrived to you. Hope you are happy with it!"
        mlCouponDiscount.value = order.discount
    }

    private val callBack = (object : OnRetryCallback {
        override fun onRetry() {
            orderTrackObserver()
        }
    })
}