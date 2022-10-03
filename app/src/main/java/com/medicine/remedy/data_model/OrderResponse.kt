package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class OrderResponse
{
    @SerializedName("status") var responseCode : Int = 0
    @SerializedName("message") lateinit var message : String
    @SerializedName("order_info") lateinit var orderHistory : List<OrderHistoryModel>
}