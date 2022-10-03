package com.medicine.remedy.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.medicine.remedy.config.BaseApplication.Companion.mContext
import com.medicine.remedy.config.DatabaseClient
import com.medicine.remedy.config.RetrofitClient
import com.medicine.remedy.config.UserApiConfig
import com.medicine.remedy.data_model.OrderResponse
import com.medicine.remedy.database.entities.CartItemEntities
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.HttpErrorCode
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Date 2/4/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class OrderRepository
{
    /**
     * ...insert product to the cart table
     * @param cartItems product information
     */
    fun addProductToCart(cartItems : CartItemEntities) : Long
    {
        return DatabaseClient.getInstance(mContext).appDatabase.cartItemDao()
            .insertCartItems(cartItems)
    }

    /**
     * ...remove cart items
     */
    fun removeItemFromCart(id: String)
    {
        return DatabaseClient.getInstance(mContext).appDatabase.cartItemDao()
            .removeCartItem(id)
    }

    suspend fun removeItemFromCart2(id: String)
    {
        return DatabaseClient.getInstance(mContext).appDatabase.cartItemDao()
            .removeCartItem2(id)
    }

    /**
     *   get single cart item
     */
    suspend fun getItemFromCart(id: String): CartItemEntities? {
        return DatabaseClient.getInstance(mContext).appDatabase.cartItemDao()
            .getCartItem(id)
    }

    /**
     * ...get cart items from local dashboardData
     * @return cart items list
     */
    fun getCartItems() : Single<List<CartItemEntities>>
    {
        return DatabaseClient
            .getInstance(mContext)
            .appDatabase
            .cartItemDao()
            .getCartItems()
    }

    /**
     * ...update product qty, total price to the cart table
     */
    fun updateCartQty(id : String,qty: Int, totalPrice: Double)
    {
        DatabaseClient.getInstance(mContext).appDatabase.cartItemDao()
            .updateCartQty(id,qty, totalPrice)
    }

    suspend fun updateCartQty2(id : String,qty: Int, totalPrice: Double)
    {
        DatabaseClient.getInstance(mContext).appDatabase.cartItemDao()
            .updateCartQty2(id,qty, totalPrice)
    }

    /**
     * ...apply coupon code
     */
    fun applyCoupon(couponCode: String) : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        config.applyCoupon( couponCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse> {
                override fun onSubscribe(d: Disposable) {}

                override fun onSuccess(t: DefaultResponse) {
                    mlResponse.value = t
                }

                override fun onError(e: Throwable) {
                    Log.d("error_response", e.toString())
                    val response = DefaultResponse()
                    response.responseCode = HttpErrorCode.GET_HTTP_ERROR_CODE(e)
                    mlResponse.value = response
                }
            })

        return mlResponse
    }

    /**
     * ...apply coupon code
     */
    fun couponApply( couponCode: String) : Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.applyCoupon( couponCode)
    }

    /**
     * ...get update price list
     */
    fun priceCheckOfProductList( productJson: String) : Single<DefaultResponse>
    {
        Log.d("product_json",productJson)
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.priceCheckOfProductList( productJson)
    }

    /**
     * ... place new order
     */
    fun placeNewOrder( addressId: String, couponId: String?, productJson: String, totalPrice: String, deliveryType: String,discount : String) : Single<DefaultResponse>
    {
        Log.d("print_address_id",addressId)
        Log.d("print_couponId","${couponId.toString()} id")
        Log.d("print_productJson",productJson)
        Log.d("print_totalPrice",totalPrice)
        Log.d("print_deliveryType",deliveryType)
        Log.d("print_discount",discount)
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.placeOrder(addressId,couponId, productJson,totalPrice, deliveryType,discount)
    }

    /**
     * ....remove offline cartItems
     */
    fun removeOfflineCartItem()
    {
        DatabaseClient.getInstance(mContext).appDatabase
            .cartItemDao()
            .removeOfflineCartItem()
    }

    /**
     * ...get order history list
     */
    fun getOrderHistory(page: Int) : Single<OrderResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.getOrderHistory(page, AppConstants.OFFSET_SIZE_PER_PAGE + 1)
    }

    /**
     * ...get order track info
     */
    fun getOrderTrack( orderId: String) : Single<DefaultResponse>
    {
        val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)

        return config.getOrderTrack(orderId)
    }

    /**
     * ...get count of total cart
     * @param cartItems product information
     */
    fun getTotalCartItem() : Long
    {
        return DatabaseClient.getInstance(mContext).appDatabase.cartItemDao()
            .countTotalCartItem()
    }
}