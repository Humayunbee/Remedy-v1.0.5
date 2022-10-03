package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.view_model.DashboardViewModel

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 8/1/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
@SuppressLint("NonConstantResourceId")
class FragmentHolderActivity: BaseActivity()
{
    @BindView(R.id.tv_title) lateinit var tvTitle : TextView
    @BindView(R.id.cart_badge) lateinit var tvCartBadge : TextView
    @BindView(R.id.iv_menu) lateinit var ivMenu : ImageView
    @BindView(R.id.rl_cart) lateinit var rlCart : RelativeLayout
    @BindView(R.id.noti_badge) lateinit var tvNotiBadge : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_fragments)
        ButterKnife.bind(this)
        initialize()
    }

    override fun initialize() {
        ivMenu.visibility = GONE
        rlCart.visibility = VISIBLE

        DashboardViewModel.mlTotalCartItem.observe(this) {
            if (it > 0) {
                rlCart.visibility = VISIBLE
                tvCartBadge.text = "$it"
            } else tvCartBadge.visibility = GONE
        }
        DashboardViewModel.mlNotiCount.observe(this) {
            if (it > 0) {
                tvNotiBadge.visibility = View.VISIBLE
                tvNotiBadge.text = "$it"
            } else tvNotiBadge.visibility = View.GONE
        }

//        when(intent.getStringExtra(fragmentId)) {
//            productList,categoryWiseProductList -> {
//                val id = intent.getStringExtra(categoryId)
//                val toolbarTitle = intent.getStringExtra(toolbarTitle)
//                val fragment: Fragment = ProductListFragment()
//                val args = Bundle()
//                args.putString(categoryId,id)
//                args.putString(AppConstants.toolbarTitle,toolbarTitle)
//                fragment.arguments = args
//                loadFragment(fragment)
//            }
//        }
    }

    override fun setToolbarTitle(title: String) {
        setToolbarTitle(title, tvTitle)
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }
}