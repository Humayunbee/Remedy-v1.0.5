package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/1/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ProductDescriptionModel
{
    @SerializedName("product_id") lateinit var productId : String
    @SerializedName("product_name") lateinit var productName : String
    @SerializedName("category_id") lateinit var categoryId : String
    @SerializedName("sub_category_id") lateinit var subCategoryId : String
    @SerializedName("sub_category_name") lateinit var subCategory : String
    @SerializedName("main_image") var mainImage : String ?= null
    @SerializedName("product_rate") var productPrice : Double = 0.0
    @SerializedName("mrp_rate") var mrpRate : Double = 0.0
    @SerializedName("product_images") var productIcons = ArrayList<String>()
    @SerializedName("discount") var discount : Double = 0.0
    @SerializedName("sales_quantity_limit") var maxQty : Int = 0


    @SerializedName("product_details") var longDescription : String ?= null
    @SerializedName("specifications_features") var specificationFeature : String ?= null
    @SerializedName("direction_for_use") var directionOfUse : String ?= null
    @SerializedName("safety_information") var safetyInformation : String ?= null
    @SerializedName("product_unit") var productUnit : String? = null
    @SerializedName("pack_size") var packSize : String? = null
}