package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.database.entities.CartItemEntities
import com.medicine.remedy.model.OrderRepository
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ImageUtils
import com.medicine.remedy.utils.MathematicsUtils
import com.medicine.remedy.view_model.DashboardViewModel
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 2/5/2021
 * Email: humayunfarid1997@gmail.com
 * * *** Happy Coding ***
 */
class CartItemListAdapter(mContext: Context, items: List<CartItemEntities>) : RecyclerView.Adapter<CartItemListAdapter.ViewHolder>()
{
    private var context = mContext
    private var cartItems : ArrayList<CartItemEntities> = items as ArrayList<CartItemEntities>
    private val repository = OrderRepository()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_cart_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartModel : CartItemEntities = cartItems[position]

        holder.tvProductName.text = cartModel.productName + if(cartModel.packSize != "") " (${cartModel.packSize})" else ""
        holder.tvPrice.text = "${context.resources.getString(R.string.taka)} ${ExtraUtils.commaOnAmount(ExtraUtils.roundOffDecimal(cartModel.totalPrice))}"
        holder.tvQuantity.text = "${cartModel.quantity}"
        holder.tvSize.text = Html.fromHtml("<font color=#A6B4D0>Type: </font> ${cartModel.extraValue}")
        MathematicsUtils.calculateAmount(context, cartItems)

        ImageUtils.displayImage(context as Activity, holder.ivProductImage, cartModel.image )

        holder.ivIncrease.setOnClickListener {
            cartModel.quantity = ExtraUtils.increaseQuantity(cartModel.quantity.toString(), cartModel.maxQty)//increase quantity
            cartModel.totalPrice = cartModel.quantity * cartModel.unitPrice
            notifyDataSetChanged()

            Completable.fromRunnable { repository.updateCartQty(cartModel.productId,cartModel.quantity, cartModel.totalPrice)}
                .subscribeOn(Schedulers.io())
                .subscribe()

            MathematicsUtils.calculateAmount(context, cartItems)
        }

        holder.ivDecrease.setOnClickListener {
            cartModel.quantity = ExtraUtils.decreaseQuantity(cartModel.quantity.toString())//increase quantity
            cartModel.totalPrice = cartModel.quantity * cartModel.unitPrice
            notifyDataSetChanged()

            Completable.fromRunnable { repository.updateCartQty(cartModel.productId,cartModel.quantity, cartModel.totalPrice)}
                .subscribeOn(Schedulers.io())
                .subscribe()

            MathematicsUtils.calculateAmount(context, cartItems)
        }

        holder.ivRemove.setOnClickListener {
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    private fun removeItem(position: Int) {
        try{
            Completable.fromRunnable {
                repository.removeItemFromCart(cartItems[position].productId)
                cartItems.remove(cartItems[position])
            }.subscribeOn(Schedulers.io()).subscribe()
            notifyDataSetChanged()
            MathematicsUtils.calculateAmount(context, cartItems)
            countTotalCartItem()
        }catch (ex : Exception)
        { }
    }

    @SuppressLint("CheckResult")
    private fun countTotalCartItem()
    {
        Completable.fromRunnable {
            DashboardViewModel.mlTotalCartItem.postValue(repository.getTotalCartItem().toInt())
        }.subscribeOn(Schedulers.io())
            .subscribe({},{
                Log.d("room_db_error","$it")
            })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvProductName: TextView = view.findViewById(R.id.tv_product_name)
        var tvPrice: TextView = view.findViewById(R.id.tv_price)
        var tvSize: TextView = view.findViewById(R.id.tv_size)
        var tvQuantity: TextView = view.findViewById(R.id.tv_quantity)
        var ivIncrease: ImageView = view.findViewById(R.id.iv_increase)
        var ivDecrease: ImageView = view.findViewById(R.id.iv_decrease)
        var ivProductImage: ImageView = view.findViewById(R.id.iv_image)
        var ivRemove: ImageView = view.findViewById(R.id.iv_remove)
    }
}

