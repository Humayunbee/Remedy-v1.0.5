package com.medicine.remedy.model

import com.google.gson.annotations.SerializedName

class BrandModel
{
    @SerializedName("brand_id") lateinit var brandId : String
    @SerializedName("brand_name") lateinit var brandName : String

    var isSelected = false
}