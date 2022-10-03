package com.medicine.remedy.response

import com.medicine.remedy.data_model.*
import com.google.gson.annotations.SerializedName


data class NotificationResponse(
    @SerializedName("status") var responseCode : Int = 0,
    @SerializedName("message")  var message : String,
    @SerializedName("data") var data : NotificationModel
)