package com.medicine.remedy.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import butterknife.ButterKnife
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.utils.ExtraUtils.Companion.startActivityIn

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/1/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class SplashScreen : BaseActivity()
{
    private val SPLASH_SCREEN_TIME = 2500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash_screen)
        ButterKnife.bind(this)
        startNewActivity()
        initialize()
    }

    override fun initialize() {
        spManager = SharedPreferenceManager(this)
    }

    override fun setToolbarTitle(title: String) {}

    /**
     * Start Log in activity
     * Splash Screen Stay 2s
     * Thread start
     */
    private fun startNewActivity() {
        Handler().postDelayed({
            if(spManager.getIsUserLoggedIn())
                startActivityIn(this, Intent(this, DashboardActivity::class.java))
            else startActivityIn(this, Intent(this, UserAuthenticationActivity::class.java))
            finish()
        }, SPLASH_SCREEN_TIME)
    }
}