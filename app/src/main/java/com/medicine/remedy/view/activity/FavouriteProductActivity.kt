package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.adapter.ProductInfoAdapter
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.databinding.LayoutRecyclerViewToolbarBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.*
import com.medicine.remedy.view_model.DashboardViewModel
import com.medicine.remedy.view_model.UserInformationViewModel

/**
 * Date 4/11/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class FavouriteProductActivity : BaseActivity()
{
    private lateinit var binding : LayoutRecyclerViewToolbarBinding
    private lateinit var viewModel : UserInformationViewModel
    @BindView(R.id.tv_title) lateinit var tvTitle : TextView
    @BindView(R.id.iv_menu) lateinit var ivMenu : ImageView
    @BindView(R.id.rl_cart) lateinit var rlCart : RelativeLayout
    @BindView(R.id.cart_badge) lateinit var tvCartBadge : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_recycler_view_toolbar)
        viewModel = ViewModelProviders.of(this).get(UserInformationViewModel::class.java)
        binding.lifecycleOwner = this
        binding.favourite = viewModel
        ButterKnife.bind(this)

        initialize()
    }

    override fun onStart() {
        super.onStart()
        onSelectBottomMenu(binding.ivWishList,views)
        if(!ExtraUtils.isOnline(this))
        {
            DialogUtils.noInternetDialogFullScreen(this, callBack)
            return
        }else {
            ExtraUtils.isUserLoggedIn(this)
            favouriteProductList()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initialize() {
        alertDialog = LoadingUtils.loadingView(this)
        progressDialog = LoadingUtils(this)
        spManager = SharedPreferenceManager(this)
        binding.rvList.visibility = GONE
        ivMenu.visibility = GONE
        rlCart.visibility = VISIBLE
        tvTitle.text = "Favourite products"
        ViewUtils.onClickToolbarMenu(this, binding.toolbar.root)
        views = listOf(binding.ivHome, binding.ivWishList, binding.ivUpload, binding.ivCart)//list of menu id

        DashboardViewModel.mlTotalCartItem.observe(this,{
            if(it > 0)
            {
                rlCart.visibility = VISIBLE
                tvCartBadge.visibility = VISIBLE
                tvCartBadge.text = "$it"
            }else tvCartBadge.visibility = GONE
        })
    }

    override fun setToolbarTitle(title: String) {}

    @OnClick(value = [R.id.tv_title,R.id.rl_cart])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.tv_title->{
                if (supportFragmentManager.backStackEntryCount > 0)
                    supportFragmentManager.popBackStack()
                else
                {
                    finish()
                    ExtraUtils.startActivityOut(this)
                }
            }

            R.id.rl_cart-> ExtraUtils.startActivityIn(this, Intent(this, OrderActivity::class.java))
        }
    }

    /**
     * ...display order notification list on recyclerview
     * ...check internet connection
     * ...display/hide loading view
     */
    private fun favouriteProductList()
    {
        if(!ExtraUtils.isOnline(this))
        {
            DialogUtils.noInternetDialogFullScreen(this, callBack)
            return
        }

        progressDialog.showProgressDialog()

        viewModel.getFavouriteProductList().observe(this) {
            Log.d("responseCode", "${it.responseCode}")
            if (it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE && it.favProducts.isNotEmpty()) {
                binding.emptyView.visibility = GONE
                binding.rvList.visibility = VISIBLE
                val productAdapter = ProductInfoAdapter(this, it.favProducts, lifecycleScope)
                ViewUtils.horizontalMultiNumberItemRecyclerView(this, binding.rvList, 3).adapter =
                    productAdapter
            } else {
                binding.emptyView.visibility = VISIBLE
                binding.rvList.visibility = GONE
            }

            progressDialog.dismissProgressDialog()
        }
    }

    private val callBack = (object : OnRetryCallback {
        override fun onRetry() {
            favouriteProductList()
        }
    })
}