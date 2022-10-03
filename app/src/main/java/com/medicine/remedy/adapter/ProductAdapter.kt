package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.data_model.ProductDescriptionModel
import com.medicine.remedy.data_model.ProductModel
import com.medicine.remedy.database.entities.CartItemEntities
import com.medicine.remedy.model.OrderRepository
import com.medicine.remedy.model.ProductRepository
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ExtraUtils.Companion.commaOnAmount
import com.medicine.remedy.utils.ExtraUtils.Companion.roundOffDecimal
import com.medicine.remedy.utils.ImageUtils.Companion.displayImage
import com.medicine.remedy.utils.MathematicsUtils
import com.medicine.remedy.view_model.DashboardViewModel
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/4/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
open class ProductAdapter(mContext: Context, private val scope: CoroutineScope, items : List<ProductModel>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>()
{
    private val cartRepo : OrderRepository = OrderRepository()
    private val repository : ProductRepository = ProductRepository()
    protected var context = mContext
    protected var productList : List<ProductModel> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_product, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productModel : ProductModel = productList[position]
        holder.tvProductTitle.text = productModel.productName
        holder.tvPrice.text = context.resources.getString(R.string.taka)+" "+ commaOnAmount(roundOffDecimal(productModel.productPrice))

        if(productModel.productPrice < productModel.mrpRate)
        {
            holder.tvDiscountPrice.visibility = View.VISIBLE
            holder.tvDiscount.visibility = View.VISIBLE
            holder.tvPrice.text = "${commaOnAmount(roundOffDecimal(productModel.productPrice))} ${context.resources.getString(R.string.taka)}"
            holder.tvDiscountPrice.text = "${commaOnAmount(roundOffDecimal(productModel.mrpRate))} ${context.resources.getString(R.string.taka)}"
            holder.tvDiscountPrice.paintFlags = holder.tvDiscountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.tvDiscount.text = "${MathematicsUtils.calculateDiscountPercentage(productModel.productPrice, productModel.mrpRate)}% Off"
        }else {
            holder.tvDiscountPrice.visibility = View.INVISIBLE
            holder.tvDiscount.visibility = View.GONE
        }

        val prodDescription = "${if(productModel.subCategoryName != null)productModel.subCategoryName else ""} ${if(productModel.brandName != null) " | "+productModel.brandName else ""}"
        holder.tvDescription.text = prodDescription

       /* holder.itemView.setOnClickListener {
            val intent = Intent(context as Activity, ProductDescriptionActivity::class.java)
            intent.putExtra(productId, productModel.productId)
            startActivityIn(context as Activity, intent)
        }*/

        displayImage(context as Activity, holder.ivImage, productModel.productIcon.toString())
        scope.launch {
            cartRepo.getItemFromCart(productModel.productId)?.let { cartItem ->
                showQuantity(holder, true, cartItem.quantity)
            } ?: run {
                showQuantity(holder, false)
            }
        }


        holder.ivIncrease.setOnClickListener {
            scope.launch {
                cartRepo.getItemFromCart(productModel.productId)?.let { cartItem ->
                    val quantity = ExtraUtils.increaseQuantity(cartItem.quantity.toString(), cartItem.maxQty)
                    val totalPrice = quantity * cartItem.unitPrice
                    cartRepo.updateCartQty2(cartItem.productId, quantity, totalPrice)
                    showQuantity(holder, true, quantity)
                }
            }


        }
        holder.ivDecrease.setOnClickListener {
            scope.launch {
                cartRepo.getItemFromCart(productModel.productId)?.let { cartItem ->
                    if (cartItem.quantity <= 1){
                        showQuantity(holder, false)
                        cartRepo.removeItemFromCart2(productModel.productId)
                        Toast.makeText(context,"Removed from Cart", Toast.LENGTH_SHORT).show()
                        countTotalCartItem()
                    }else{
                        val quantity =  ExtraUtils.decreaseQuantity(cartItem.quantity.toString())
                        val totalPrice = quantity * cartItem.unitPrice
                        cartRepo.updateCartQty2(cartItem.productId, quantity, totalPrice)
                        showQuantity(holder, true, quantity)
                    }


                }
            }

        }


        holder.tvAddCart.setOnClickListener {
            val model = ProductDescriptionModel()
            model.productId = productModel.productId
            model.productName = productModel.productName
            model.productPrice = productModel.productPrice
            model.discount = productModel.productDiscount
            model.subCategory = if(productModel.subCategoryName != null)productModel.subCategoryName!! else ""
            model.productUnit = productModel.productUnit
            model.packSize = productModel.packSize
            model.mainImage = productModel.productIcon
            model.maxQty = productModel.salesQtyLimit

            addProductToCartList(model)
            showQuantity(holder, true)
        }



    }



    private fun showQuantity(holder: ViewHolder, show: Boolean, count: Int = 1) {
        holder.tvAddCart.isVisible = !show
        holder.cvQuantity.isVisible = show
        holder.tvQuantity.text = "$count"
    }

    /**
     * ...add a new product to the cart list
     * ...add to local db
     */
    @SuppressLint("CheckResult")
    fun addProductToCartList(productInfo: ProductDescriptionModel)
    {
        val cartItemEntities = CartItemEntities()

        cartItemEntities.productId = productInfo.productId
        cartItemEntities.productName = productInfo.productName
        cartItemEntities.quantity = 1
        cartItemEntities.unitPrice = productInfo.productPrice
        cartItemEntities.totalPrice = productInfo.productPrice * 1
        cartItemEntities.discount = productInfo.discount
        cartItemEntities.extraValue = productInfo.subCategory
        cartItemEntities.maxQty = productInfo.maxQty
        cartItemEntities.packSize = if(productInfo.productUnit != null && productInfo.packSize != null) "${productInfo.packSize} ${productInfo.productUnit}" else ""
        cartItemEntities.image = if(productInfo.mainImage != null) productInfo.mainImage.toString() else ""

        Completable.fromRunnable {
            cartRepo.addProductToCart(cartItemEntities)
            countTotalCartItem()
        }
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("response","Success entry")
            },{
                Log.d("response","failed entry")
            })

        Toast.makeText(context,"Added to Cart", Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("CheckResult")
    fun countTotalCartItem()
    {
        Completable.fromRunnable {
            DashboardViewModel.mlTotalCartItem.postValue(repository.getTotalCartItem().toInt())
        }.subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("totalCartItem","${DashboardViewModel.mlTotalCartItem.value}")
            },{
                Log.d("room_db_error","$it")
            })
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvProductTitle: TextView = view.findViewById(R.id.tv_product_title)
        var tvPrice: TextView = view.findViewById(R.id.tv_price)
        var tvDiscountPrice: TextView = view.findViewById(R.id.tv_discount_price)
        var tvDiscount: TextView = view.findViewById(R.id.tv_discount)
        var tvDescription: TextView = view.findViewById(R.id.tv_description)
        var tvAddCart: TextView = view.findViewById(R.id.tv_add_to_cart)
        var cvQuantity: CardView = view.findViewById(R.id.cv_quantity)
        var tvQuantity: TextView = view.findViewById(R.id.tv_quantity)
        var ivIncrease: ImageView = view.findViewById(R.id.iv_increase)
        var ivDecrease: ImageView = view.findViewById(R.id.iv_decrease)
        var ivImage: ImageView = view.findViewById(R.id.iv_image)
    }
}
