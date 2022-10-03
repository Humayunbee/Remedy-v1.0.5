package com.medicine.remedy.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

/**
 * Date 2/4/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
@Entity(tableName = "tbl_cart_item")
class CartItemEntities
{
    @Expose @PrimaryKey @ColumnInfo(name = "product_id")
    lateinit var productId: String

    @ColumnInfo(name = "product_name")
    lateinit var productName: String

    @Expose @ColumnInfo(name = "quantity") var quantity: Int = 0

    @ColumnInfo(name = "unit_price") var unitPrice: Double = 0.0

    @ColumnInfo(name = "total_price") var totalPrice: Double = 0.0

    @ColumnInfo(name = "discount") var discount: Double = 0.0

    @ColumnInfo(name = "image") lateinit var image: String

    @ColumnInfo(name = "packSize") lateinit var packSize: String

    @ColumnInfo(name = "maxQty") var maxQty: Int = 0


    /**
     * ...store extra value
     * ...store as json object string
     * ...extra value like color , size , souce , spicy etc for food
     */
    @ColumnInfo(name = "extra_value")
    lateinit var extraValue: String
}