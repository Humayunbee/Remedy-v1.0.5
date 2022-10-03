package com.medicine.remedy.view_model

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.medicine.remedy.config.BaseAndroidViewModel
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.data_model.ProductModel
import com.medicine.remedy.model.DashboardRepository
import com.medicine.remedy.model.ProductRepository
import com.medicine.remedy.model.UserInfoRepository
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.HttpErrorCode
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

/**
 * Date 12/22/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class DashboardViewModel(application: Application) : BaseAndroidViewModel(application)
{
    private val repository : DashboardRepository = DashboardRepository()
    private val productRepository : ProductRepository = ProductRepository()
    private val repositoryUser = UserInfoRepository()

    companion object{
        var productSearchKeyword = MutableLiveData("")
        var mlTotalCartItem = MutableLiveData(0)
        var mlNotiCount = MutableLiveData(0)
    }

    var searchKey = MutableLiveData<String>()

    init {
        mContext = application
        spManager = SharedPreferenceManager(mContext)
        disposables = CompositeDisposable()
    }

    val searchSuggestionKey = MutableLiveData<String>()
    val searchSuggestions: LiveData<List<ProductModel>> = searchSuggestionKey.switchMap { key ->
        liveData {
            if (key.isNullOrEmpty()){
                emit(emptyList())
            }else{
                try {
                    val resp = productRepository.searchSuggestion(key)
                    emit(resp.products)
                } catch(e: Exception) {
                    emit(listOf())
                }
            }

        }

    }


    fun countNotification(){
        viewModelScope.launch {
            try {
                val resp = repositoryUser.getNotificationList(1)
                mlNotiCount.postValue(resp.notificationList.filter { it.readStatus == null }.size)
            }catch (e: java.lang.Exception){ }
        }
    }


    /**
     * ...getDashboardData from remote db
     * ...send to request to remote db
     * ...handle server response
     * ...initialize disposables
     * @return MutableLiveData server response
     */
    fun getDashboardData() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.getDashboardData()
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

    @SuppressLint("CheckResult")
    fun countTotalCartItem()
    {
        Completable.fromRunnable {
            mlTotalCartItem.postValue(repository.getTotalCartItem().toInt())
        }.subscribeOn(Schedulers.io())
            .subscribe({},{
                Log.d("room_db_error","$it")
            })
    }

    fun getBrandsList() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.getBrandsList()
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

    //store firebase token
    fun storeFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("firebase_token",token)

            repository.insertDeviceToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<DefaultResponse> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onSuccess(t: DefaultResponse) {
                        if (t.responseCode == RESPONSE_SUCCESS_CODE) //if token successfully registered
                            spManager.deviceTokenRegistration(true) //changed status to true
                        else spManager.deviceTokenRegistration(false) //changed status to false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("error_response", e.toString())
                    }
                })

        })
    }
}

/*
if (response.responseCode == RESPONSE_SUCCESS_CODE) //if token successfully registered
                    spManager.deviceTokenRegistration(true) //changed status to true
                else spManager.deviceTokenRegistration(false) //changed status to false
 */