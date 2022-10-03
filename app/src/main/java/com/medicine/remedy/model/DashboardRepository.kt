package com.medicine.remedy.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.medicine.remedy.config.ApiConfig
import com.medicine.remedy.config.BaseApplication
import com.medicine.remedy.config.DatabaseClient
import com.medicine.remedy.config.RetrofitClient
import com.medicine.remedy.response.DefaultResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/22/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class DashboardRepository
{
    /**
     * ...get dashboard data.
     * @return server response
     */
    fun getDashboardData() : Single<DefaultResponse>
    {
        val config : ApiConfig = RetrofitClient.getRetrofitClient().create(ApiConfig::class.java)

        return config.dashboardData()
    }

    /**
     * ...get count of total cart
     * @param cartItems product information
     */
    fun getTotalCartItem() : Long
    {
        return DatabaseClient.getInstance(BaseApplication.mContext).appDatabase.cartItemDao()
            .countTotalCartItem()
    }

    fun getBrandsList() : Single<DefaultResponse>
    {
        val config : ApiConfig = RetrofitClient.getRetrofitClient().create(ApiConfig::class.java)

        return config.getBrandList()
    }

    /**
     * store device token
     * @param token, device token
     */
    fun insertDeviceToken(token: String):  Single<DefaultResponse> {
        val config : ApiConfig = RetrofitClient.getRetrofitClient().create(ApiConfig::class.java)

        return config.registerDeviceToken(token)
    }
}