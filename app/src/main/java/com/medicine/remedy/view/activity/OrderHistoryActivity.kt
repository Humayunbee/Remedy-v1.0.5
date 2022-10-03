package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ViewUtils
import com.medicine.remedy.view.fragment.OrderHistoryFragment
import com.medicine.remedy.view_model.DashboardViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
@SuppressLint("NonConstantResourceId")
class OrderHistoryActivity : BaseActivity()
{
    @BindView(R.id.tv_title) lateinit var tvTitle : TextView
    @BindView(R.id.toolbar_white) lateinit var toolbarWhite : RelativeLayout
    @BindView(R.id.noti_badge) lateinit var tvNotiBadge : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_fragments)
        ButterKnife.bind(this)
        initialize()
    }

    override fun initialize() {
        ViewUtils.onClickToolbarMenu(this, toolbarWhite)
        loadFragment(OrderHistoryFragment())
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

    @OnClick(value = [R.id.tv_title])
    fun onClickListener()
    {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
        {
            finish()
            ExtraUtils.startActivityOut(this)
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