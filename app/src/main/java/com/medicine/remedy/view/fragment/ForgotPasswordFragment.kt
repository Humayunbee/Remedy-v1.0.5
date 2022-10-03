package com.medicine.remedy.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.databinding.LayoutForgotPasswordBinding
import com.medicine.remedy.utils.*
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.AppConstants.Companion.WARNING_DIALOG
import com.medicine.remedy.utils.DataValidation.Companion.modifyPhoneNumber
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.view.activity.UserAuthenticationActivity
import com.medicine.remedy.view_model.UserAuthenticationViewModel

class ForgotPasswordFragment: BaseFragment()
{
    private lateinit var binding : LayoutForgotPasswordBinding
    private lateinit var viewModel : UserAuthenticationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_forgot_password, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(UserAuthenticationViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        ButterKnife.bind(this, view)
        initialize()

        return view
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        dataValidation = DataValidation()
        loadingDialog = LoadingUtils.loadingView(mContext)
        spManager = SharedPreferenceManager(requireContext())
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.rl_next_btn)
    fun onClickListener()
    {
        if(dataValidation.phoneNumberValidation(modifyPhoneNumber(viewModel.mlPhoneNumber.value.toString())))
            sendOtp()
        else showToast(mActivity, "Phone number is not valid",WARNING_DIALOG)
    }

    /**
     * ...checking internet connection
     * ...send otp to entered phone number
     */
    private fun sendOtp()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            noInternetConnection(binding.root)
            return
        }

        loadingDialog.show()

        viewModel.sendOtp().observe(mActivity as LifecycleOwner) {
            if (it.responseCode == RESPONSE_SUCCESS_CODE) {
                showToast(mActivity, "OTP Successfully Send To This ${modifyPhoneNumber(viewModel.mlPhoneNumber.value.toString())} Number.", AppConstants.SUCCESS_DIALOG)
                val fragment = OtpValidationFragment().apply {
                    arguments = bundleOf(AppConstants.phoneNumber to modifyPhoneNumber(viewModel.mlPhoneNumber.value.toString()), "type" to 1)
                }
                (activity as UserAuthenticationActivity).showFragment(fragment)
            } else showToast(mActivity, "Failed to send OTP, please retry.", AppConstants.ERROR_DIALOG)
            loadingDialog.dismiss()
        }
    }
}