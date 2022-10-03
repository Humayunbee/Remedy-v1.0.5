package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.databinding.LayoutFragmentsBinding
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ExtraUtils.Companion.startActivityOut
import com.medicine.remedy.utils.ViewUtils.Companion.onClickToolbarMenu
import com.medicine.remedy.view.fragment.SettingFragment
import com.medicine.remedy.view_model.DashboardViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/6/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
@SuppressLint("NonConstantResourceId")
class SettingActivity : BaseActivity()
{
    lateinit var binding : LayoutFragmentsBinding
    @BindView(R.id.tv_title_red) lateinit var tvTitle : TextView
    @BindView(R.id.toolbar_red) lateinit var toolbarRed : RelativeLayout
    @BindView(R.id.toolbar_white) lateinit var toolbarWhite : RelativeLayout
    @BindView(R.id.noti_badge) lateinit var tvNotiBadge : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_fragments)
        ButterKnife.bind(this)
        initialize()
    }

    override fun initialize() {
        onClickToolbarMenu(this,toolbarWhite)
        DashboardViewModel.mlNotiCount.observe(this) {
            if (it > 0) {
                binding.toolbarRed.notiBadge.visibility = View.VISIBLE
                binding.toolbarRed.notiBadge.text = "$it"
            } else  binding.toolbarRed.notiBadge.visibility = View.GONE
        }
        loadFragment(SettingFragment())

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
            startActivityOut(this)
        }
    }

    /**
     * ...hide red toolbar
     * ...shoe white toolbar
     */
    fun hideToolbar()
    {
        toolbarRed.visibility = View.VISIBLE
        toolbarWhite.visibility = View.GONE
    }

    @OnClick(value = [R.id.tv_title_red,R.id.iv_notification])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.tv_title_red->{
                if (supportFragmentManager.backStackEntryCount > 0)
                    supportFragmentManager.popBackStack()
                else
                {
                    finish()
                    startActivityOut(this)
                }
            }

            R.id.iv_notification-> ExtraUtils.startActivityIn(this, Intent(this, NotificationActivity::class.java))
        }
    }
}