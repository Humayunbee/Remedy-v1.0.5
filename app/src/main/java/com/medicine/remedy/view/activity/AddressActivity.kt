package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.databinding.LayoutFragmentsBinding
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ExtraUtils.Companion.isUserLoggedIn
import com.medicine.remedy.utils.ViewUtils
import com.medicine.remedy.view.fragment.AddressListFragment
import com.medicine.remedy.view_model.DashboardViewModel
import com.medicine.remedy.view_model.UserInformationViewModel.Companion.mlSubDistrictId

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 4/2/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
@SuppressLint("NonConstantResourceId")
class AddressActivity : BaseActivity()
{
    @BindView(R.id.tv_title) lateinit var tvTitle : TextView
    lateinit var binding : LayoutFragmentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_fragments)
        ButterKnife.bind(this)
        initialize()
    }

    override fun onStart() {
        super.onStart()
        ViewUtils.onClickToolbarMenu(this, binding.toolbarWhite.root)
        isUserLoggedIn(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mlSubDistrictId.value = ""
    }

    override fun initialize() {
        loadFragment(AddressListFragment())
        DashboardViewModel.mlNotiCount.observe(this) {
            if (it > 0) {
                binding.toolbarWhite.notiBadge.visibility = View.VISIBLE
                binding.toolbarWhite.notiBadge.text = "$it"
            } else  binding.toolbarWhite.notiBadge.visibility = View.GONE
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

    @OnClick(value = [R.id.tv_title, R.id.iv_menu, R.id.iv_notification,R.id.rl_cart])
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