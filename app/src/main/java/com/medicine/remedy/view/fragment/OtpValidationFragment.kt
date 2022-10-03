package com.medicine.remedy.view.fragment

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
import com.medicine.remedy.databinding.LayoutOtpValidationBinding
import com.medicine.remedy.interfaces.OnOtpVerificationComplete
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.AppConstants.Companion.ERROR_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.SUCCESS_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.WARNING_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.phoneNumber
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.LoadingUtils
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.view.activity.UserAuthenticationActivity
import com.medicine.remedy.view_model.UserAuthenticationViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/17/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class OtpValidationFragment : BaseFragment()
{
    private lateinit var binding : LayoutOtpValidationBinding
    private lateinit var viewModel : UserAuthenticationViewModel

    lateinit var onOtpVerification : OnOtpVerificationComplete

    private val otp = "1234"
    private var type = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_otp_validation, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(UserAuthenticationViewModel::class.java)
        binding.lifecycleOwner = this
        binding.validation = viewModel
        ButterKnife.bind(this, view)
        initialize()

        return view
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        loadingDialog = LoadingUtils.loadingView(requireContext())
        spManager = SharedPreferenceManager(requireContext())

        val userPhoneNumber = requireArguments().getString(phoneNumber)
        type = requireArguments().getInt("type", 0)
        viewModel.mlUserPhoneNumber.value = userPhoneNumber
    }

    @OnClick(value = [R.id.iv_back, R.id.btn_continue, R.id.tv_resend])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.iv_back->{
                requireActivity().supportFragmentManager.popBackStack()//remove from back stack
            }

            R.id.btn_continue->{
                if(dataValidation()) checkOtp()
            }

            R.id.tv_resend->{}
        }
    }

    /**
     * ...checking phone number and otp
     * ...return true if both are valid otherwise return false
     */
    private fun dataValidation() : Boolean
    {
        val otpInput = binding.otpView.text.toString()
        viewModel.mlOtp.value = otpInput

        val status = viewModel.otpInfoValidation()

        if(status == 1)
        {
            requireActivity().supportFragmentManager.popBackStack()
            return false
        }

        if(status == 2)
        {
            showToast(mActivity, "Invalid OTP.", WARNING_DIALOG)
            return false
        }

        return true
    }

    /**
     * ...checking internet connection
     * ...check otp and entered phone number
     */
    private fun checkOtp()
    {
        if (!ExtraUtils.isOnline(mContext)) //check internet connection
        {
            noInternetConnection(binding.root)
            return
        }

        loadingDialog.show()

        viewModel.checkOtp().observe(mActivity as LifecycleOwner) {

            if (it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE) {
                requireActivity().supportFragmentManager.popBackStack()
                val fragment=  if (type == 1){
                    ChangePasswordOtpFragment().apply { arguments = bundleOf(phoneNumber to viewModel.mlUserPhoneNumber.value, "otp" to viewModel.mlOtp.value) }
                }else{
                   UserRegistrationFragment().apply { arguments = bundleOf(phoneNumber to viewModel.mlUserPhoneNumber.value) }
                }
                (activity as UserAuthenticationActivity).showFragment(fragment)

            } else showToast(mActivity, "OTP does not match, please retry.", ERROR_DIALOG)
            loadingDialog.dismiss()
        }
    }
}