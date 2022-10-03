package com.medicine.remedy.model

import com.medicine.remedy.config.RetrofitClient
import com.medicine.remedy.config.UserApiConfig
import com.medicine.remedy.response.DefaultResponse
import io.reactivex.Single

/**
 * Date 3/27/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class SettingRepository
{
    /**
     * ...Change current user password
     * ...all product list
     * ...return server response
     */
    fun changePassword(oldPassword : String, newPassword : String): Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.changePassword(oldPassword,newPassword)
    }
    fun changePasswordOtp(phoneNumber: String, otp: String, newPassword : String): Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.changePasswordOtp(phoneNumber, otp, newPassword)
    }
}