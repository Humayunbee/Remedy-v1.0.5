package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/9/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class NotificationModel
{
    @SerializedName("id") lateinit var notificationId : String
    @SerializedName("title") lateinit var title : String
    @SerializedName("details") lateinit var description : String
    @SerializedName("link") lateinit var link : String
    @SerializedName("status")  var status : Int = 0
    @SerializedName("read_status")  var readStatus : Int? = null
    @SerializedName("created") lateinit var createdAt : String
}