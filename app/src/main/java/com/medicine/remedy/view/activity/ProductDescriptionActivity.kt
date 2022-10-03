package com.medicine.remedy.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import butterknife.ButterKnife
import butterknife.OnClick
import com.medicine.remedy.R
import com.medicine.remedy.adapter.HorizontalProductAdapter
import com.medicine.remedy.adapter.ProductImageSliderAdapter
import com.medicine.remedy.config.BaseActivity
import com.medicine.remedy.config.SharedPreferenceManager
import com.medicine.remedy.data_model.ProductDescriptionModel
import com.medicine.remedy.databinding.LayoutProductDescriptionBinding
import com.medicine.remedy.interfaces.OnRetryCallback
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.AppConstants.Companion.ERROR_DIALOG
import com.medicine.remedy.utils.AppConstants.Companion.RESPONSE_SUCCESS_CODE
import com.medicine.remedy.utils.AppConstants.Companion.SUCCESS_DIALOG
import com.medicine.remedy.utils.DialogUtils.Companion.noInternetDialogFullScreen
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ExtraUtils.Companion.commaOnAmount
import com.medicine.remedy.utils.ExtraUtils.Companion.decreaseQuantity
import com.medicine.remedy.utils.ExtraUtils.Companion.htmlToPlainText
import com.medicine.remedy.utils.ExtraUtils.Companion.increaseQuantity
import com.medicine.remedy.utils.ExtraUtils.Companion.isOnline
import com.medicine.remedy.utils.ExtraUtils.Companion.startActivityOut
import com.medicine.remedy.utils.LoadingUtils
import com.medicine.remedy.utils.LoadingUtils.Companion.loadingView
import com.medicine.remedy.utils.ViewUtils
import com.medicine.remedy.utils.ViewUtils.Companion.showToast
import com.medicine.remedy.view_model.ProductViewModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/25/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ProductDescriptionActivity : BaseActivity()
{
    private lateinit var binding : LayoutProductDescriptionBinding
    private lateinit var viewModel : ProductViewModel
    private lateinit var productId : String
    private lateinit var productInfo: ProductDescriptionModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_product_description)
        viewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        binding.lifecycleOwner = this
        binding.description = viewModel
        ButterKnife.bind(this)

        initialize()
    }

    override fun onStart() {
        super.onStart()
        if(!isOnline(this))
            noInternetDialogFullScreen(this, retryCallback)
        else getProductDescriptions()
    }

    @OnClick(value = [R.id.iv_back, R.id.iv_increase, R.id.iv_decrease, R.id.cv_add_to_cart, R.id.iv_favourite])
    fun onClickListener(view : View)
    {
        when(view.id)
        {
            R.id.iv_back->{
                finish()
                overridePendingTransition(R.anim.animation_slide_in_left, R.anim.animation_slide_out_right)
            }

            R.id.iv_increase-> viewModel.mlQuantity.value = increaseQuantity(viewModel.mlQuantity.value!!,productInfo.maxQty).toString()//increase quantity

            R.id.iv_decrease->viewModel.mlQuantity.value = decreaseQuantity(viewModel.mlQuantity.value!!).toString()//decrease quantity

            R.id.cv_add_to_cart-> viewModel.addProductToCartList(productInfo)//add product to cart

            R.id.iv_favourite-> {
                if(spManager.getIsUserLoggedIn())
                    productAddToFavourite()
                else ExtraUtils.startActivityIn(this, Intent(this, UserAuthenticationActivity::class.java))
            }
        }
    }

    override fun initialize() {
        alertDialog = loadingView(this)
        progressDialog = LoadingUtils(this)
        spManager = SharedPreferenceManager(this)
        if(intent.getStringExtra(AppConstants.productId) != null)
        {
            productId = intent.getStringExtra(AppConstants.productId)!!
            viewModel.mlProductId.value = productId
        }
        else {
            showToast(this, "Product not found",ERROR_DIALOG)
            finish()
        }

        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        binding.imageSlider.scrollTimeInSec = 4 //set scroll delay in seconds
        binding.imageSlider.startAutoCycle()

        viewModel.mlIsProductAddToCart.observe(this, {
            if(it)
            {
                showToast(this, "Product add to cart", SUCCESS_DIALOG)
                startActivityOut(this)
            }
        })
    }

    override fun setToolbarTitle(title: String) {}

    /**
     * ...get product description from server
     * ...send request to server to get product description
     * ...check internet connection
     * ...handle server response
     * ...display product description
     */
    private fun getProductDescriptions()
    {
        if(!isOnline(this))
        {
            retrySnackBar(binding.clRoot, retryCallback)
            return
        }

        alertDialog.show()//display alertDialog

        viewModel.getProductDescription().observe(this) {
            if (it.responseCode == RESPONSE_SUCCESS_CODE) {
                productInfo = it.productInfo//initialize global variable
                displayDescription(it.productInfo)
                val productAdapter =
                    HorizontalProductAdapter(this, lifecycleScope, it.relatedProduct)
                ViewUtils.horizontalMultiNumberItemRecyclerView(this, binding.rvList, 3).adapter =
                    productAdapter
                //ViewUtils.verticalRecyclerView(mActivity, binding.rvList).adapter = productAdapter
            } else showToast(this, "Product description not found, please retry", ERROR_DIALOG)
            alertDialog.dismiss()//dismiss alertDialog
        }
    }

    /**
     * ...display product description
     * ...set details in component
     */
    @SuppressLint("SetTextI18n")
    private fun displayDescription(model: ProductDescriptionModel)
    {
        binding.tvProductTitle.text = model.productName
        binding.tvPrice.text = resources.getString(R.string.taka)+" "+commaOnAmount(ExtraUtils.roundOffDecimal(model.productPrice))
        binding.tvDetails.text = if(model.longDescription != null) htmlToPlainText(model.longDescription.toString()) else "Not Found"
        binding.tvSpecificationsFeatures.text = if(model.specificationFeature != null) htmlToPlainText(model.specificationFeature.toString()) else "Not Found"
        binding.tvDirectionForUse.text =  if(model.directionOfUse != null) htmlToPlainText(model.directionOfUse.toString()) else "Not Found"
        binding.tvSafetyInformation.text =  if(model.safetyInformation != null) htmlToPlainText(model.safetyInformation.toString()) else "Not Found"
        binding.tvQuantity.text = "1"
        binding.tvUnit.text = if(model.productUnit != null && model.packSize != null) "${model.packSize} ${model.productUnit}" else "Not Found"

        model.productIcons.add(if(model.mainImage != null) model.mainImage!! else "")

        val adapter = ProductImageSliderAdapter(this, model.productIcons)
        binding.imageSlider.setSliderAdapter(adapter)
    }

    /**
     * ...product add to favorites list
     * ...check internet connection
     * ...display and hide loading dialog
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun productAddToFavourite()
    {
        if(!isOnline(this))
        {
            retrySnackBar(binding.clRoot, retryCallback)
            return
        }

        progressDialog.showProgressDialog()

        viewModel.userRepo.productAddToFavourite(productInfo.productId).observe(this, {
            if(it.responseCode == RESPONSE_SUCCESS_CODE)
            {
                showToast(this, "Product add to favourite list", SUCCESS_DIALOG)
                binding.ivFavourite.setImageDrawable(resources.getDrawable(R.drawable.ic_favourite_25_red_bold))
            } else showToast(this, "Failed to add in favourite, please retry",ERROR_DIALOG)

            progressDialog.dismissProgressDialog()
        })
    }

    private val retryCallback = object : OnRetryCallback {
        override fun onRetry() {
            getProductDescriptions()
        }
    }
}