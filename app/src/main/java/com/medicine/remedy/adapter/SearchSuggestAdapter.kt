package com.medicine.remedy.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import com.medicine.remedy.data_model.ProductModel

class SearchSuggestAdapter(context: Context, resource: Int): ArrayAdapter<String>(context, resource) {
    var items: List<ProductModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): String {
        return items[position].productName
    }

    fun getObject(position: Int) = items[position]

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
               val results = FilterResults()
                if (charSequence != null){
                    results.values = items
                    results.count = items.size
                }
                return  results
            }

            override fun publishResults(charSequence: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0){
                    notifyDataSetChanged()
                }else{
                    notifyDataSetInvalidated()
                }
            }

        }

    }

}