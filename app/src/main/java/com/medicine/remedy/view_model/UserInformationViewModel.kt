package com.medicine.remedy.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.medicine.remedy.config.BaseAndroidViewModel
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.data_model.AddressModel
import com.medicine.remedy.model.UserInfoRepository
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.response.NotificationResponse
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.DataValidation
import com.medicine.remedy.utils.DataValidation.Companion.inputTextValidator
import com.medicine.remedy.utils.HttpErrorCode
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/2/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class UserInformationViewModel(application: Application) : BaseAndroidViewModel(application) {

    companion object{
        var mlSubDistrictId = MutableLiveData<String>()
        var addressModel = AddressModel()
    }

    private val repository = UserInfoRepository()

    var mlTitle = MutableLiveData<String>()
    var mlAddress = MutableLiveData<String>()
    var mlReceiverName = MutableLiveData<String>()
    var mlPhoneNo = MutableLiveData<String>()
    var mlAddressId = MutableLiveData<String>()
    var mlAddressType = MutableLiveData<Int>()
    var mlIsDefaultAddress = MutableLiveData<Int>()

    init {
        validation = DataValidation()
        disposables = CompositeDisposable()
        spManager = SharedPreferenceManager(application)
        mlAddressType.value = 0
    }

    /**
     * ...get address list from remote db
     * ...add disposables
     * ...get error code if any error happen onCompleteOtpVerification server side
     * ...set appropriate error code
     */
    fun getAddressList() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.getAddressList()
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
     * ...validation of full address
     * ...check any fields is empty or not
     * ...return error code while any fields is empty
     * ...while all data are valid return success code
     */
    fun validationOfAddress() : Int
    {
        if(mlTitle.value == null || !inputTextValidator(mlTitle.value!!))
            return 1

        if(mlAddress.value == null || !inputTextValidator(mlAddress.value!!))
            return 2

        if(mlReceiverName.value == null || !inputTextValidator(mlReceiverName.value!!))
            return 6

        if(mlPhoneNo.value == null || !validation.phoneNumberValidation(mlPhoneNo.value!!) || !inputTextValidator(mlPhoneNo.value!!))
            return 7

        if(mlAddressType.value == null || mlAddressType.value!! == 0)
            return 8

        return RESPONSE_SUCCESS_CODE
    }

    /**
     * ...add new address
     * ...pass address related parameters
     * ...initialize Disposable
     * ...get error code while any error occurred
     * ...return server response
     */
    fun addNewAddress(isDefault: Boolean) : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.addNewAddress(mlTitle.value.toString(), mlAddress.value.toString(),
                mlReceiverName.value.toString(),mlPhoneNo.value.toString(),
                if(isDefault)1 else 0, mlAddressType.value!!)
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
     * ...get address list from remote db
     * ...add disposables
     * ...get error code if any error happen onCompleteOtpVerification server side
     * ...set appropriate error code
     */
    fun getSubDistrictList() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.getSubDistrictList()
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
     * ...update address
     * ...pass address related parameters
     * ...initialize Disposable
     * ...get error code while any error occurred
     * ...return server response
     */
    fun updateAddress(isDefault: Boolean) : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.updateAddress(mlAddressId.value.toString(),mlTitle.value.toString(),mlAddress.value.toString(), mlReceiverName.value.toString(),mlPhoneNo.value.toString(),
                if(isDefault)1 else 0, mlAddressType.value!!)
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
     * ...address information
     */
    fun addressInfo()
    {
        mlTitle.value = addressModel.title
        mlAddress.value = addressModel.address
        mlReceiverName.value = addressModel.receiverName
        mlPhoneNo.value = addressModel.phoneNumber
        mlAddressType.value = addressModel.addressType
        mlIsDefaultAddress.value = addressModel.isDefault
    }

    private val _notifications = MutableLiveData<DefaultResponse>()
    val notifications: LiveData<DefaultResponse>
        get() = _notifications

    fun loadNotifications(){
        viewModelScope.launch {
            try {
                val resp = repository.getNotificationList(1)
                _notifications.value = resp
                DashboardViewModel.mlNotiCount.postValue(resp.notificationList.filter { it.readStatus == null }.size)
            }catch (e: Exception){
                _notifications.value = DefaultResponse().apply { responseCode =  HttpErrorCode.GET_HTTP_ERROR_CODE(e)}
            }

        }

    }

    private val _notificationDetails = MutableLiveData<NotificationResponse>()
    val notificationDetails: LiveData<NotificationResponse>
        get() = _notificationDetails

    fun viewNotification(notificationId: String) {
        viewModelScope.launch {
            try {
                val resp = repository.getNotification(notificationId)
                _notificationDetails.value = resp
                loadNotifications()
            }catch (e: Exception){ }
        }
    }



    /**
     * ...get favourite product list from remote db
     * ...add disposables
     * ...get error code if any error happen onCompleteOtpVerification server side
     * ...set appropriate error code
     */
    fun getFavouriteProductList() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.getFavouriteProductList(1)
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

}