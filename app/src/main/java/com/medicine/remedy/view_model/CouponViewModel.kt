package com.medicine.remedy.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.medicine.remedy.config.BaseAndroidViewModel
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.model.UserInfoRepository
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.utils.DataValidation
import com.medicine.remedy.utils.HttpErrorCode
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class CouponViewModel(application: Application) : BaseAndroidViewModel(application)
{

    private val repository = UserInfoRepository()

    init {
        mContext = application
        validation = DataValidation()
        disposables = CompositeDisposable()
        spManager = SharedPreferenceManager(application)
    }

    /**
     * ...get coupon list from remote db
     * ...add disposables
     * ...get error code if any error happen onCompleteOtpVerification server side
     * ...set appropriate error code
     */
    fun getCouponList() : MutableLiveData<DefaultResponse>
    {
        val mlResponse : MutableLiveData<DefaultResponse> = MutableLiveData()

        repository.getCouponList()
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
}