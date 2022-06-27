package com.nszalas.bookscheck

import android.widget.Filter

class FilterBook:Filter {

    private var filterList: ArrayList<ModelBook>

    private var adapterBook: AdapterBook

    constructor(filterList: ArrayList<ModelBook>, adapterBook: AdapterBook){
        this.filterList = filterList
        this.adapterBook = adapterBook
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        var results = FilterResults()

        if(constraint != null && constraint.isNotEmpty()){

            constraint = constraint.toString().uppercase()
            var filteredModels:ArrayList<ModelBook> = ArrayList()
            for (i in 0 until filterList.size){
                if (filterList[i].bookTitle.uppercase().contains(constraint)){
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else{
            results.count = filterList.size
            results.values = filterList
        }

        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterBook.bookArrayList = results.values as ArrayList<ModelBook>

        adapterBook.notifyDataSetChanged()
    }
}