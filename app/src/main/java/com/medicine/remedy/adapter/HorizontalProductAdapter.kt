package com.medicine.remedy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medicine.remedy.R
import com.medicine.remedy.data_model.ProductModel
import kotlinx.coroutines.CoroutineScope

class HorizontalProductAdapter(mContext: Context, scope: CoroutineScope, items : List<ProductModel>) : ProductAdapter(mContext, scope, items)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_product_horizontal, parent, false)
        return ViewHolder(view)
    }
}