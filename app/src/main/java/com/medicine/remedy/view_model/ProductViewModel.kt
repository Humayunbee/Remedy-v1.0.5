package com.medicine.remedy.view_model

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.medicine.remedy.config.BaseAndroidViewModel
import com.medicine.remedy.data_model.ProductDescriptionModel
import com.medicine.remedy.data_model.ProductModel
import com.medicine.remedy.database.entities.CartItemEntities
import com.medicine.remedy.model.OrderRepository
import com.medicine.remedy.model.ProductRepository
import com.medicine.remedy.model.UserInfoRepository
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.utils.HttpErrorCode
import com.medicine.remedy.view.activity.DashboardActivity
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/25/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ProductViewModel(application: Application) : BaseAndroidViewModel(application)
{

    private val repository : ProductRepository = ProductRepository()
    private val cartRepo : OrderRepository = OrderRepository()
    val userRepo  = UserInfoRepository()
    val mlProductId: MutableLiveData<String> = MutableLiveData()
    val mlQuantity: MutableLiveData<String> = MutableLiveData()
    val mlIsProductAddToCart: MutableLiveData<Boolean> = MutableLiveData()
    var mlIsLoading = MutableLiveData(false)
    var searchKey = MutableLiveData<String>()

    init {
        disposables = CompositeDisposable()
    }


    val searchSuggestionKey = MutableLiveData<String>()
    val searchSuggestions: LiveData<List<ProductModel>> = searchSuggestionKey.switchMap { key ->
        liveData {
            if (key.isNullOrEmpty()){
                emit(emptyList())
            }else{
                try {
                    val resp = repository.searchSuggestion(key)
                    emit(resp.products)
                } catch(e: Exception) {
                    emit(listOf())
                }
            }

        }

    }


    /**
     * ...get product description from server
     * ...handle server response
     * ...initialize disposables
     * @return server response
     */
    fun getProductDescription() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.getProductDescription(mlProductId.value.toString())
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
     * ...add a new product to the cart list
     * ...add to local db
     */
    @SuppressLint("CheckResult")
    fun addProductToCartList(productInfo: ProductDescriptionModel)
    {
        val cartItemEntities = CartItemEntities()

        cartItemEntities.productId = productInfo.productId
        cartItemEntities.productName = productInfo.productName
        cartItemEntities.quantity = mlQuantity.value!!.toInt()
        cartItemEntities.unitPrice = productInfo.productPrice
        cartItemEntities.totalPrice = productInfo.productPrice * mlQuantity.value!!.toInt()
        cartItemEntities.discount = productInfo.discount
        cartItemEntities.extraValue = productInfo.subCategory
        cartItemEntities.maxQty = productInfo.maxQty
        cartItemEntities.packSize = if(productInfo.productUnit != null && productInfo.packSize != null) "${productInfo.packSize} ${productInfo.productUnit}" else ""
        cartItemEntities.image = if(productInfo.mainImage != null) productInfo.mainImage.toString() else ""

        Completable.fromRunnable { cartRepo.addProductToCart(cartItemEntities)}
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mlIsProductAddToCart.postValue(true)
                    Log.d("response","Success entry")
                },{
                    Log.d("response","failed entry")
                })

        countTotalCartItem()
    }

    @SuppressLint("CheckResult")
    fun countTotalCartItem()
    {
        Completable.fromRunnable {
            DashboardViewModel.mlTotalCartItem.postValue(repository.getTotalCartItem().toInt())
        }.subscribeOn(Schedulers.io())
            .subscribe({},{
                Log.d("room_db_error","$it")
            })
    }

    /**
     * ...get product list
     */
    fun productList(pageNumber : Int) : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()
        mlIsLoading.value = true
        repository.productList(pageNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onSuccess(t: DefaultResponse) {
                    mlResponse.value = t
                    mlIsLoading.value = false
                }

                override fun onError(e: Throwable) {
                    Log.d("error_response", e.toString())
                    val response = DefaultResponse()
                    response.responseCode = HttpErrorCode.GET_HTTP_ERROR_CODE(e)
                    mlResponse.value = response
                    mlIsLoading.value = false
                }
            })

        return mlResponse
    }

    /**
     * ...get category wise product list
     */
    fun categoryWiseProductList(categoryId : String,pageNumber : Int) : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        mlIsLoading.value = true
        repository.categoryWiseProductList(categoryId,pageNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DefaultResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onSuccess(t: DefaultResponse) {
                    mlResponse.value = t
                    mlIsLoading.value = false
                }

                override fun onError(e: Throwable) {
                    Log.d("error_response", e.toString())
                    val response = DefaultResponse()
                    response.responseCode = HttpErrorCode.GET_HTTP_ERROR_CODE(e)
                    mlResponse.value = response
                    mlIsLoading.value = false
                }
            })

        return mlResponse
    }

    /**
     * ...get product list
     */
    fun searchProductList() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()
        repository.searchProduct(searchKey.value.toString())
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

    fun searchByBrand(): MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()
        repository.searchProductByBrand(getOnlySelectedBrandId())
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

    private fun getOnlySelectedBrandId() : String
    {
        var jsonArray = ""
        try{
            val brandId = JSONArray()
            for(item in DashboardActivity.brandList)
            {
                if(item.isSelected)
                    brandId.put(item.brandId)
            }

            jsonArray = brandId.toString()
        }catch (ex : Exception)
        {
        }

        return  jsonArray
    }
}