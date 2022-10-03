package com.medicine.remedy.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseAndroidViewModel
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.data_model.AddressModel
import com.medicine.remedy.data_model.CouponModel
import com.medicine.remedy.database.entities.CartItemEntities
import com.medicine.remedy.model.OrderRepository
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.DataValidation
import com.medicine.remedy.utils.DataValidation.Companion.inputTextValidator
import com.medicine.remedy.utils.HttpErrorCode
import com.medicine.remedy.view.fragment.OrderDetailsFragment.Companion.productList
import com.medicine.remedy.view.fragment.ProductCartFragment.Companion.cartItems
import com.google.gson.GsonBuilder
import com.medicine.remedy.config.RetrofitClient
import com.medicine.remedy.config.UserApiConfig
import com.medicine.remedy.data_model.OrderDetailsModel
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
open class OrderViewModel(application: Application) : BaseAndroidViewModel(application)
{
    private val repository = OrderRepository()

    var mlShippingAddress = MutableLiveData<String>()
    var mlShippingAddressId = MutableLiveData<String>()
    var mlDisplayDeliveryCharge = MutableLiveData<String>()
    var mlCouponCode = MutableLiveData<String>()
    var mlCouponCodeId = MutableLiveData<String>()
    var mlInvoiceNo = MutableLiveData<String>()
    var mlGrandTotalAmt = MutableLiveData<String>()

    companion object{
        var selectedAddressModel = MutableLiveData<AddressModel>()
        @JvmField var mlSubTotal = MutableLiveData<Double>()
        @JvmField var mlTotalDiscount = MutableLiveData<Double>()
        @JvmField var mlCouponDiscount = MutableLiveData<Double>()
        @JvmField var mlDisplayGrandTotal = MutableLiveData<String>()
        @JvmField var mlGrandTotal = MutableLiveData<Double>()
        @JvmField var mlDeliveryCharge = MutableLiveData<Int>()

        @JvmField var selectedCouponModel = MutableLiveData<CouponModel>()

        @JvmField var mlDisplaySubTotal = MutableLiveData<String>()
        @JvmField var mlDisplaySubTotalDiscount = MutableLiveData<String>()
    }

    init {
        mContext = application
        validation = DataValidation()
        disposables = CompositeDisposable()
        spManager = SharedPreferenceManager(application)

        mlSubTotal.value = 0.0
        mlTotalDiscount.value = 0.0
        mlCouponDiscount.value = 0.0
        mlGrandTotal.value = 0.0
    }

    /**
     * ...get cart items from local db
     * ...initialize disposables
     * @return data from local db
     */
    fun getCartItems() : MutableLiveData<List<CartItemEntities>>
    {
        val mlCartItems : MutableLiveData<List<CartItemEntities>> = MutableLiveData()

        repository.getCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<CartItemEntities>> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onSuccess(t: List<CartItemEntities>) {
                        for(item in t)
                        {
                            Log.d("itemQuantity", "${item.quantity} and ${item.maxQty}")
                            if(item.quantity >= item.maxQty && item.maxQty != 0)
                                item.quantity = item.maxQty
                            else item.quantity = item.quantity
                        }

                        mlCartItems.value = t
                    }

                    override fun onError(e: Throwable) {
                        val cartItems : List<CartItemEntities> = ArrayList()
                        mlCartItems.value = cartItems
                    }
                })

        return mlCartItems
    }

    /**
     * ...get default address information from shared preferences
     * ...set address and address id
     */
    fun getAddressInfo()
    {
        if(spManager.getDefaultAddress() != null && spManager.getDefaultAddressId() != null){
            mlShippingAddress.value = spManager.getDefaultAddress()
            mlShippingAddressId.value = spManager.getDefaultAddressId()
        }else{
            val config : UserApiConfig = RetrofitClient.getRetrofitClient().create(UserApiConfig::class.java)
            viewModelScope.launch {
                try  {
                    val resp = config.getAddressList2()
                    resp.addressList.firstOrNull { it.isDefault == 1 }?.let {
                        selectedAddressModel.value = it
                    }

                } catch(e: Exception) { }
            }
        }

        mlDeliveryCharge.value = 0
        mlDisplayDeliveryCharge.value = "0"
    }

    /**
     * ...apply coupons
     *
     */
    fun applyCoupon(): MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.couponApply(mlCouponCode.value.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

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
     * ...validation of required parameters
     * ...check cart items is empty or not
     * ...check grand total is getter than 0vb
     * ...check shipping address is available or not
     * ...if coupon apply then coupon id is empty or not
     */
    fun validationOfRequiredData() : Int
    {
        if(cartItems.isEmpty())
            return 1

        if(mlShippingAddress.value == null || !inputTextValidator(mlShippingAddress.value.toString()) || mlShippingAddressId.value == null || !inputTextValidator(mlShippingAddressId.value.toString()))
            return 2

        if(mlGrandTotal.value == 0.0 || mlGrandTotal.value!! < 0.0)
            return 3

        if(mlCouponCode.value != null && inputTextValidator(mlCouponCode.value.toString()))
        {
            if(mlCouponCodeId.value == null || !inputTextValidator(mlCouponCodeId.value.toString()))
                return 4
        }

        return RESPONSE_SUCCESS_CODE
    }

    /**
     * ...validation of required parameters
     * ...check cart items is empty or not
     * ...check grand total is getter than 0
     * ...check shipping address is available or not
     * ...if coupon apply then coupon id is empty or not
     */
    fun validationOfCheckoutRequiredData() : Int
    {
        if(productList.isEmpty())
            return 1

        if(mlShippingAddressId.value == null || !inputTextValidator(mlShippingAddressId.value.toString()))
            return 2

        if(mlGrandTotal.value == 0.0 || mlGrandTotal.value!! < 0.0)
            return 3

        if(mlCouponCode.value != null && inputTextValidator(mlCouponCode.value.toString()))
        {
            if(mlCouponCodeId.value == null || !inputTextValidator(mlCouponCodeId.value.toString()))
                return 4
        }

        return RESPONSE_SUCCESS_CODE
    }

    /**
     * ...get update price list of all product
     */
    fun getUpdateProductPrice() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        val productJson = getProductId(cartItems)

        repository.priceCheckOfProductList( productJson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onSuccess(t: DefaultResponse) {
                    val details = ArrayList<OrderDetailsModel>()
                    for(items in t.updateProductRates)
                    {
                        for(product in cartItems)
                        {
                            val model = OrderDetailsModel()
                            if(product.productId == items.productId)
                            {
                                model.productId = items.productId
                                model.discount = 0.0
                                model.productName = product.productName
                                model.rate = items.rate
                                model.qty = product.quantity
                                details.add(model)
                                break
                            }
                        }
                    }

                    val response = DefaultResponse()
                    response.updateProductRates = details
                    response.responseCode = t.responseCode
                    response.message = t.message
                    mlResponse.value = response
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
     * ...place new order
     * ...get json string from product list
     * ...get error code from server error response
     */
    fun placeOrder() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        val gSon = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val productJson = gSon.toJson(productList)

        repository.placeNewOrder(mlShippingAddressId.value.toString(), mlCouponCodeId.value.toString(),productJson,
            mlGrandTotalAmt.value.toString(),"1", mlCouponDiscount.value.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

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
     * ...remove all cartItems
     */
    fun doEmptyCart()
    {
        Completable.fromRunnable {
            repository.removeOfflineCartItem()
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    private fun getProductId(item: List<CartItemEntities>) : String
    {
        var jsonProductId = ""
        try{
            val productId = JSONArray()
            for(product in item)
            {
                productId.put(product.productId)
            }

            jsonProductId = productId.toString()
        }catch (ex : Exception)
        {
            ex.printStackTrace()
        }

        return jsonProductId
    }
}