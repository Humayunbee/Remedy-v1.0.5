package com.medicine.remedy.response

import com.google.gson.annotations.SerializedName

data class AreaResponse(
    @SerializedName("status") val responseCode : Int = 0,
    @SerializedName("message") val message : String,
    @SerializedName("areas") val areas : List<Area>,
)

data class Area(
    @SerializedName("area_id") val id : String,
    @SerializedName("area_name") val name : String,
    @SerializedName("zone_number") val zoneNumber : String,
)
