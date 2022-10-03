package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/3/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class RestaurantModel
{
    @SerializedName("restaurant_id") lateinit var restaurantId : String
    @SerializedName("restaurant_name") var  restaurantName : String? = null
    @SerializedName("restaurant_logo") var  restaurantLogo : String? = null
    @SerializedName("restaurant_rating") val restaurantRating : Double = 0.0
}