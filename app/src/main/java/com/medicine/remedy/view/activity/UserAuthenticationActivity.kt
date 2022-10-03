package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.utils.ExtraUtils.Companion.startActivityOut
import com.medicine.remedy.view.fragment.UserLoginFragment
import com.medicine.remedy.view_model.DashboardViewModel

/**
 * Date 1/11/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
@SuppressLint("NonConstantResourceId")
class UserAuthenticationActivity : BaseActivity()
{
    @BindView(R.id.toolbar_white) lateinit var toolbarWhite : RelativeLayout
    @BindView(R.id.noti_badge) lateinit var tvNotiBadge : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_fragments)
        ButterKnife.bind(this)
        initialize()
    }

    override fun initialize() {
        toolbarWhite.visibility = View.GONE
        loadFragment(UserLoginFragment())
        DashboardViewModel.mlNotiCount.observe(this) {
            if (it > 0) {
                tvNotiBadge.visibility = View.VISIBLE
                tvNotiBadge.text = "$it"
            } else tvNotiBadge.visibility = View.GONE
        }
    }

    override fun setToolbarTitle(title: String) {}

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
        {
            super.onBackPressed()
            startActivityOut(this)
        }
    }
}