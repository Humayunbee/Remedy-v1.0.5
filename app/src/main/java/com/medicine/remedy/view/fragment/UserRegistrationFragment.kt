package com.medicine.remedy.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseFragment
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.AppConstants.Companion.ERROR_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.AppConstants.Companion.SUCCESS_DIALOG
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ExtraUtils.Companion.isOnline
import com.medicine.remedy.utils.LoadingUtils
import com.medicine.remedy.utils.LoadingUtils.Companion.loadingView
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.view.activity.DashboardActivity
import com.medicine.remedy.view_model.UserAuthenticationViewModel

/**
 * Date 1/11/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
@SuppressLint("NonConstantResourceId")
class UserRegistrationFragment : BaseFragment()
{
    private lateinit var binding : com.medicine.remedy.databinding.LayoutRegistrationBinding
    private lateinit var viewModel : UserAuthenticationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_registration, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(UserAuthenticationViewModel::class.java)
        binding.lifecycleOwner = this
        binding.registration = viewModel
        ButterKnife.bind(this, view)
        initialize()

        return view
    }

    override fun initialize() {
        mContext = requireContext()
        mActivity = requireActivity()
        loadingDialog = loadingView(requireContext())
        spManager = SharedPreferenceManager(requireContext())
        loadingUtils = LoadingUtils(requireContext())
        val userPhoneNumber = requireArguments().getString(AppConstants.phoneNumber)
        viewModel.mlPhoneNumber.value = userPhoneNumber

        viewModel.area.observe(viewLifecycleOwner){ areas ->
            if (areas.isNotEmpty()){
                val popup = PopupMenu(requireContext(), binding.etArea)
                areas.forEachIndexed { i, area ->
                    popup.menu.add(Menu.NONE, i, i, area.name)
                }
                popup.setOnMenuItemClickListener {
                    val area = areas[it.itemId]
                    viewModel.mlArea.value = area.name
                    viewModel.mlAreaId.value = area.id
                    binding.etArea.error = null
                    return@setOnMenuItemClickListener true
                }
                popup.show()
            }

        }

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(value = [R.id.btn_sign_up, R.id.tv_login, R.id.et_area])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.btn_sign_up ->{
                if(areAllDataValid())//if all data are valid
                    newUserRegistration()
            }

            R.id.tv_login->requireActivity().supportFragmentManager.popBackStack()
            R.id.et_area -> viewModel.loadArea()
        }
    }

    /**
     * ...check all data
     * @return true when all data are valid otherwise return false
     */
    private fun areAllDataValid() : Boolean
    {
        when(viewModel.registrationDataValidation())
        {
            1 -> {
                binding.etFullName.error = "Invalid name"
                binding.etFullName.requestFocus()
                return false
            }

            2 -> {
                binding.etEmail.error = "Invalid email"
                binding.etEmail.requestFocus()
                return false
            }

            3 -> {
                binding.etPhone.error = "Invalid phone"
                binding.etPhone.requestFocus()
                return false
            }

            4 -> {
                binding.etPassword.error = "Invalid password"
                binding.etPassword.requestFocus()
                return false
            }

            5 -> {
                binding.etConfirmPassword.error = "Invalid password"
                binding.etConfirmPassword.requestFocus()
                return false
            }
            6 -> {
                binding.etShopName.error = "Invalid Shop name"
                binding.etShopName.requestFocus()
                return false
            }
            7 -> {
                binding.etShopAddress.error = "Invalid Shop address"
                binding.etShopAddress.requestFocus()
                return false
            }
            8 -> {
                binding.etArea.error = "Invalid Area"
                binding.etArea.requestFocus()
                return false
            }
        }

        return true
    }

    /**
     * ...check internet connection
     * ...start loading
     * ...handle error and server response
     * ...call login method after complete registration
     */
    private fun newUserRegistration()
    {
        if(!isOnline(requireContext()))
        {
            noInternetConnection(binding.cvRoot)
            return
        }

        val alertDialog = loadingView(requireContext())
        alertDialog.show()

        viewModel.createNewUser().observe(requireContext() as LifecycleOwner, {
            if(it.responseCode == RESPONSE_SUCCESS_CODE)
            {
                //userLogin()
                showToast(mContext as Activity, "Registration complete, pending for admin approval.", SUCCESS_DIALOG)
                requireActivity().supportFragmentManager.popBackStack()
            }
            else showToast(mContext as Activity, "Failed to register new user, please retry.",ERROR_DIALOG)

            alertDialog.dismiss()
        })
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
        if(!isOnline(requireContext()))
        {
            noInternetConnection(binding.cvRoot)
            return
        }

        loadingDialog.show()

        viewModel.userLogin().observe(requireContext() as LifecycleOwner, {
            if(it.responseCode == RESPONSE_SUCCESS_CODE)
            {
                spManager.isLoggedIn(true)
                spManager.saveUserInfo(it.userModel)
                spManager.accessToken(it.token)
                ExtraUtils.startActivityIn(mActivity, Intent(mActivity, DashboardActivity::class.java))
                requireActivity().finish()
            }
            else
                errorSnackBar(binding.cvRoot, "Failed to auto Login, Please try again")
            loadingDialog.dismiss()
        })
    }
}