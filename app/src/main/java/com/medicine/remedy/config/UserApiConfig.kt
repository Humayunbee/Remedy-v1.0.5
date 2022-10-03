package com.medicine.remedy.config

import com.medicine.remedy.data_model.OrderResponse
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.response.NotificationResponse
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
interface UserApiConfig
{
    @GET("api/address_list")
    fun getAddressList() : Single<DefaultResponse>

    @GET("api/address_list")
    suspend fun getAddressList2() : DefaultResponse

    @FormUrlEncoded
    @POST("api/add_new_address")
    fun addNewAddress(
            @Field("title") title: String,
            @Field("address_details") address: String,
            @Field("receiver_name") receiverName: String,
            @Field("contact_no") phoneNumber: String,
            @Field("is_default") isDefault: Int,
            @Field("address_type") addressType: Int
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/update_address")
    fun updateAddress(
            @Field("address_id") addressId: String,
            @Field("title") houseNo: String,
            @Field("address_details") floorNo: String,
            @Field("receiver_name") receiverName: String,
            @Field("contact_no") phoneNumber: String,
            @Field("is_default") isDefault: Int,
            @Field("address_type") addressType: Int
    ) : Single<DefaultResponse>


    @GET("api/getUpazila")
    fun getSubDistrictList() : Single<DefaultResponse>

    @GET("api/coupon_list")
    fun getCouponList() : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/apply_coupon")
    fun applyCoupon(
            @Field("coupon_code") coupon: String
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/price_check")
    fun priceCheckOfProductList(
            @Field("product_id") items: String
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/place_order")
    fun placeOrder(
            @Field("address_id") addressId: String,
            @Field("coupon_id") couponId: String?,
            @Field("items") items: String,
            @Field("total_amount") totalAmount: String,
            @Field("delivery_type") deliveryType: String,
            @Field("discount") discount: String,
    ) : Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/order_history")
    fun getOrderHistory(
            @Field("page") page: Int,
            @Field("size") size: Int
    ) : Single<OrderResponse>

    @FormUrlEncoded
    @POST("api/order_tracking")
    fun getOrderTrack(
            @Field("order_id") page: String
    ) : Single<DefaultResponse>

    @GET("api/notification_list")
    suspend fun getNotificationList(
            @Query("page") page: Int
    ) : DefaultResponse

    @FormUrlEncoded
    @POST("api/notification_details")
    suspend fun getNotification(
        @Field("id") id: String
    ) : NotificationResponse

    @FormUrlEncoded
    @POST("api/change_password")
    fun changePassword(
            @Field("old_password") newPassword: String,
            @Field("new_password") confPassword: String
    ): Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/update_new_pass")
    fun changePasswordOtp(
            @Field("cell_no") cellNo: String,
            @Field("otp") otp: String,
            @Field("new_password") newPassword: String
    ): Single<DefaultResponse>

    @FormUrlEncoded
    @POST("api/product_add_to_favourite")
    fun productAddToFavourite(
            @Field("product_id") prodId: String
    ) : Single<DefaultResponse>

    @GET("api/favourite_products")
    fun getFavouriteProducts(
            @Query("page") page: Int
    ) : Single<DefaultResponse>
}