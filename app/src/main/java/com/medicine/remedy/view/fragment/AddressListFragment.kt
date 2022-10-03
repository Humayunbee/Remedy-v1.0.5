package com.medicine.remedy.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.adapter.AddressListAdapter
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.databinding.LayoutAddressListBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.DialogUtils.Companion.noInternetDialogFullScreen
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.LoadingUtils.Companion.loadingView
import com.medicine.remedy.utils.ViewUtils.Companion.verticalRecyclerView
import com.medicine.remedy.view.activity.AddressActivity
import com.medicine.remedy.view_model.UserInformationViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/2/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class AddressListFragment : BaseFragment()
{
    private lateinit var binding : LayoutAddressListBinding
    private lateinit var viewModel : UserInformationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.layout_address_list, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(UserInformationViewModel::class.java)
        binding.lifecycleOwner = this
        binding.address = viewModel
        ButterKnife.bind(this, view)

        initialize()
        return view
    }

    override fun onStart() {
        super.onStart()
        if(!ExtraUtils.isOnline(requireContext()))
        {
            noInternetDialogFullScreen(mActivity,callBack)
            return
        }else addressListObserver()
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.rl_add_address)
    fun onClickListener()
    {
        (activity as AddressActivity).showFragment(AddNewAddressFragment())
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = loadingView(mContext)
        (activity as AddressActivity).setToolbarTitle(AppConstants.shippingAddress)//set toolbar title
    }

    /**
     * ...display address list on recyclerview
     * ...check internet connection
     * ...display/hide loading view
     */
    private fun addressListObserver()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            noInternetDialogFullScreen(mActivity,callBack)
            return
        }

        loadingDialog.show()

        viewModel.getAddressList().observe(viewLifecycleOwner, {
            if(it.responseCode == RESPONSE_SUCCESS_CODE)
            {
                val adapter = AddressListAdapter(mContext, it.addressList)
                binding.rvList.isNestedScrollingEnabled = false
                verticalRecyclerView(mActivity, binding.rvList).adapter = adapter

                if(it.addressList.isNotEmpty())
                    binding.rlAddAddress.visibility = GONE
                else binding.rlAddAddress.visibility = VISIBLE

            }else errorSnackBar(binding.root,"No address found, please add an address.")

            loadingDialog.dismiss()
        })
    }

    private val callBack = (object : OnRetryCallback{
        override fun onRetry() {
            addressListObserver()
        }
    })
}