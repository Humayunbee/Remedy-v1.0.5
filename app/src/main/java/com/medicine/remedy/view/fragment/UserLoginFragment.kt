package com.medicine.remedy.view.fragment

import android.content.Intent
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
import com.medicine.remedy.databinding.LayoutLoginBinding
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.LoadingUtils.Companion.loadingView
import com.medicine.remedy.view.activity.DashboardActivity
import com.medicine.remedy.view.activity.UserAuthenticationActivity
import com.medicine.remedy.view_model.UserAuthenticationViewModel

/**
 * Date 1/11/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class UserLoginFragment : BaseFragment()
{
    private lateinit var binding : LayoutLoginBinding
    private lateinit var viewModel : UserAuthenticationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_login, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(UserAuthenticationViewModel::class.java)
        binding.lifecycleOwner = this
        binding.login = viewModel
        ButterKnife.bind(this, view)

        initialize()

        return view
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        loadingDialog = loadingView(mContext)
        spManager = SharedPreferenceManager(mContext)
    }

    @OnClick(value = [R.id.btn_sign_up, R.id.btn_login, R.id.tv_forgot_password])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.btn_sign_up->{
                (activity as UserAuthenticationActivity).showFragment(PhoneNumberValidationFragment())
            }

            R.id.btn_login->{
                if(isLoginInfoValid())
                    userLogin()
            }

            R.id.tv_forgot_password->  (activity as UserAuthenticationActivity).showFragment(ForgotPasswordFragment())
        }
    }

    /**
     * ...check all data
     * @return true when all data are valid otherwise return false
     */
    private fun isLoginInfoValid() : Boolean
    {
        when(viewModel.loginDataValidations())
        {
            1 -> {
                binding.etPhoneNumber.error = "Invalid phone number"
                binding.etPhoneNumber.requestFocus()
                return false
            }
            2 -> {
                binding.etPassword.error = "Invalid password"
                binding.etPassword.requestFocus()
                return false
            }
        }

        return true
    }

    /**
     * ...user login method
     * ...user login with valid phone number and password
     * ...check internet connection
     * ...handle server response
     * ...store user information and token
     */
    private fun userLogin()
    {
        if(!ExtraUtils.isOnline(requireContext()))
        {
            noInternetConnection(binding.cvRoot)
            return
        }

        loadingDialog.show()

        viewModel.userLogin().observe(viewLifecycleOwner, {
            if(it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE)
            {
                spManager.isLoggedIn(true)
                spManager.saveUserInfo(it.userModel)
                spManager.accessToken(it.token)
                if(it.addressData != null)
                    spManager.saveDefaultAddressInfo(it.addressData!!)
                ExtraUtils.startActivityIn(mActivity, Intent(mActivity, DashboardActivity::class.java))
                mActivity.finish()
            }else
                errorSnackBar(binding.cvRoot, "Login failed, Please try again")

            loadingDialog.dismiss()
        })
    }
}