package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.view.fragment.CouponListFragment
import com.medicine.remedy.view_model.DashboardViewModel
import com.medicine.remedy.view_model.DashboardViewModel.Companion.mlTotalCartItem

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
@SuppressLint("NonConstantResourceId")
class CouponsActivity : BaseActivity()
{
    @BindView(R.id.tv_title) lateinit var tvTitle : TextView
    @BindView(R.id.iv_menu) lateinit var ivMenu : ImageView
    @BindView(R.id.rl_cart) lateinit var rlCart : RelativeLayout
    @BindView(R.id.cart_badge) lateinit var tvCartBadge : TextView
    @BindView(R.id.noti_badge) lateinit var tvNotiBadge : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_fragments)
        ButterKnife.bind(this)
        initialize()
    }

    override fun onStart() {
        super.onStart()
        ExtraUtils.isUserLoggedIn(this)
    }

    override fun initialize() {
        ivMenu.visibility = View.GONE
        rlCart.visibility = View.VISIBLE
        mlTotalCartItem.observe(this) {
            if (it > 0) {
                rlCart.visibility = View.VISIBLE
                tvCartBadge.visibility = View.VISIBLE
                tvCartBadge.text = "$it"
            } else tvCartBadge.visibility = View.GONE
        }
        loadFragment(CouponListFragment())
        DashboardViewModel.mlNotiCount.observe(this) {
            if (it > 0) {
                tvNotiBadge.visibility = View.VISIBLE
                tvNotiBadge.text = "$it"
            } else tvNotiBadge.visibility = View.GONE
        }
    }

    override fun setToolbarTitle(title: String) {
        setToolbarTitle(title, tvTitle)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
        {
            super.onBackPressed()
            ExtraUtils.startActivityOut(this)
        }
    }

    @OnClick(value = [R.id.tv_title,R.id.rl_cart])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.tv_title->{
                if (supportFragmentManager.backStackEntryCount > 0)
                    supportFragmentManager.popBackStack()
                else
                    ExtraUtils.startActivityOut(this)
            }

            R.id.iv_menu-> ExtraUtils.startActivityIn(this, Intent(this, SettingActivity::class.java))

            R.id.iv_notification-> ExtraUtils.startActivityIn(this, Intent(this, NotificationActivity::class.java))

            R.id.rl_cart-> ExtraUtils.startActivityIn(this, Intent(this, OrderActivity::class.java))
        }
    }
}