package com.medicine.remedy.view.fragment;

import static com.medicine.remedy.utils.AppConstants.ERROR_DIALOG;
import static com.medicine.remedy.utils.AppConstants.RESPONSE_SUCCESS_CODE;
import static com.medicine.remedy.utils.AppConstants.SUCCESS_DIALOG;
import static com.medicine.remedy.utils.ViewUtils.showToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.medicine.remedy.R;
import com.medicine.remedy.config.BaseFragment;
import com.medicine.remedy.config.SharedPreferenceManager;
import com.medicine.remedy.databinding.LayoutChangePasswordBinding;
import com.medicine.remedy.interfaces.OnRetryCallback;
import com.medicine.remedy.utils.AppConstants;
import com.medicine.remedy.utils.DialogUtils;
import com.medicine.remedy.utils.ExtraUtils;
import com.medicine.remedy.utils.LoadingUtils;
import com.medicine.remedy.view.activity.UserAuthenticationActivity;
import com.medicine.remedy.view_model.SettingViewModel;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordOtpFragment extends BaseFragment {

    private LayoutChangePasswordBinding binding;
    private SettingViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_change_password, container, false);
        View view = binding.getRoot();
        viewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setSetting(viewModel);
        ButterKnife.bind(this, view);
        initialize();

        return view;
    }

    @Override
    public void initialize() {
        viewModel.getMlPhoneNumber().setValue(requireArguments().getString(AppConstants.phoneNumber, ""));
        viewModel.getMlOtp().setValue(requireArguments().getString("otp", ""));
        mContext = getContext();
        mActivity = (Activity) getContext();
        spManager = new SharedPreferenceManager(getContext());
        loadingUtils = new LoadingUtils(getContext());
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_change_password)
    public void onClickChangePassword()
    {
        if(dataValidation())
            changePasswordObserver();
    }

    /**
     * ...validation of required data
     * @return true while all password are valid otherwise false
     */
    private boolean dataValidation()
    {
        int status = viewModel.passwordValidation(1);

        switch(status)
        {
            case 1:
                binding.etOldPassword.setError("Invalid password");
                binding.etOldPassword.requestFocus();
                return false;
            case 2:
                binding.etNewPassword.setError("Invalid password");
                binding.etNewPassword.requestFocus();
                return false;

            case 3:
                binding.etConfirmPassword.setError("Invalid password");
                binding.etConfirmPassword.requestFocus();
                return false;
        }

        return  true;
    }

    /**
     * ...change password observer
     * ...check internet connection
     * ...only executed while all data are valid
     */
    private void changePasswordObserver()
    {
        if(!ExtraUtils.isOnline(mContext))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack);
            return;
        }
        loadingUtils.showProgressDialog();

        viewModel.changePasswordOtp().observe(getViewLifecycleOwner(), response -> {
            if(response.getResponseCode() == RESPONSE_SUCCESS_CODE)
            {
                showToast(mActivity, "Password successfully change", SUCCESS_DIALOG);
                startActivity(new Intent(mActivity, UserAuthenticationActivity.class));
                mActivity.finish();
            }else showToast(mActivity, "Failed to change password", ERROR_DIALOG);
            loadingUtils.dismissProgressDialog();
        });
    }

    /**
     * ...resend request after internet connection
     * ...if all data are valid then resend request
     */
    private final OnRetryCallback callBack = this::changePasswordObserver;
}

