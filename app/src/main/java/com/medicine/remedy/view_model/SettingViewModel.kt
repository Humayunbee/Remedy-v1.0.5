package com.medicine.remedy.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.medicine.remedy.config.BaseAndroidViewModel
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.model.SettingRepository
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.AppConstants.Companion.nameNotFound
import com.medicine.remedy.utils.AppConstants.Companion.notFound
import com.medicine.remedy.utils.AppConstants.Companion.phoneNotFound
import com.medicine.remedy.utils.DataValidation
import com.medicine.remedy.utils.HttpErrorCode
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/6/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class SettingViewModel(application: Application) : BaseAndroidViewModel(application)
{
    var isUserLoggedIn = MutableLiveData<Boolean>()
    var mlUserName = MutableLiveData<String>()
    var mlPhoneNumber = MutableLiveData<String>()
    var mlOtp = MutableLiveData<String>()
    var mlUserImage = MutableLiveData<String>()

    var mlOldPassword  = MutableLiveData<String>()
    var mlNewPassword = MutableLiveData<String>()
    var mlConfirmPassword = MutableLiveData<String>()

    var repository = SettingRepository()

    init {
        mContext = application
        disposables = CompositeDisposable()
        validation = DataValidation()
        spManager = SharedPreferenceManager(application)
        isUserLoggedIn()
    }

    /**
     * ...check is any user logged in or not
     */
    fun isUserLoggedIn()
    {
        isUserLoggedIn.value = spManager.getIsUserLoggedIn()
    }

    /**
     * ...get current user information from shared preferences
     * ...get user name, phone number, userImage
     * ...initialize mutableLiveData variableSet
     */
    fun getCurrentUserInformation()
    {
        mlUserName.value = if(spManager.getUserFullName() != null) spManager.getUserFullName() else nameNotFound
        mlPhoneNumber.value = if(spManager.getUserPhone() != null) spManager.getUserPhone() else phoneNotFound
        mlUserImage.value = if(spManager.getUserImage() != null) spManager.getUserImage() else notFound
    }

    /**
     * ...validation of password
     * ...password must follow password policy
     * ...confirm password must be same of new password
     * ...old password must be correct
     */
    fun passwordValidation(type: Int): Int
    {

        if (type != 1){
            if(mlOldPassword.value == null || !validation.passwordValidation(mlOldPassword.value!!))
            {
                return 1
            }
        }

        if(mlNewPassword.value == null || !validation.passwordValidation(mlNewPassword.value!!))
        {
            return 2
        }

        if(mlConfirmPassword.value == null || !validation.passwordValidation(mlConfirmPassword.value!!) || mlNewPassword.value!! != mlConfirmPassword.value!!)
        {
            return 3
        }

        return RESPONSE_SUCCESS_CODE
    }

    /**
     * ...change password
     * ...change current user password by userName, oldPassword and new password
     * ...return server response
     */
    fun changePassword() : MutableLiveData<DefaultResponse>
    {
        val mlResponse = MutableLiveData<DefaultResponse>()
        repository.changePassword(mlOldPassword.value!!,mlNewPassword.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onSuccess(response: DefaultResponse) {
                    mlResponse.value = response
                }

                override fun onError(e: Throwable) {
                    Log.d("error_response", e.toString())
                    val response = DefaultResponse()
                    response.responseCode = HttpErrorCode.GET_HTTP_ERROR_CODE(e)
                    mlResponse.value = response
                }
            })

        return mlResponse
    }

    fun changePasswordOtp() : MutableLiveData<DefaultResponse>
    {
        val mlResponse = MutableLiveData<DefaultResponse>()
        repository.changePasswordOtp(mlPhoneNumber.value.toString(), mlOtp.value.toString(), mlNewPassword.value.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onSuccess(response: DefaultResponse) {
                    mlResponse.value = response
                }

                override fun onError(e: Throwable) {
                    Log.d("error_response", e.toString())
                    val response = DefaultResponse()
                    response.responseCode = HttpErrorCode.GET_HTTP_ERROR_CODE(e)
                    mlResponse.value = response
                }
            })

        return mlResponse
    }
}