package com.medicine.remedy.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.medicine.remedy.R
import com.medicine.remedy.adapter.CouponListAdapter
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.databinding.LayoutCouponListBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.*
import com.medicine.remedy.view.activity.CouponsActivity
import com.medicine.remedy.view_model.CouponViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class CouponListFragment : BaseFragment()
{
    private lateinit var binding : LayoutCouponListBinding
    private lateinit var viewModel : CouponViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.layout_coupon_list, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(CouponViewModel::class.java)
        binding.lifecycleOwner = this
        binding.coupon = viewModel
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
        }else couponListObserver()
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        (activity as CouponsActivity).setToolbarTitle(AppConstants.coupon)//set toolbar title
    }

    /**
     * ...display coupon list on recyclerview
     * ...check internet connection
     * ...display/hide loading view
     */
    private fun couponListObserver()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        loadingDialog.show()

        viewModel.getCouponList().observe(viewLifecycleOwner, {
            if(it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE)
            {
                binding.emptyView.visibility = GONE
                binding.rvList.visibility = VISIBLE
                val adapter = CouponListAdapter(mContext, it.couponList)
                binding.rvList.isNestedScrollingEnabled = false
                ViewUtils.verticalRecyclerView(mActivity, binding.rvList).adapter = adapter
            }else{
                binding.emptyView.visibility = VISIBLE
                binding.rvList.visibility = GONE
            }

            loadingDialog.dismiss()
        })
    }

    private val callBack = (object : OnRetryCallback {
        override fun onRetry() {
            couponListObserver()
        }
    })
}