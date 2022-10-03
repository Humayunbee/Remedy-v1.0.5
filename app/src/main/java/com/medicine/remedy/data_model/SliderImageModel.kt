package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/3/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class SliderImageModel
{
    @SerializedName("id") lateinit var imageId : String
    @SerializedName("title") lateinit var imageTitle : String
    @SerializedName("image") var imageLink : String? = null
    @SerializedName("status") val imageStatus: Int = 0
}