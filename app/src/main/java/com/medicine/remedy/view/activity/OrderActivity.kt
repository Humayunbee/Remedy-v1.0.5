package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.utils.ExtraUtils.Companion.startActivityOut
import com.medicine.remedy.utils.ViewUtils
import com.medicine.remedy.view.fragment.OrderConfirmationFragment.Companion.isOrderPlaced
import com.medicine.remedy.view.fragment.ProductCartFragment
import com.medicine.remedy.view_model.DashboardViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
@SuppressLint("NonConstantResourceId")
class OrderActivity : BaseActivity()
{
    @BindView(R.id.tv_title) lateinit var tvTitle : TextView
    @BindView(R.id.toolbar_white) lateinit var toolbarWhite : RelativeLayout
    @BindView(R.id.noti_badge) lateinit var tvNotiBadge : TextView


    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var orderActivity : Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_fragments)
        ButterKnife.bind(this)
        initialize()
    }

    override fun initialize() {
        orderActivity = this
        ViewUtils.onClickToolbarMenu(this, toolbarWhite)
        loadFragment(ProductCartFragment())
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

        if(isOrderPlaced)
        {
            isOrderPlaced = false
            super.onBackPressed()
            startActivityOut(this)
        }

        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
        {
            super.onBackPressed()
            startActivityOut(this)
        }
    }

    @OnClick(value = [R.id.tv_title])
    fun onClickListener()
    {
        if(isOrderPlaced)
        {
            isOrderPlaced = false
            super.onBackPressed()
            startActivityOut(this)
        }

        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
        {
            finish()
            startActivityOut(this)
        }
    }

    /**
     * ...hide red toolbar
     * ...shoe white toolbar
     */
    fun hideToolbar()
    {
        toolbarWhite.visibility = View.GONE
    }
}