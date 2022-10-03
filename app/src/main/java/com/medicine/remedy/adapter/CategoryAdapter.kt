package com.medicine.remedy.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medicine.remedy.R
import com.medicine.remedy.data_model.CategoryModel
import com.medicine.remedy.utils.AppConstants
import com.medicine.remedy.utils.ExtraUtils
import com.medicine.remedy.utils.ViewUtils.Companion.horizontalRecyclerView
import com.medicine.remedy.view.activity.ProductListActivity
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 1/4/2021
  * Email: humayunfarid1997@gmail.com
 * *** Happy Coding ***
 */
class CategoryAdapter(mContext: Context, private val scope: CoroutineScope, items: List<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>()
{
    private var context = mContext
    private var categoryList : List<CategoryModel> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.model_category_product, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val category : CategoryModel = categoryList[position]
        holder.tvCategoryTitle.text = category.categoryTitle
        val adapter = ProductAdapter(context, scope, category.productList)
        horizontalRecyclerView(context, holder.rvCategory).adapter = adapter

        holder.tvViewAll.setOnClickListener {
            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtra(AppConstants.fragmentId, AppConstants.productList)
            intent.putExtra(AppConstants.categoryId, category.subCategoryId)
            intent.putExtra(AppConstants.toolbarTitle, category.categoryTitle)
            ExtraUtils.startActivityIn(context as Activity, intent)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvCategoryTitle: TextView = view.findViewById(R.id.tv_category)
        var tvViewAll: TextView = view.findViewById(R.id.tv_view_all)
        var rvCategory: RecyclerView = view.findViewById(R.id.rv_list)
    }
}

