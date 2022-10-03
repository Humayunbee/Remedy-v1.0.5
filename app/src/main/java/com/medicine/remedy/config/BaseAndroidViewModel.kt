package com.medicine.remedy.config

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.medicine.remedy.interfaces.InitComponent
import com.medicine.remedy.utils.DataValidation
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/17/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application),InitComponent
{
    protected lateinit var validation : DataValidation
    protected lateinit var disposables : CompositeDisposable
    protected lateinit var spManager : SharedPreferenceManager
    protected lateinit var mContext : Context

    /**
     * ...initialize all component
     * ...create object
     * ...call some necessary method
     */
    override fun initialize(){}

    override fun setToolbarTitle(title: String) { }
}