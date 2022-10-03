package com.medicine.remedy.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
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
import com.medicine.remedy.databinding.LayoutAddNewAddressBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.*
import com.medicine.remedy.utils.AppConstants.Companion.ERROR_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.AppConstants.Companion.SUCCESS_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.homeAddress
import com.medicine.remedy.utils.AppConstants.Companion.invalid
import com.medicine.remedy.utils.AppConstants.Companion.officeAddress
import com.medicine.remedy.utils.AppConstants.Companion.otherAddress
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.view.activity.AddressActivity
import com.medicine.remedy.view_model.UserInformationViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
open class AddNewAddressFragment : BaseFragment()
{
    protected lateinit var binding : LayoutAddNewAddressBinding
    protected lateinit var viewModel : UserInformationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_add_new_address, container, false)
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
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = [R.id.btn_save,R.id.ti_pin_location,R.id.et_pin_location, R.id.rb_home,R.id.rb_office,R.id.rb_other])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.btn_save ->{
                if(isDataValid())
                {
                    onButtonDisable(binding.btnSave)
                    addNewAddressObserver()
                }
            }

            R.id.rb_home-> viewModel.mlAddressType.value = homeAddress

            R.id.rb_office-> viewModel.mlAddressType.value = officeAddress

            R.id.rb_other-> viewModel.mlAddressType.value = otherAddress

            R.id.ti_pin_location,R.id.et_pin_location->{}
        }
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        (activity as AddressActivity).setToolbarTitle(AppConstants.addNewAddress)//set toolbar title
    }

    /**
     * ...set error while any fields is not valid.
     * ...set request focus while any data is not valid.
     * ...while all data are valid return true.
     * ...otherwise return false
     * ...get error codes
     */
    private fun isDataValid() : Boolean
    {
        when(viewModel.validationOfAddress())
        {
            1->{
                binding.etTitle.error = invalid
                binding.etTitle.requestFocus()
                return false
            }

            2->{
                binding.etAddress.error = invalid
                binding.etAddress.requestFocus()
                return false
            }

            6->{
                binding.etReceiverName.error = invalid
                binding.etReceiverName.requestFocus()
                return false
            }

            7->{
                binding.etPhoneNumber.error = invalid
                binding.etPhoneNumber.requestFocus()
                return false
            }

            8->{
                errorSnackBar(binding.root, "Please choose address type")
                return false
            }
        }

        return true
    }

    /**
     * ...add new address
     * ...check internet connection
     * ...display/hide loading view
     * ...enable button if failed to add address
     */
    open fun addNewAddressObserver()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        loadingDialog.show()

        viewModel.addNewAddress(binding.cbDefaultAddress.isChecked).observe(viewLifecycleOwner, {
            if(it.responseCode == RESPONSE_SUCCESS_CODE)
            {
                showToast(mContext as Activity, "New address successfully added", SUCCESS_DIALOG)
                requireActivity().supportFragmentManager.popBackStack()
            }
            else {
                showToast(mContext as Activity, "Failed to add address, please retry", ERROR_DIALOG)
                onButtonEnable(binding.btnSave)
            }

            loadingDialog.dismiss()
        })
    }

    open val callBack = (object : OnRetryCallback {
        override fun onRetry() {
            addNewAddressObserver()
        }
    })
}