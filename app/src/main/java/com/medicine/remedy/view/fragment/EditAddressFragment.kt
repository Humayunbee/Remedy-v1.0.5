package com.medicine.remedy.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.medicine.remedy.R
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.AppConstants.Companion.ERROR_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.SUCCESS_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.addressId
import com.medicine.remedy.utils.AppConstants.Companion.homeAddress
import com.medicine.remedy.utils.AppConstants.Companion.officeAddress
import com.medicine.remedy.utils.DialogUtils
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.LoadingUtils
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.view.activity.AddressActivity
import com.medicine.remedy.view_model.UserInformationViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class EditAddressFragment : AddNewAddressFragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_add_new_address, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(UserInformationViewModel::class.java)
        binding.lifecycleOwner = this
        binding.address = viewModel
        ButterKnife.bind(this, view)

        initialize()
        viewModel.addressInfo()
        return view
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        (activity as AddressActivity).setToolbarTitle(AppConstants.editAddress)//set toolbar title

        viewModel.mlAddressId.value = requireArguments().getString(addressId)

        viewModel.mlAddressType.observe(viewLifecycleOwner,{
            when (it) {
                homeAddress -> binding.rbHome.isChecked =  true
                officeAddress -> binding.rbOffice.isChecked =  true
                else -> binding.rbOther.isChecked =  true
            }
        })

        viewModel.mlIsDefaultAddress.observe(viewLifecycleOwner,{
            binding.cbDefaultAddress.isChecked = it == 1
        })
    }

    /**
     * ...add new address
     * ...check internet connection
     * ...display/hide loading view
     * ...enable button if failed to add address
     */
    override fun addNewAddressObserver()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        loadingDialog.show()

        viewModel.updateAddress(binding.cbDefaultAddress.isChecked).observe(viewLifecycleOwner, {
            if(it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE)
            {
                showToast(mContext as Activity, "Address successfully updated", SUCCESS_DIALOG)
                requireActivity().supportFragmentManager.popBackStack()
            }
            else {
                showToast(mContext as Activity, "Failed to update address, please retry", ERROR_DIALOG)
                onButtonEnable(binding.btnSave)
            }

            loadingDialog.dismiss()
        })
    }
}