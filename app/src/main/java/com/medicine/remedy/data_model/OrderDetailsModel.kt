package com.medicine.remedy.data_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/7/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class OrderDetailsModel
{
    @Expose  @SerializedName("product_id") lateinit var productId : String
    @Expose  @SerializedName("product_name") lateinit var productName : String
    @Expose  @SerializedName("sales_rate")  var rate : Double = 0.0
    @Expose  @SerializedName("discount")  var discount : Double = 0.0
    @Expose  @SerializedName("qty")  var qty : Int = 0
}