package com.medicine.remedy.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.medicine.remedy.R
import com.medicine.remedy.adapter.OrderHistoryAdapter
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.databinding.LayoutRecyclerViewBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.*
import com.medicine.remedy.view.activity.OrderHistoryActivity
import com.medicine.remedy.view_model.OrderHistoryViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class OrderHistoryFragment  : BaseFragment()
{
    private lateinit var binding : LayoutRecyclerViewBinding
    private lateinit var viewModel : OrderHistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.layout_recycler_view, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel::class.java)
        binding.lifecycleOwner = this
        binding.history = viewModel
        ButterKnife.bind(this, view)

        initialize()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }else orderHistoryObserver()
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        (activity as OrderHistoryActivity).setToolbarTitle(AppConstants.orderHistory)//set toolbar title
    }

    /**
     * ...display order history list on recyclerview
     * ...check internet connection
     * ...display/hide loading view
     */
    private fun orderHistoryObserver()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        loadingDialog.show()

        viewModel.getOrderHistory().observe(viewLifecycleOwner, {
            Log.d("responseCode",it.responseCode.toString())
            if(it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE)
            {
                binding.emptyView.visibility = View.GONE
                binding.rvList.visibility = View.VISIBLE
                val adapter = OrderHistoryAdapter(mContext, it.orderHistory)
                binding.rvList.isNestedScrollingEnabled = false
                ViewUtils.verticalRecyclerView(mActivity, binding.rvList).adapter = adapter
            }else {
                binding.emptyView.visibility = View.VISIBLE
                binding.rvList.visibility = View.GONE
            }

            loadingDialog.dismiss()
        })
    }

    private val callBack = (object : OnRetryCallback {
        override fun onRetry() {
            orderHistoryObserver()
        }
    })
}