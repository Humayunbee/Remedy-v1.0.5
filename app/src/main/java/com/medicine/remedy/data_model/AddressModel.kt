package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class AddressModel
{
    @SerializedName("id") lateinit var addressId : String
    @SerializedName("receiver_name") lateinit var receiverName : String
    @SerializedName("contact_no") lateinit var phoneNumber : String
    @SerializedName("address_details") lateinit var address : String
    @SerializedName("title") lateinit var title : String
    @SerializedName("delivery_charge") var deliveryCharge : Int = 0
    @SerializedName("is_default") var isDefault : Int = 0
    @SerializedName("address_type") var addressType : Int = 0 //[1 for home, 2 for officeAddress, 3 for otherAddress ]
}