package com.medicine.remedy.model

import android.util.Log
import com.medicine.remedy.config.ApiConfig
import com.medicine.remedy.config.BaseApplication
import com.medicine.remedy.config.DatabaseClient
import com.medicine.remedy.config.RetrofitClient
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.utils.AppConstants
import io.reactivex.Single

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/31/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ProductRepository
{
    /**
     * ...get product  description
     * @param productId product id
     * @return server response
     */
    fun getProductDescription(productId: String) : Single<DefaultResponse>
    {
        val config : ApiConfig = RetrofitClient.getRetrofitClient().create(ApiConfig::class.java)

        return config.getProductDescription(productId)
    }

    /**
     * ...get all product list from server
     */
    fun productList(pageNumber : Int) : Single<DefaultResponse>
    {
        val config : ApiConfig = RetrofitClient.getRetrofitClient().create(ApiConfig::class.java)

        return config.getAllProductList(pageNumber, AppConstants.OFFSET_SIZE_PER_PAGE + 1)
    }

    /**
     * ...get all product list from server
     */
    fun categoryWiseProductList(categoryId : String,pageNumber : Int) : Single<DefaultResponse>
    {
        val config : ApiConfig = RetrofitClient.getRetrofitClient().create(ApiConfig::class.java)

        return config.categoryWiseProductList(categoryId,pageNumber, AppConstants.OFFSET_SIZE_PER_PAGE + 1)
    }

    fun searchProduct(searchKey : String) : Single<DefaultResponse>
    {
        val config : ApiConfig = RetrofitClient.getRetrofitClient().create(ApiConfig::class.java)

        return config.searchProduct(searchKey)
    }

    suspend fun searchSuggestion(searchKey : String) : DefaultResponse
    {
      return RetrofitClient.getRetrofitClient().create(ApiConfig::class.java).searchSuggestion(searchKey)

    }

    /**
     * ...get count of total cart
     * @param cartItems product information
     */
    fun getTotalCartItem() : Long
    {
        return DatabaseClient.getInstance(BaseApplication.mContext).appDatabase.cartItemDao()
            .countTotalCartItem()
    }

    fun searchProductByBrand(searchKey : String) : Single<DefaultResponse>
    {
        Log.d("brandId",searchKey)
        val config : ApiConfig = RetrofitClient.getRetrofitClient().create(ApiConfig::class.java)

        return config.searchProductByBrand(searchKey)
    }
}