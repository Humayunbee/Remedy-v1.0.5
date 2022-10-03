package com.medicine.remedy.config

import android.content.Context
import com.medicine.remedy.utils.AppConstants.Companion.baseUrl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.Interceptor.Chain
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Date 12/26/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class RetrofitClient
{
    companion object{
        private lateinit var retrofit : Retrofit

        fun getRetrofitClient() : Retrofit
        {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val gson : Gson = GsonBuilder()
                .setLenient()
                .create()

            val context: Context = BaseApplication.mContext
            val spManager = SharedPreferenceManager(context)


            val interceptor : Interceptor = object :  Interceptor{
                override fun intercept(chain: Chain): Response {
                    val original : Request = chain.request()

                    val request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/x-www-form-urlencoded")
                            .addHeader("Authorization", "Bearer " + spManager.getUserAccessToken())
                            .method(original.method, original.body)
                            .build()

                    return chain.proceed(request)
                }
            }

            val httpClient : OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                //.cache(cache)
                //.addInterceptor(httpLoggingInterceptor)
                //.addNetworkInterceptor(provideCacheInterceptor())
                .addInterceptor(interceptor)
                .build()

            retrofit  = Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()

            return retrofit
        }

        private fun provideCacheInterceptor(): Interceptor {

            return object : Interceptor{
                override fun intercept(chain: Chain): Response {
                    var request = chain.request()
                    val originalResponse = chain.proceed(request)
                    val cacheControl = originalResponse.header("Cache-Control")
                    return if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
                                    "no-cache"
                            ) ||
                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")
                    ) {

                        val cc : CacheControl = CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build()

                        request = request.newBuilder()
                            .cacheControl(cc)
                            .build()
                        chain.proceed(request)
                    } else {
                        originalResponse
                    }
                }
            }
        }
    }
}