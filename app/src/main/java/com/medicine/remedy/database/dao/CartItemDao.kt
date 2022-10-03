package com.medicine.remedy.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medicine.remedy.database.entities.CartItemEntities
import io.reactivex.Single

/**
 * Date 2/4/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
@Dao
interface CartItemDao
{
    /**
     * ...insert a product into db
     * ...if any product already exist in db
     * ...then update the previous one with current product
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItems(cartItems: CartItemEntities) : Long

    @Query("SELECT * FROM tbl_cart_item")
    fun getCartItems(): Single<List<CartItemEntities>>

    @Query("UPDATE tbl_cart_item SET quantity = :qty, total_price = :totalPrice WHERE product_id = :id")
    fun updateCartQty(id: String, qty: Int, totalPrice: Double)

    @Query("UPDATE tbl_cart_item SET quantity = :qty, total_price = :totalPrice WHERE product_id = :id")
    suspend fun updateCartQty2(id: String, qty: Int, totalPrice: Double)

    @Query("DELETE FROM tbl_cart_item")
    fun removeOfflineCartItem()

    @Query("DELETE FROM tbl_cart_item WHERE product_id = :id")
    fun removeCartItem(id: String)

    @Query("DELETE FROM tbl_cart_item WHERE product_id = :id")
    suspend fun removeCartItem2(id: String)

    @Query("SELECT * FROM tbl_cart_item WHERE product_id = :id")
    suspend fun getCartItem(id: String): CartItemEntities?

    @Query("SELECT COUNT(*) FROM tbl_cart_item")
    fun countTotalCartItem() : Long
}