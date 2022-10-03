package com.medicine.remedy.config

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.medicine.remedy.R
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


/**
 * Date 12/23/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class BaseApplication : Application()
{
    override fun onCreate() {
        super.onCreate()
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        mContext = applicationContext
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext : Context
    }
}