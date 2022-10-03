package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class CouponModel
{
    @SerializedName("id") lateinit var couponId : String
    @SerializedName("title") lateinit var details : String
    @SerializedName("coupon_code") lateinit var couponCode : String
    @SerializedName("discount_per")  var discount : Double = 0.0
    @SerializedName("discount_flat") var flagDiscount : Double = 0.0
    @SerializedName("maximum_discount") var maxDiscount : Double = 0.0
    @SerializedName("minimum_order_amount") var minOrderAmount : Double = 0.0
    @SerializedName("status")  var status : Int = 0
    @SerializedName("expire_date") lateinit var expiryDate : String
    @SerializedName("maximum_use")  var maximum : Int = 0
    @SerializedName("used")  var used : Int = 0

    var isApplied = false
}