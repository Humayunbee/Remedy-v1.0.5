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
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.adapter.BrandAdapter
import com.medicine.remedy.adapter.ProductInfoAdapter
import com.medicine.remedy.adapter.SearchSuggestAdapter
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.data_model.ProductModel
import com.medicine.remedy.databinding.LayoutNavHolderBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.model.BrandModel
import com.medicine.remedy.utils.*
import com.medicine.remedy.utils.AppConstants.Companion.isFilterApply
import com.medicine.remedy.utils.DataValidation.Companion.inputTextValidator
import com.medicine.remedy.utils.KeyboardUtils.Companion.hideSoftKeyboard
import com.medicine.remedy.utils.ViewUtils.Companion.verticalRecyclerView
import com.medicine.remedy.view_model.DashboardViewModel
import com.medicine.remedy.view_model.DashboardViewModel.Companion.productSearchKeyword
import com.medicine.remedy.view_model.ProductViewModel
import java.util.ArrayList

/**
 * Date 9/6/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
@SuppressLint("NonConstantResourceId")
open class ProductListActivity : BaseActivity()
{
    protected lateinit var binding : LayoutNavHolderBinding
    protected lateinit var viewModel : ProductViewModel

    private var categoryId = ""
    private var isSearchInitiated = false
    private lateinit var productAdapter : ProductInfoAdapter
    protected var productList = ArrayList<ProductModel>()
    private lateinit var brandAdapter : BrandAdapter

    @BindView(R.id.tv_title) lateinit var tvTitle : TextView
    @BindView(R.id.cart_badge) lateinit var tvCartBadge : TextView
    @BindView(R.id.iv_menu) lateinit var ivMenu : ImageView
    @BindView(R.id.iv_filter) lateinit var ivFilter : ImageView
    @BindView(R.id.rl_cart) lateinit var rlCart : RelativeLayout
    @BindView(R.id.iv_notification) lateinit var ivNotification : ImageView

    private  var isFilterByCategory = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_nav_holder)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        binding.lifecycleOwner = this
        ButterKnife.bind(this, view)

        initialize()
        setUpLoadMoreListener()
        initializeSearch()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        if (::productAdapter.isInitialized) productAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        viewModel.countTotalCartItem()

        DashboardViewModel.mlTotalCartItem.observe(this,{
            if(it > 0)
            {
                rlCart.visibility = View.VISIBLE
                tvCartBadge.visibility = View.VISIBLE
                tvCartBadge.text = "$it"
            }else tvCartBadge.visibility = View.GONE
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        isFilterByCategory = false
    }

    override fun initialize() {
        ivNotification.visibility = View.GONE
        ivMenu.visibility = View.GONE
        rlCart.visibility = View.VISIBLE
        ivFilter.visibility = View.VISIBLE

        mContext = this
        mActivity = this
        spManager = SharedPreferenceManager(mContext)
        loadingDialog = LoadingUtils.loadingView(mContext)
        layoutManager = LinearLayoutManager(mContext)

        categoryId = if(intent.getStringExtra(AppConstants.categoryId) != null) intent.getStringExtra(AppConstants.categoryId).toString() else ""
        tvTitle.text = intent.getStringExtra(AppConstants.toolbarTitle).toString()

        productAdapter = ProductInfoAdapter(mActivity, productList, lifecycleScope)
        verticalRecyclerView(mActivity, binding.products.rvList).adapter = productAdapter

        viewModel.mlIsLoading.observe(mActivity as LifecycleOwner) { aBoolean ->
            binding.products.progressBar.visibility = if (aBoolean) View.VISIBLE else View.GONE
        }

        if(intent.getStringExtra(isFilterApply) == "1")
        {
            isFilterByCategory = true
            productListByBrandObserver()
        }else {
            if(productSearchKeyword.value == null || !inputTextValidator(productSearchKeyword.value.toString()))
            {
                if(!TextUtils.isEmpty(categoryId))
                    categoryWiseProductListObserver(pageNumber,true)
                else productListObserver(pageNumber,true)
            }else {
                productList.clear()
                pageNumber = 1
                hideSoftKeyboard(binding.products.etSearch, mContext)
                isSearchInitiated = true
                viewModel.searchKey.value = productSearchKeyword.value.toString()
                binding.products.etSearch.setText(productSearchKeyword.value.toString())
                searchProductListObserver()
            }
        }
    }

    override fun setToolbarTitle(title: String) {}

    @SuppressLint("NonConstantResourceId", "RtlHardcoded")
    @OnClick(value = [R.id.iv_cancel_search, R.id.iv_filter,R.id.iv_cancel,R.id.tv_positive_btn, R.id.tv_negative_btn,R.id.tv_title,R.id.rl_cart])
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

            R.id.rl_cart-> ExtraUtils.startActivityIn(this, Intent(this, OrderActivity::class.java))

            R.id.iv_cancel_search->{
                binding.products.etSearch.setText("")
                binding.products.etSearch.clearFocus()
                hideSoftKeyboard(binding.products.etSearch, mActivity)
            }

            R.id.iv_filter->{
                if (binding.drawer.isDrawerOpen(Gravity.RIGHT))
                    binding.drawer.closeDrawer(Gravity.RIGHT)
                else {
                    if(DashboardActivity.brandList.size > 0)
                    {
                        val brands = ArrayList<BrandModel>()
                        brands.addAll(DashboardActivity.brandList)
                        binding.navDrawer.etSearch.setText("")
                        companySearchInitialize()
                        brandAdapter = BrandAdapter(brands)
                        verticalRecyclerView(this, binding.navDrawer.rvList).adapter = brandAdapter
                    }
                    binding.drawer.openDrawer(Gravity.RIGHT)
                }
            }

            R.id.iv_cancel->{
                openCloseDrawer()
            }

            R.id.tv_positive_btn->{
                if(DashboardActivity.brandList.isAnyItemSelected())
                {
                    openCloseDrawer()
                    productList.clear()
                    pageNumber = 1
                    productListByBrandObserver()
                }else ViewUtils.showToast(this, "Select at least one brand", AppConstants.WARNING_DIALOG)
            }

            R.id.tv_negative_btn->{
                openCloseDrawer()
                DashboardActivity.brandList.clearFilters()
                brandAdapter.notifyDataSetChanged()
                isFilterByCategory = false
                if(productSearchKeyword.value == null || !inputTextValidator(productSearchKeyword.value.toString()))
                {
                    if(!TextUtils.isEmpty(categoryId))
                        categoryWiseProductListObserver(pageNumber,true)
                    else productListObserver(pageNumber,true)
                }else {
                    productList.clear()
                    pageNumber = 1
                    hideSoftKeyboard(binding.products.etSearch, mContext)
                    isSearchInitiated = true
                    viewModel.searchKey.value = productSearchKeyword.value.toString()
                    searchProductListObserver()
                }
            }
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

    /**
     * ...search productList by keyword
     * ...observe keyword
     * ...hide keyboard while search box is empty
     * ...when isFilterApply = true and search box is empty remove all filter
     */
    private fun initializeSearch() {
        binding.products.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = binding.products.etSearch.text.toString()
                viewModel.searchSuggestionKey.value = input
                viewModel.searchKey.value = input
                Log.d("onSearchKey","$input and ${viewModel.searchKey.value}")
            }
        })

        val suggestAdapter = SearchSuggestAdapter(this, R.layout.list_suggest)
        binding.products.etSearch.setAdapter(suggestAdapter)
        binding.products.etSearch.setOnItemClickListener { _, _, i, l ->
            val keyword = suggestAdapter.getObject(i).productName

            if (!TextUtils.isEmpty(keyword) && inputTextValidator(keyword)) {
                productList.clear()
                pageNumber = 1
                hideSoftKeyboard(binding.products.etSearch, mContext)
                isSearchInitiated = true
                searchProductListObserver()
            }

        }

        viewModel.searchSuggestions.observe(this){ productList ->
            suggestAdapter.items = productList
        }

        

        viewModel.searchKey.observe(this) { s ->
            if (s.equals("")) {
                binding.products.ivCancelSearch.visibility = View.GONE
                hideSoftKeyboard(binding.products.etSearch, mContext)
                pageNumber = 1
                productList.clear()
                isSearchInitiated = false
                productListObserver(pageNumber,true)
            } else
            {
                //binding.products.etSearch.setText(s)
                binding.products.ivCancelSearch.visibility = View.VISIBLE
            }
        }

        binding.products.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                val keyword = if (binding.products.etSearch.text != null) binding.products.etSearch.text.toString() else ""
                if (!TextUtils.isEmpty(keyword) && inputTextValidator(keyword)) {
                    productList.clear()
                    pageNumber = 1
                    hideSoftKeyboard(binding.products.etSearch, mContext)
                    isSearchInitiated = true
                    searchProductListObserver()
                }
            }
            false
        }
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
                    hideSoftKeyboard(binding.navDrawer.etSearch,this@ProductListActivity)
            }
        })

        binding.navDrawer.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("searchKey",newText)
                brandAdapter.filterList(newText)
                return false
            }
        })
    }

    /**
     * ...display product list on recyclerview
     * ...check internet connection
     * ...display/hide loading view
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun searchProductListObserver()
    {
        if(!ExtraUtils.isOnline(this))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        loadingDialog.show()

        viewModel.searchProductList().observe(this, {
            if(it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE && it.products.isNotEmpty())
            {
                binding.products.emptyView.visibility = View.GONE
                binding.products.rvList.visibility = View.VISIBLE
                productList.addAll(it.products)
                productAdapter.notifyDataSetChanged()
            }else{
                if (productList.size == 0) {
                    binding.products.emptyView.visibility = View.VISIBLE
                    binding.products.rvList.visibility = View.GONE
                }
            }

            loadingDialog.dismiss()
        })
    }

    /**
     * ...display product list on recyclerview
     * ...check internet connection
     * ...display/hide loading view
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun productListObserver(pageNumber : Int, isLoading : Boolean)
    {
        if(!ExtraUtils.isOnline(this))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        if(isLoading)
            loadingDialog.show()

        viewModel.productList(pageNumber).observe(this, {
            if(it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE && it.products.isNotEmpty())
            {
                binding.products.emptyView.visibility = View.GONE
                binding.products.rvList.visibility = View.VISIBLE
                productList.addAll(it.products)
                productAdapter.notifyDataSetChanged()

                isAnyMoreDataAvailable = it.currentPage != it.totalPage
            }else{
                if (productList.size == 0) {
                    binding.products.emptyView.visibility = View.VISIBLE
                    binding.products.rvList.visibility = View.GONE
                }

                isAnyMoreDataAvailable = false
            }

            loadingDialog.dismiss()
        })
    }

    /**
     * ...display product list on recyclerview
     * ...check internet connection
     * ...display/hide loading view
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun categoryWiseProductListObserver(pageNumber : Int, isLoading : Boolean)
    {
        if(!ExtraUtils.isOnline(this))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        if(isLoading)
            loadingDialog.show()

        viewModel.categoryWiseProductList(categoryId,pageNumber).observe(this, {
            if(it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE && it.products.isNotEmpty())
            {
                binding.products.emptyView.visibility = View.GONE
                binding.products.rvList.visibility = View.VISIBLE
                productList.addAll(it.products)
                productAdapter.notifyDataSetChanged()
                isAnyMoreDataAvailable = it.currentPage != it.totalPage
            }else{
                if (productList.size == 0) {
                    binding.products.emptyView.visibility = View.VISIBLE
                    binding.products.rvList.visibility = View.GONE
                }

                isAnyMoreDataAvailable = false
            }

            loadingDialog.dismiss()
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun productListByBrandObserver()
    {
        if(!ExtraUtils.isOnline(this))
        {
            DialogUtils.noInternetDialogFullScreen(mActivity, callBack)
            return
        }

        loadingDialog.show()

        viewModel.searchByBrand().observe(this, {
            if(it.responseCode == AppConstants.RESPONSE_SUCCESS_CODE && it.products.isNotEmpty())
            {
                binding.products.emptyView.visibility = View.GONE
                binding.products.rvList.visibility = View.VISIBLE
                productList.addAll(it.products)
                productAdapter.notifyDataSetChanged()
                isFilterByCategory = true
            }else{
                if (productList.size == 0) {
                    binding.products.emptyView.visibility = View.VISIBLE
                    binding.products.rvList.visibility = View.GONE
                }
            }

            loadingDialog.dismiss()
        })
    }

    /**
     * setting listener to get callback for load more
     */
    override fun setUpLoadMoreListener() {
        binding.products.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0)
                {
                    layoutManager = binding.products.rvList.layoutManager as LinearLayoutManager
                    totalItemCount = layoutManager.itemCount
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (!viewModel.mlIsLoading.value!! && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                        if (!isAnyMoreDataAvailable || isSearchInitiated) return
                        pageNumber++
                        if(!TextUtils.isEmpty(categoryId) && !isFilterByCategory)
                            categoryWiseProductListObserver(pageNumber,false)
                        else productListObserver(pageNumber,false)
                        viewModel.mlIsLoading.value = true
                    }
                }
            }
        })
    }

    protected val callBack = (object : OnRetryCallback {
        override fun onRetry() {
            if(!TextUtils.isEmpty(categoryId))
                categoryWiseProductListObserver(pageNumber,true)
            else productListObserver(pageNumber,true)
        }
    })
}