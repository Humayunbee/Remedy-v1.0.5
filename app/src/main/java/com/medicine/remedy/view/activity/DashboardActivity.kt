package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.response.DefaultResponse
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.ExtraUtils.Companion.isOnline
import com.medicine.remedy.utils.ExtraUtils.Companion.startActivityIn
import com.medicine.remedy.view_model.DashboardViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.medicine.remedy.adapter.*
import com.medicine.remedy.databinding.LayoutContentDashboardBinding
import com.medicine.remedy.model.BrandModel
import com.medicine.remedy.utils.AppConstants.Companion.WARNING_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.categoryId
import com.medicine.remedy.utils.AppConstants.Companion.fragmentId
import com.medicine.remedy.utils.AppConstants.Companion.getAlphabets
import com.medicine.remedy.utils.AppConstants.Companion.isFilterApply
import com.medicine.remedy.utils.AppConstants.Companion.productList
import com.medicine.remedy.utils.AppConstants.Companion.toolbarTitle
import com.medicine.remedy.utils.DataValidation
import com.medicine.remedy.utils.KeyboardUtils
import com.medicine.remedy.utils.KeyboardUtils.Companion.hideSoftKeyboard
import com.medicine.remedy.utils.ViewUtils.Companion.horizontalMultiNumberItemRecyclerView
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.utils.ViewUtils.Companion.verticalRecyclerView
import com.medicine.remedy.utils.clearFilters
import com.medicine.remedy.utils.isAnyItemSelected
import com.medicine.remedy.view_model.DashboardViewModel.Companion.productSearchKeyword
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

/**
 * Created by Atik Faysal on 12/20/2020.
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
@SuppressLint("NonConstantResourceId")
class DashboardActivity : BaseActivity() {

    companion object{
        var brandList = ArrayList<BrandModel>()
    }

    private lateinit var brandAdapter : BrandAdapter
    private lateinit var binding : LayoutContentDashboardBinding
    private lateinit var viewModel : DashboardViewModel
    private lateinit var categoryAdapter : CategoryAdapter

    @BindView(R.id.shimmer_animation) lateinit var shimmerAnimation : ShimmerFrameLayout
    @BindView(R.id.empty_view) lateinit var emptyView : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_content_dashboard)
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        binding.lifecycleOwner = this
        ButterKnife.bind(this)

        initialize()
        initializeSearch()
        getDashboardData()
        getBrandsList()
    }

    override fun onStart() {
        super.onStart()
        onSelectBottomMenu(binding.dashboard.ivHome,views)
        viewModel.searchKey.value = ""
        binding.dashboard.etSearch.setText("")
        viewModel.countTotalCartItem()
        viewModel.countNotification()
        if (!spManager.getTokenRegisterStatus()) //if device token is not registered
            viewModel.storeFirebaseToken()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        if (::categoryAdapter.isInitialized) categoryAdapter.notifyDataSetChanged()
    }

    override fun initialize() {
        spManager = SharedPreferenceManager(this)
        binding.dashboard.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.dashboard.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.dashboard.imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        binding.dashboard.imageSlider.scrollTimeInSec = 4 //set scroll delay in seconds
        binding.dashboard.imageSlider.startAutoCycle()
        views = listOf(binding.dashboard.ivHome, binding.dashboard.ivWishList, binding.dashboard.ivUpload, binding.dashboard.ivCart)//list of menu id

        val alphAdapter = SelectCharacterAdapter(this, getAlphabets())
        horizontalMultiNumberItemRecyclerView(this,binding.dashboard.rvAlphabets, 8).adapter = alphAdapter

        DashboardViewModel.mlTotalCartItem.observe(this) {
            if (it > 0) {
                binding.dashboard.cartBadge.visibility = View.VISIBLE
                binding.dashboard.cartBadge.text = "$it"
            } else binding.dashboard.cartBadge.visibility = View.GONE
        }

        DashboardViewModel.mlNotiCount.observe(this) {
            if (it > 0) {
                binding.dashboard.toolbarWhite.notiBadge.visibility = View.VISIBLE
                binding.dashboard.toolbarWhite.notiBadge.text = "$it"
            } else binding.dashboard.toolbarWhite.notiBadge.visibility = View.GONE
        }
    }

    override fun setToolbarTitle(title: String) {}

    @SuppressLint("RtlHardcoded")
    @OnClick(value = [R.id.iv_menu, R.id.iv_notification, R.id.iv_home, R.id.iv_wish_list, R.id.iv_upload,
        R.id.iv_cart,R.id.cv_all_product,R.id.cv_offer_product,R.id.tv_positive_btn, R.id.tv_negative_btn,R.id.iv_cancel,R.id.iv_filter,R.id.iv_keyword_search])
    fun onClickListener(view: View)
    {
        when(view.id)
        {
            R.id.iv_menu-> startActivityIn(this,Intent(this, SettingActivity::class.java))

            R.id.iv_notification-> startActivityIn(this,Intent(this, NotificationActivity::class.java))

            R.id.iv_home -> onSelectBottomMenu(binding.dashboard.ivHome,views)

            R.id.iv_wish_list -> {
                onSelectBottomMenu(binding.dashboard.ivWishList,views)
                startActivityIn(this,Intent(this, FavouriteProductActivity::class.java))
            }

            R.id.iv_upload -> onSelectBottomMenu(binding.dashboard.ivUpload,views)

            R.id.iv_cart -> {
                onSelectBottomMenu(binding.dashboard.ivCart,views)
                startActivityIn(this, Intent(this, OrderActivity::class.java))
            }

            R.id.iv_keyword_search -> searchByKeyword()

            R.id.iv_filter->{
                if (binding.drawer.isDrawerOpen(Gravity.RIGHT))
                    binding.drawer.closeDrawer(Gravity.RIGHT)
                else {
                    if(brandList.size > 0)
                    {
                        val brands = ArrayList<BrandModel>()
                        brands.addAll(brandList)
                        binding.navDrawer.etSearch.setText("")
                        companySearchInitialize()
                        brandAdapter = BrandAdapter(brands)
                        verticalRecyclerView(this, binding.navDrawer.rvList).adapter = brandAdapter
                    }
                    binding.drawer.openDrawer(Gravity.RIGHT)
                }
            }

            R.id.cv_all_product,R.id.cv_offer_product->{
                val intent = Intent(this, ProductListActivity::class.java)
                intent.putExtra(fragmentId, productList)
                intent.putExtra(categoryId, "")
                intent.putExtra(toolbarTitle, "Products")
                startActivityIn(this,intent)
            }

            R.id.tv_positive_btn->{
                if(brandList.isAnyItemSelected())
                {
                    openCloseDrawer()
                    val intent = Intent(this, ProductListActivity::class.java)
                    intent.putExtra(isFilterApply, "1")
                    intent.putExtra(toolbarTitle, "Products")
                    startActivityIn(this,intent)
                }else showToast(this, "Select at least one brand", WARNING_DIALOG)
            }

            R.id.tv_negative_btn ->{
                openCloseDrawer()
                brandList.clearFilters()
                brandAdapter.notifyDataSetChanged()
            }

            R.id.iv_cancel-> openCloseDrawer()
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun openCloseDrawer()
    {
        if (binding.drawer.isDrawerOpen(Gravity.RIGHT))
            binding.drawer.closeDrawer(Gravity.RIGHT)
        else binding.drawer.openDrawer(Gravity.RIGHT)
        hideSoftKeyboard(binding.navDrawer.etSearch, this)
    }

    private fun companySearchInitialize()
    {
        binding.navDrawer.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val text: String = binding.navDrawer.etSearch.text.toString()
                binding.navDrawer.svSearch.setQuery(text, false)
                if(text.isEmpty())
                    hideSoftKeyboard(binding.navDrawer.etSearch,this@DashboardActivity)
            }
        })

        binding.navDrawer.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                brandAdapter.filterList(newText)
                return false
            }
        })
    }

    /**
     * ...search productList by keyword
     * ...observe keyword
     * ...hide keyboard while search box is empty
     * ...when isFilterApply = true and search box is empty remove all filter
     */
    private fun initializeSearch() {
        binding.dashboard.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = binding.dashboard.etSearch.text.toString()
                viewModel.searchSuggestionKey.value = input
                viewModel.searchKey.value = input
                productSearchKeyword.value = input
                Log.d("onSearchKey","$input and ${viewModel.searchKey.value}")
            }
        })

        val suggestAdapter = SearchSuggestAdapter(this, R.layout.list_suggest)
        binding.dashboard.etSearch.setAdapter(suggestAdapter)
        binding.dashboard.etSearch.setOnItemClickListener { _, _, i, l ->
            val keyword = suggestAdapter.getObject(i).productName
            if (!TextUtils.isEmpty(keyword) && DataValidation.inputTextValidator(keyword)) {
                hideSoftKeyboard(binding.dashboard.etSearch, this)
                val intent = Intent(this, ProductListActivity::class.java)
                intent.putExtra(isFilterApply, "0")
                intent.putExtra(toolbarTitle, "Products")
                startActivityIn(this,intent)
            }
        }
        viewModel.searchSuggestions.observe(this){ productList ->
            suggestAdapter.items = productList
        }

//        viewModel.searchKey.observe(this as LifecycleOwner) { s ->
//            productSearchKeyword.value = s
//        }

        binding.dashboard.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                searchByKeyword()
            }
            false
        }
    }

    private fun searchByKeyword()
    {
        val keyword = if (binding.dashboard.etSearch.text != null) binding.dashboard.etSearch.text.toString() else ""
        if (!TextUtils.isEmpty(keyword) && DataValidation.inputTextValidator(keyword)) {
            KeyboardUtils.hideSoftKeyboard(binding.dashboard.etSearch, this)
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra(isFilterApply, "0")
            intent.putExtra(toolbarTitle, "Products")
            startActivityIn(this,intent)
            //Toast.makeText(this, "working",Toast.LENGTH_LONG).show()
        }
    }

    /**
     * ...slider image list
     * ...category list
     * ...popular restaurant list
     * ...most popular products list
     */
    private fun displayData(response : DefaultResponse)
    {
        val adapter = ImageSliderAdapter(this, response.sliderImages)
        binding.dashboard.imageSlider.setSliderAdapter(adapter)

        categoryAdapter = CategoryAdapter(this, lifecycleScope, response.subCategory)
        verticalRecyclerView(this,binding.dashboard.rvHorizontalCategory).adapter = categoryAdapter

        if(response.couponList.isNotEmpty())
        {
            binding.dashboard.rlPromoCode.visibility = View.VISIBLE
            binding.dashboard.tvPromoCode.text = response.couponList[0].couponCode
            binding.dashboard.tvPromoDescription.text = response.couponList[0].details
        }else binding.dashboard.rlPromoCode.visibility = View.GONE
    }

    /**
     * ...checking internet connection
     * ...visible and start animation
     * ...visible constraint layout
     * ...handle server response
     */
    private fun getDashboardData()
    {
        if(!isOnline(this))
        {
            noInternetConnection(binding.dashboard.clRoot)
            return
        }

        shimmerAnimation.visibility = View.VISIBLE
        shimmerAnimation.startShimmerAnimation()
        binding.dashboard.clDashboard.visibility = View.GONE

        viewModel.getDashboardData().observe(this, {

            if(it.responseCode == RESPONSE_SUCCESS_CODE)
            {
                emptyView.visibility = View.GONE
                binding.dashboard.clDashboard.visibility = View.VISIBLE
                displayData(it)
            }else {
                emptyView.visibility = View.VISIBLE
                binding.dashboard.clDashboard.visibility = View.GONE
            }
            shimmerAnimation.stopShimmerAnimation()
            shimmerAnimation.visibility = View.GONE
        })
    }

    private fun getBrandsList()
    {
        if(!isOnline(this))
        {
            noInternetConnection(binding.dashboard.clRoot)
            return
        }

        viewModel.getBrandsList().observe(this, {
            Log.d("responseCode","$it.responseCode")
            if(it.responseCode == RESPONSE_SUCCESS_CODE && it.brandList.isNotEmpty())
            {
                Log.d("brandSize", "${it.brandList.size}")
                brandList.clear()
                brandList.addAll(it.brandList)
            }
        })
    }
}