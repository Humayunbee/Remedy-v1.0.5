package com.medicine.remedy.view_model

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.medicine.remedy.config.ApiConfig
import com.medicine.remedy.config.BaseAndroidViewModel
import com.medicine.remedy.config.RetrofitClient
import com.medicine.remedy.model.UserSettingRepository
import com.medicine.remedy.response.Area
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.DataValidation
import com.medicine.remedy.utils.DataValidation.Companion.inputTextValidator
import com.medicine.remedy.utils.DataValidation.Companion.modifyPhoneNumber
import com.medicine.remedy.utils.DataValidation.Companion.otpNumberValidation
import com.medicine.remedy.utils.HttpErrorCode
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

/**
 * Date 1/11/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
open class UserAuthenticationViewModel(application: Application) : BaseAndroidViewModel(application)
{
    var mlPhoneNumber : MutableLiveData<String> = MutableLiveData<String>()
    var mlPassword : MutableLiveData<String> = MutableLiveData<String>()
    var mlFullName : MutableLiveData<String> = MutableLiveData<String>()
    var mlShopName : MutableLiveData<String> = MutableLiveData<String>()
    var mlShopAddress : MutableLiveData<String> = MutableLiveData<String>()
    var mlArea : MutableLiveData<String> = MutableLiveData<String>()
    var mlAreaId : MutableLiveData<String?> = MutableLiveData<String?>()
    var mlEmail : MutableLiveData<String> = MutableLiveData<String>()
    var mlConfirmPassword : MutableLiveData<String> = MutableLiveData<String>()
    var mlUserPhoneNumber : MutableLiveData<String> = MutableLiveData<String>()
    var mlOtp : MutableLiveData<String> = MutableLiveData<String>()

    private val settingRepository = UserSettingRepository()

    init {
        validation = DataValidation()
        disposables = CompositeDisposable()
    }

    /**
     * ...6 digits otp number and phone number validation
     */
    fun otpInfoValidation() : Int
    {
        if(mlUserPhoneNumber.value == null || !validation.phoneNumberValidation(mlUserPhoneNumber.value.toString()))
            return 1

        if(mlOtp.value == null || TextUtils.isEmpty(mlOtp.value) || !otpNumberValidation(mlOtp.value.toString()))
            return 2

        return RESPONSE_SUCCESS_CODE
    }

    /**
     * ...required data validation
     * ...phone number validation must be followed phone number policy
     * ...password validation, must be followed password policy
     * @return success code if all data are valid otherwise return error code
     */
    fun loginDataValidations(): Int{

        if(mlPhoneNumber.value == null || !validation.phoneNumberValidation(mlPhoneNumber.value!!) || !inputTextValidator(mlPhoneNumber.value!!))
            return 1

        if(mlPassword.value == null || !validation.passwordValidation(mlPassword.value.toString()) || !inputTextValidator(mlPassword.value!!))
            return 2

        return RESPONSE_SUCCESS_CODE
    }

    /**
     * ...required data validation
     * ...name must followed name format
     * ...email must be valid and email may optionally
     * ...phone number validation must be followed phone number policy
     * ...password validation, must be followed password policy
     * ...confirmation password must be matched with password
     * @return success code if all data are valid otherwise return error code
     */
    fun registrationDataValidation() : Int
    {
        if(mlFullName.value == null || !validation.nameValidation(mlFullName.value.toString()))
            return 1

        if(mlEmail.value != null)
        {
            if(!validation.emailValidation(mlEmail.value!!))
                return 2
        }

        if(mlPhoneNumber.value == null || !validation.phoneNumberValidation(mlPhoneNumber.value.toString()))
            return 3

        if(mlShopName.value.isNullOrEmpty() || !inputTextValidator(mlShopName.value.toString()))
            return 6

        if(mlShopAddress.value.isNullOrEmpty() || !inputTextValidator(mlShopAddress.value.toString()))
            return 7

        if(mlAreaId.value.isNullOrEmpty())
            return 8

        if(mlPassword.value == null || !validation.passwordValidation(mlPassword.value.toString()))
            return 4

        if(mlConfirmPassword.value == null ||
                !validation.passwordValidation(mlConfirmPassword.value.toString()) ||
                (mlPassword.value.toString() != mlConfirmPassword.value.toString()))
            return 5


        return RESPONSE_SUCCESS_CODE
    }

    /**
     * ...create new user
     * ...handle error response
     * ...handle server response
     * @return server response
     */
    fun createNewUser() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        //settingRepository.userRegistration("mlFullName.value!!", "mlEmail.value!!", "mlPhoneNumber.value!!", "mlPassword.value!!")
        settingRepository.userRegistration(
            mlFullName.value.toString(),
            mlEmail.value.toString().trim(),
            mlPhoneNumber.value.toString().trim(),
            mlShopName.value.toString(),
            mlShopAddress.value.toString(),
            mlAreaId.value.toString(),
            mlPassword.value.toString()
        )
            .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<DefaultResponse> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onSuccess(t: DefaultResponse) {
                        mlResponse.value = t
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

    /**
     * ...user login request
     *
     */
    fun userLogin() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        settingRepository.userLogin(mlPhoneNumber.value.toString(), mlPassword.value.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<DefaultResponse> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onSuccess(t: DefaultResponse) {
                        mlResponse.value = t
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

    /**
     * ...send otp to entered phone number.
     * ...return server response
     */
    fun sendOtp() : MutableLiveData<DefaultResponse>
    {
        val mlResponse = MutableLiveData<DefaultResponse>()

        settingRepository.sendOtp(modifyPhoneNumber(mlPhoneNumber.value.toString()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse>
            {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onSuccess(response: DefaultResponse) {
                    mlResponse.value = response
                }

                override fun onError(e: Throwable) {
                    val defaultResponse = DefaultResponse()
                    defaultResponse.responseCode = HttpErrorCode.GET_HTTP_ERROR_CODE(e)
                    mlResponse.value = defaultResponse
                }
            })

        return mlResponse
    }

    /**
     * ...check otp
     * ...send a request to server for validate otp
     */
    fun checkOtp() : MutableLiveData<DefaultResponse>
    {
        val mlResponse = MutableLiveData<DefaultResponse>()

        settingRepository.checkOtp(modifyPhoneNumber(mlUserPhoneNumber.value.toString()), mlOtp.value.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse>
            {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onSuccess(response: DefaultResponse) {
                    mlResponse.value = response
                }

                override fun onError(e: Throwable) {
                    val defaultResponse = DefaultResponse()
                    defaultResponse.responseCode = HttpErrorCode.GET_HTTP_ERROR_CODE(e)
                    mlResponse.value = defaultResponse
                }
            })

        return mlResponse
    }

    val area = MutableLiveData<List<Area>>()
    val areaHelperText = MutableLiveData<String?>()
    fun loadArea() {
        val config : ApiConfig = RetrofitClient.getRetrofitClient().create(ApiConfig::class.java)
        viewModelScope.launch {
            try {
                areaHelperText.value = "Loading..."
                val resp = config.area()
                area.value =  resp.areas
                areaHelperText.value = null
            } catch(e: Exception) {
                areaHelperText.value = "Can't load try again"
            }
        }

    }
}