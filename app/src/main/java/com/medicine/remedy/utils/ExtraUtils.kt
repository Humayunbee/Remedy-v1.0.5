package com.medicine.remedy.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.medicine.remedy.R
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.model.BrandModel
import com.medicine.remedy.utils.AppConstants.Companion.WARNING_DIALOG
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.view.activity.DashboardActivity
import com.medicine.remedy.view.activity.UserAuthenticationActivity
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.ArrayList
import kotlin.math.roundToInt

/**
 * Date 12/26/2020.
 * Created by Md Atik Faysal(Android Developer)
 *
 */

fun ArrayList<BrandModel>.clearFilters()
{
    val items = ArrayList<BrandModel>()
    for(it in this)
    {
        it.isSelected = false
        items.add(it)
    }

    DashboardActivity.brandList.clear()
    DashboardActivity.brandList.addAll(items)
}

fun ArrayList<BrandModel>.isAnyItemSelected() : Boolean
{
    var count = 0
    for(it in this)
    {
       if(it.isSelected)
           count++
    }

    return count > 0
}

class ExtraUtils
{
    companion object{

        /**
         * ...Convert html text to plain text
         * @param text, html text
         * @return , plain text
         */
        fun htmlToPlainText(text: String): Spanned {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            else
                Html.fromHtml(text)
        }

        //close top all activity and go to specific activity
        fun closeAllActivity(context: Activity, clazz: Class<*>?) {
            val intent = Intent(context, clazz)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            //context.startActivity(intent);
            startActivityIn(context, intent)
            context.finish()
        }

        /**
         * ...Check internet connection status
         * ...Is device connected with internet
         * ...Check android os version
         * @return if device connected with internet return true otherwise false
         */
        @JvmStatic
        fun isOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)//if os version is getter than marshmallow
            {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                            return true
                        }
                    }
                }
            }else
            {
                val networkInfo = connectivityManager.activeNetworkInfo
                if (networkInfo != null) return networkInfo.state == NetworkInfo.State.CONNECTED

                return false
            }

            return false
        }

        @JvmStatic
        fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

        /**
         * ...start activity with intent
         * ...start animation in when new activity started
         * @param activity activity context
         * @param intent intent with extra value
         */
        @JvmStatic
        fun startActivityIn(activity: Activity, intent: Intent)
        {
            activity.startActivity(intent)
            activity.overridePendingTransition(
                R.anim.animation_slide_in_right,
                R.anim.animation_slide_out_left
            )
        }

        /**
         * ...start activity with intent
         * ...start animation out when new activity started
         * @param activity activity context
         */
        @JvmStatic
        fun startActivityOut(activity: Activity)
        {
            activity.finish()
            activity.overridePendingTransition(
                R.anim.animation_slide_in_left,
                R.anim.animation_slide_out_right
            )
        }

        /**
         * ...increase number of quantity
         * ...add 1 with old value
         * @param strOldQty old quantity
         * @return new qty after adding 01
         */
        fun increaseQuantity(strOldQty: String, maxQty : Int) : Int
        {
            val oldQty : Int = strOldQty.toInt()
            if(oldQty == maxQty)
                return oldQty

            return oldQty + 1
        }

        /**
         * ...decrease number of quantity
         * ...subtract 1 with old value
         * @param strOldQty old quantity
         * @return new qty after adding 01
         */
        fun decreaseQuantity(strOldQty: String) : Int
        {
            val oldQty : Int = strOldQty.toInt()

            if(oldQty == 1) return oldQty

            return oldQty - 1
        }

        /**
         * ...load fragment in activity
         * ...set animation on transaction
         * ...transaction commit
         */
        fun showFragment(activity: FragmentActivity, fragment: Fragment) {
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.animation_slide_in_right,
                R.anim.animation_slide_out_left,
                R.anim.animation_slide_in_left,
                R.anim.animation_slide_out_right
            )
            fragmentTransaction.replace(R.id.fl_container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        /**
         * ...check is current user logged in
         */
        fun isUserLoggedIn(activity: Activity)
        {
            val spManager = SharedPreferenceManager(activity)
            if(!spManager.getIsUserLoggedIn())
            {
                val intent = Intent(activity, UserAuthenticationActivity::class.java)
                startActivityIn(activity, intent)
                activity.finish()
            }
        }

        fun getTintedDrawable(inputDrawable: Drawable, @ColorInt color: Int): Drawable {
            val wrapDrawable = DrawableCompat.wrap(inputDrawable)
            DrawableCompat.setTint(wrapDrawable, color)
            DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN)
            return wrapDrawable
        }

        /**
         * ...add comma for amount
         * @param number decimal amount
         * @return amount with comma
         */
        @JvmStatic
        fun commaOnAmount(number: Double): String {
            val formatter =
                DecimalFormat(amountFormat(doubleValueFormat(number)))
            return formatter.format(number)
        }

        @JvmStatic
        fun commaOnAmount(number: Int): String {
            val formatter = DecimalFormat(amountFormat(number.toString()))
            return formatter.format(number.toLong())
        }

        /**
         * ...get amount format
         * @param amount actual amount
         * @return amount format
         */
        fun amountFormat(amount: String): String {
            return when (amount.length) {
                5 -> "##,###.##"
                6 -> "#,##,###.##"
                7 -> "##,##,###.##"
                8 -> "#,##,##,###.##"
                9 -> "##,##,##,###.##"
                10 -> "#,##,##,##,###.##"
                11 -> "##,##,##,##,###.##"
                4 -> "#,###.##"
                else -> "#,###.##"
            }
        }

        /**
         * get double format value with two decimal places
         * when decimal number is 0 then return int format
         * @param d double value
         * @return double format value
         */
        @SuppressLint("DefaultLocale")
        fun doubleValueFormat(d: Double): String {
            if (d == d)
                return String.format("%d", d.toLong())
            else return String.format("%.2f", d)
        }

        /**
         * get double format value with two decimal places
         * when decimal number is 0 then return int format
         * @param d double value
         * @return double format value
         */
        @JvmStatic
        @SuppressLint("DefaultLocale")
        fun doubleToInt(d: Double): Int {
            return d.roundToInt()
        }

        /**
         * ...convert string to double
         */
        @JvmStatic
        fun stringAmountToDouble(amount : String) : Double
        {
            var result = 0.00
            try {
                result = amount.toDouble()
            }catch (ex : Exception) {
                Log.d("exception",ex.toString())
            }

            return roundOffDecimal(result)
        }

        /**
         * Phone call intent
         * make a call to contact number
         */
        fun makePhoneCall(mobile: String, activity: Activity) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$mobile")

            // permission is granted so make a phone call
            // check for permanent denial of permission
            // navigate user to app settings
            val permissionListener: PermissionListener = object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // permission is granted so make a phone call
                    if (ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    activity.startActivity(callIntent)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        // navigate user to app settings
                        PermissionManager.showSettingsDialog(
                            activity,
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        )
                        showToast(activity, "You need to allow permissions", WARNING_DIALOG)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }
            if (PermissionManager.isCallPhonePermissionGranted(activity)) {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                activity.startActivity(callIntent)
            } else {
                PermissionManager.requestForPhoneCallPermission(activity, permissionListener)
            }
        }
    }
}
