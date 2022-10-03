package com.medicine.remedy.config

import android.content.Context
import android.content.SharedPreferences
import com.medicine.remedy.data_model.AddressModel
import com.medicine.remedy.data_model.TokenInfoModel
import com.medicine.remedy.data_model.UserModel

/**
 * Date 12/29/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class SharedPreferenceManager(con: Context)
{
    private val context = con
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val IS_LOGGED_IN = "is_logged_in" //is user logged in
    private val USER_INFO = "user_info" //store user information
    private val ADDRESS_INFO = "address_info" //store address information
    private val TOKEN = "token" //store user access token
    private val TOKEN_REGISTRATION = "device_token" //device token registration

    //================================================ STORE DATA ==============================================

    /**
     * save is user logged in
     * @param isLoggedIn log in status
     */
    fun isLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences = context.getSharedPreferences(IS_LOGGED_IN, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    /**
     * ...is firebase token register
     * @return true while token is register otherwise return false
     */
    fun getTokenRegisterStatus(): Boolean {
        sharedPreferences = context.getSharedPreferences(TOKEN_REGISTRATION, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isRegistered", false)
    }

    /**
     * store user access token
     * @param token user access token
     */
    fun accessToken(token: TokenInfoModel) {
        sharedPreferences = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString("token", token.token)
        editor.putString("expire_time", token.expireAt)
        editor.apply()
    }

    fun saveUserInfo(userInfo: UserModel) {
        sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString("userId", userInfo.userId)
        editor.putString("phone", userInfo.phone)
        editor.putString("name", userInfo.name)
        editor.putString("email", userInfo.email)
        editor.apply()
    }

    fun saveDefaultAddressInfo(address: AddressModel) {
        sharedPreferences = context.getSharedPreferences(ADDRESS_INFO, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString("address_id", address.addressId)
        editor.putString("address", address.address)
        //editor.putString("upazila_id", address.subDistrictId)
        editor.apply()
    }

    //================================================ GET DATA ==============================================

    /**
     * get user login status
     * @return true if user logged in otherwise return false
     */
    fun getIsUserLoggedIn(): Boolean {
        sharedPreferences = context.getSharedPreferences(IS_LOGGED_IN, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    /**
     * get user id
     * @return user id
     */
    fun getUserId(): String? {
        sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
    }

    /**
     * get user full name
     * @return user full name
     */
    fun getUserFullName(): String? {
        sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString("name", null)
    }

    /**
     * get user phone
     * @return user phone
     */
    fun getUserPhone(): String? {
        sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString("phone", null)
    }

    /**
     * get user image
     * @return user image
     */
    fun getUserImage(): String? {
        sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString("image", null)
    }

    /**
     * get user access token
     * @return access token
     */
    fun getUserAccessToken(): String? {
        sharedPreferences = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }

    /**
     * get default address id
     * @return address id
     */
    fun getDefaultAddressId():  String? {
        sharedPreferences = context.getSharedPreferences(ADDRESS_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString("address_id", null)
    }

    /**
     * get default address id
     * @return address id
     */
    fun getDefaultAddress():  String? {
        sharedPreferences = context.getSharedPreferences(ADDRESS_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString("address", null)
    }

    /**
     * get delivery charge
     * @return delivery charge
     */
    fun getDefaultDeliveryCharge():  Int {
        sharedPreferences = context.getSharedPreferences(ADDRESS_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getInt("delivery_charge", 0)
    }

    /**
     * get default address subDistrictId
     * @return subDistrictId
     */
    fun getUpazillaId():  String? {
        sharedPreferences = context.getSharedPreferences(ADDRESS_INFO, Context.MODE_PRIVATE)
        return sharedPreferences.getString("upazila_id", null)
    }

    //store status when device token registered, if token registered store true otherwise false
    fun deviceTokenRegistration(status: Boolean) {
        sharedPreferences = context.getSharedPreferences(TOKEN_REGISTRATION, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putBoolean("isRegistered", status)
        editor.apply()
    }

    //clear all data when user log out
    //clear all stored data when user log out
    fun clearData() {
        val prefNames = listOf(IS_LOGGED_IN, USER_INFO, TOKEN,ADDRESS_INFO)
        for (prefName in prefNames) {
            sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
        }
    }
}