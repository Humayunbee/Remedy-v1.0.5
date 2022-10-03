package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/1/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class TokenInfoModel
{
    @SerializedName("expire_time") lateinit var expireAt : String
    @SerializedName("token") lateinit var token : String
}