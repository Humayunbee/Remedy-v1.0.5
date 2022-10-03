package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/3/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ProductModel
{
    @SerializedName("product_id") lateinit var productId : String
    @SerializedName("category_id") lateinit var categoryId : String
    @SerializedName("sub_category_id") lateinit var subCategoryId : String
    @SerializedName("sub_category_name") var subCategoryName : String ?= null
    @SerializedName("product_name")  lateinit var productName : String
    @SerializedName("main_image") var productIcon : String? = null
    @SerializedName("brand_name") var brandName : String? = null
    @SerializedName("category_name") var categoryName : String? = null
    @SerializedName("product_rate") var productPrice : Double = 0.0
    @SerializedName("mrp_rate") var mrpRate : Double = 0.0
    @SerializedName("discount") var productDiscount : Double = 0.0
    @SerializedName("sales_quantity_limit") var salesQtyLimit : Int = 0
    @SerializedName("product_unit") var productUnit : String? = null
    @SerializedName("pack_size") var packSize : String? = null
}