package com.medicine.remedy.model

import androidx.lifecycle.MutableLiveData
import com.medicine.remedy.config.RetrofitClient
import com.medicine.remedy.config.UserApiConfig
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.response.NotificationResponse
import com.medicine.remedy.utils.AppConstants
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/2/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class UserInfoRepository
{
    /**
     * ...get address list from remote db
     */
    fun getAddressList() : Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.getAddressList()
    }

    /**
     * ...add new address
     * ...pass address related parameters
     * ...send request to remote db
     */
    fun addNewAddress(title : String, address : String, receiverName : String, phoneNumber : String,isDefault : Int, addressType : Int) : Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.addNewAddress(title,address,receiverName,phoneNumber,isDefault,addressType)
    }

    /**
     * ...get sub district list from remote db
     */
    fun getSubDistrictList() : Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.getSubDistrictList()
    }

    /**
     * ...add new address
     * ...pass address related parameters
     * ...send request to remote db
     */
    fun updateAddress(addressId: String, title : String, address : String, receiverName : String, phoneNumber : String,isDefault : Int, addressType : Int) : Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.updateAddress(addressId,title,address,receiverName,phoneNumber,isDefault,addressType)
    }

    /**
     * ...get coupon list from remote db
     */
    fun getCouponList() : Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.getCouponList()
    }

    /**
     * ...get notification list
     */
    suspend fun getNotificationList( page: Int) : DefaultResponse
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.getNotificationList( page)
    }

    suspend fun getNotification(notificationId: String): NotificationResponse {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.getNotification(notificationId)
    }

    /**
     * ...product id
     * ...product add to favourite
     */
    fun productAddToFavourite( productId: String) : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        config.productAddToFavourite( productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onSuccess(t: DefaultResponse) {
                    mlResponse.value = t
                }

                override fun onError(e: Throwable) {
                    val response = DefaultResponse()
                    response.responseCode = AppConstants.SERVER_ERROR_CODE //GET_HTTP_ERROR_CODE(e)
                    mlResponse.value = response
                }
            })

        return mlResponse
    }

    /**
     * ...get favourite product list
     */
    fun getFavouriteProductList( page: Int) : Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.getFavouriteProducts( page)
    }
}