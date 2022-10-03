package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/21/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class UserModel
{
    @SerializedName("customer_id") lateinit var userId : String
    @SerializedName("customer_name") lateinit var name : String
    @SerializedName("email") lateinit var email : String
    @SerializedName("cell_no") lateinit var phone : String
}