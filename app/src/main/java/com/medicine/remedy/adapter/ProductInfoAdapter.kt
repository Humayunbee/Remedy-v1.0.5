package com.medicine.remedy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medicine.remedy.R
import com.medicine.remedy.data_model.ProductModel
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 9/5/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class ProductInfoAdapter(
    mContext: Context,
    items: List<ProductModel>,
    scope: CoroutineScope,
) : ProductAdapter(mContext, scope, items)
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_product_vertical, parent, false)
        return ViewHolder(view)
    }

}