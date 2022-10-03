package com.medicine.remedy.data_model

import com.google.gson.annotations.SerializedName

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/3/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class CategoryModel
{
    @SerializedName("id") lateinit var subCategoryId : String
    @SerializedName("category_id") lateinit var categoryId : String
    @SerializedName("category_name") lateinit var categoryName : String
    @SerializedName("name") lateinit var categoryTitle : String
    @SerializedName("items") lateinit var productList : List<ProductModel>

}