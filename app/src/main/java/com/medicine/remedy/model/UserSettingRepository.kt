package com.medicine.remedy.model

import com.medicine.remedy.config.ApiConfig
import com.medicine.remedy.config.RetrofitClient.Companion.getRetrofitClient
import com.medicine.remedy.response.DefaultResponse
import io.reactivex.Single

/**
 * Date 12/22/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class UserSettingRepository
{
    /**
     * ...create new user
     * ...send request to server
     * @return server response observer
     */
    fun userRegistration(fullName: String, email: String,phoneNumber: String, shopName: String, shopAddress: String, areaId: String, password: String) : Single<DefaultResponse>
    {
        val config : ApiConfig = getRetrofitClient().create(ApiConfig::class.java)

        return config.userRegistration(fullName, email,phoneNumber, shopName, shopAddress,areaId, password)
    }

    /**
     * ...duplicate phone number checking
     * ...send request to server
     */
    fun duplicatePhoneNumberCheck(phoneNumber: String) : Single<DefaultResponse>
    {
        val config : ApiConfig = getRetrofitClient().create(ApiConfig::class.java)

        return config.phoneNumberDuplicateCheck(phoneNumber)
    }

    /**
     * ...send user log in request
     * @param phoneNumber user phone phoneNumber
     * @param password user password
     * @return server response
     */
    fun userLogin(phoneNumber: String, password : String) : Single<DefaultResponse>
    {
        val config : ApiConfig = getRetrofitClient().create(ApiConfig::class.java)

        return config.userLogin(phoneNumber,password)
    }

    /**
     * ...send otp to phone number
     * ...return server response
     */
    fun sendOtp(phoneNumber: String) : Single<DefaultResponse>
    {
        val config : ApiConfig = getRetrofitClient().create(ApiConfig::class.java)

        return config.sendOtp(phoneNumber)
    }

    /**
     * ...check otp and phone number
     * ...return server response
     */
    fun checkOtp(phoneNumber: String,otp : String) : Single<DefaultResponse>
    {
        val config : ApiConfig = getRetrofitClient().create(ApiConfig::class.java)

        return config.checkOtp(phoneNumber,otp)
    }
}