package com.medicine.remedy.config

import com.medicine.remedy.response.AreaResponse
import com.medicine.remedy.response.DefaultResponse
import io.reactivex.Single
import retrofit2.http.*

/**
 * Date 12/26/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
interface ApiConfig
{
    @FormUrlEncoded
    @POST("api/login")
    fun userLogin(
            @Field("cell_no") phone: String,
            @Field("password") password: String
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/register")
    fun userRegistration(
        @Field("customer_name") name: String,
        @Field("email") email: String,
        @Field("cell_no") phone: String,
        @Field("shop_name") shopName: String,
        @Field("address") shopAddress: String,
        @Field("area_id") areaId: String,
        @Field("password") password: String
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("phoneCheck")
    fun phoneNumberDuplicateCheck(
            @Field("phone") phone: String
    ) : Single<DefaultResponse>

    @GET("api/dashboard")
    fun dashboardData() : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/product_description")
    fun getProductDescription(
            @Field("product_id") productId: String
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/all_product")
    fun getAllProductList(
        @Field("page") page: Int,
        @Field("size") size: Int
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/sub_cat_products")
    fun categoryWiseProductList(
        @Field("sub_cat_id") productId: String,
        @Field("page") page: Int,
        @Field("size") size: Int
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/search_product")
    fun searchProduct(
        @Field("keyword") page: String
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/search_product")
    suspend fun searchSuggestion(
        @Field("keyword") page: String
    ) : DefaultResponse

    @FormUrlEncoded
    @POST("api/send_otp")
    fun sendOtp(@Field("cell_no") contactNumber: String) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/check_otp")
    fun checkOtp(
        @Field("cell_no") contactNumber: String,
        @Field("otp") otp: String
    ) : Single<DefaultResponse>

    @GET("api/brands")
    fun getBrandList() : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/search_brand_products")
    fun searchProductByBrand(
        @Field("keyword") keyword: String
    ) : Single<DefaultResponse>

    @GET("api/area")
    suspend fun area(): AreaResponse

    @FormUrlEncoded
    @POST("api/register_firebase_token")
    fun registerDeviceToken(
        @Field("firebase_token") token: String
    ): Single<DefaultResponse>

}