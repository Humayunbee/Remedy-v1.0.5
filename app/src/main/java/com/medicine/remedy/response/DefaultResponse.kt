package com.medicine.remedy.response

import com.medicine.remedy.data_model.*
import com.google.gson.annotations.SerializedName
import com.medicine.remedy.model.BrandModel
import java.util.ArrayList

/**
 * Date 12/22/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class DefaultResponse
{
    @SerializedName("status") var responseCode : Int = 0
    @SerializedName("current_page") var currentPage : Int = 0
    @SerializedName("total_page") var totalPage : Int = 0
    @SerializedName("message") lateinit var message : String
    @SerializedName("order_id") lateinit var orderId : String
    @SerializedName("token_info") lateinit var token : TokenInfoModel
    @SerializedName("user_info") lateinit var userModel : UserModel
    @SerializedName("sliders") lateinit var sliderImages : List<SliderImageModel>
    @SerializedName("categories") lateinit var category : List<CategoryModel>
    @SerializedName("sub_cats") lateinit var subCategory : List<CategoryModel>
    @SerializedName("products") lateinit var products : List<ProductModel>
    @SerializedName("favourite_products") lateinit var favProducts : List<ProductModel>
    @SerializedName("popular_restaurant") lateinit var popularRestaurant : List<RestaurantModel>
    @SerializedName("recommended_product") lateinit var recommendedProduct : List<ProductModel>
    @SerializedName("product_info") lateinit var productInfo : ProductDescriptionModel
    @SerializedName("address_list") lateinit var addressList : List<AddressModel>
    @SerializedName("address") var addressData : AddressModel ?= null
    @SerializedName("upazilas") lateinit var subDistrictList : List<SubDistrictModel>
    @SerializedName("product_rates") lateinit var updateProductRates : List<OrderDetailsModel>
    @SerializedName("order_details") lateinit var orderDetails : List<OrderDetailsModel>
    @SerializedName("coupons") lateinit var couponList : List<CouponModel>
    @SerializedName("notifications") lateinit var notificationList : List<NotificationModel>
    @SerializedName("address_info")  var addressInfo : AddressModel? = null
    @SerializedName("coupon_information") lateinit var couponModel : CouponModel
    @SerializedName("coupon") lateinit var couponInfo : CouponModel
    @SerializedName("delivery_address") lateinit var addressModel : AddressModel
    @SerializedName("order_info") lateinit var orderHistory : OrderHistoryModel
    @SerializedName("related_products") var relatedProduct: List<ProductModel> = ArrayList()
    @SerializedName("brands") var brandList: List<BrandModel> = ArrayList()
}