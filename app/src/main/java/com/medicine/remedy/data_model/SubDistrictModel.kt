package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class SubDistrictModel
{
    @SerializedName("upazila_id") lateinit var subDistrictId : String
    @SerializedName("name") lateinit var name : String
    @SerializedName("delivery_charge") var deliveryCharge : Double = 0.0
}