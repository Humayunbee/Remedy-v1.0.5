package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class OrderHistoryModel
{
    @SerializedName("invoice_id") lateinit var invoiceId : String
    @SerializedName("order_id") lateinit var orderId : String
    @SerializedName("total_amount")  var totalAmount : Double = 0.0
    @SerializedName("discount_amount")  var discount : Double = 0.0
    @SerializedName("order_status")  var orderStatus : Int = 0
    @SerializedName("order_date") lateinit var orderDate : String
    @SerializedName("delivery_address") lateinit var deliveryAddress : AddressModel

    @SerializedName("confirmed_date") var approveDate : String ?= null
    @SerializedName("on_the_way_date") var pickDate : String ?= null
    @SerializedName("delivered_date") var shipDate : String ?= null
    @SerializedName("canceled_date") var cancelDate : String ?= null
    @SerializedName("address_details") var address : String ?= null
    @SerializedName("coupon_code") var couponCode : String ?= null
}