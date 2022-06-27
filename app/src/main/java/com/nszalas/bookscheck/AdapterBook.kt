package com.nszalas.bookscheck

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.nszalas.bookscheck.databinding.RowBookBinding

class AdapterBook: RecyclerView.Adapter<AdapterBook.HolderBook>, Filterable {

    private val context:Context
    var bookArrayList: ArrayList<ModelBook>
    private var filterList: ArrayList<ModelBook>

    private var filter: FilterBook? = null

    private lateinit var binding: RowBookBinding

    constructor(context: Context, bookArrayList: ArrayList<ModelBook>){
        this.context = context
        this.bookArrayList = bookArrayList
        this.filterList = bookArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterBook.HolderBook {
        binding = RowBookBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderBook(binding.root)
    }

    override fun onBindViewHolder(holder: AdapterBook.HolderBook, position: Int) {
        val model = bookArrayList[position]
        val id = model.id
        val bookTitle = model.bookTitle
        val author = model.author
        val description = model.description
        var read = model.read
        val timestamp = model.timestamp
        val uid = model.uid



        holder.bookTitleTv.text = bookTitle
        holder.authorTv.text = author
        holder.descriptionTv.text = description

        if(read == true){
        holder.favBtn.setImageResource(R.drawable.ic_fav_2)
        }
        else{
            holder.favBtn.setImageResource(R.drawable.ic_fav_1)
        }

        holder.favBtn.setOnClickListener{
            if(read == false){
                holder.favBtn.setImageResource(R.drawable.ic_fav_2)
                //inFavs(model, holder, position)
                read = true

            }
            else{
                holder.favBtn.setImageResource(R.drawable.ic_fav_1)
                //inFavs(model, holder, position)
                read = false
            }

        }

        holder.deleteBtn.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete $bookTitle ?")
                .setPositiveButton("Yes"){a, d ->
                    Toast.makeText(context,"Deleting...", Toast.LENGTH_SHORT).show()
                    deleteBook(model, holder)
                }
                .setNegativeButton("No"){a, d ->
                    a.dismiss()
                }
                .show()
        }

    }

   private fun inFavs(model: ModelBook, holder: HolderBook, int: Int) {

       val model = bookArrayList[int]
       val id = model.id
       val bookTitle = model.bookTitle
       val author = model.author
       val description = model.description
       var read = model.read
       val timestamp = model.timestamp
       val uid = model.uid

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["id"] = id
        hashMap["bookTitle"] = bookTitle
        hashMap["author"] = author
        hashMap["description"] = description
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = uid

       if (read == true) {
           val read2 = false
           hashMap["read"] = "$read2"
           val ref = FirebaseDatabase.getInstance().getReference("Books")
           ref.child(id!!)
               .updateChildren(hashMap)
       }
       else {
           val read3 = true
           hashMap["read"] = "$read3"
           val ref = FirebaseDatabase.getInstance().getReference("Books")
           ref.child(id!!)
               .updateChildren(hashMap)
       }

    }


    private fun deleteBook(model: ModelBook, holder: HolderBook) {
        val id = model.id

        val ref = FirebaseDatabase.getInstance().getReference("Books")

        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Deleted...",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e ->
                Toast.makeText(context,"Operation failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }

    }

    override fun getItemCount(): Int {
        return bookArrayList.size
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterBook(filterList,this)
        }
        return filter as FilterBook
    }

    inner class HolderBook(itemView: View): RecyclerView.ViewHolder(itemView){
        var bookTitleTv: TextView = binding.bookTitleTv
        var authorTv: TextView = binding.authorTv
        var descriptionTv: TextView = binding.descriptionTv
        var favBtn: ImageButton = binding.favBtn
        var deleteBtn: ImageButton = binding.deleteBtn
    }
}