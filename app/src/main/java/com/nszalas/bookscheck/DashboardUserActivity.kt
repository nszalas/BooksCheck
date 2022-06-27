package com.nszalas.bookscheck

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nszalas.bookscheck.databinding.ActivityDashboardUserBinding

class DashboardUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardUserBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var bookArrayList: ArrayList<ModelBook>
    private lateinit var adapterBook: AdapterBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadBooks()

        binding.logOutBtn.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.addBookBtn.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }


        binding.searchEt.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adapterBook.filter!!.filter(s)
                }
                catch (e: Exception){
                    Log.d(TAG, "onTextChangerd: ${e.message}")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    private fun loadBooks() {
        bookArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(ModelBook::class.java)

                    bookArrayList.add(model!!)
                }

                adapterBook = AdapterBook(this@DashboardUserActivity, bookArrayList)

                binding.booksRv.adapter = adapterBook
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            binding.subTitleTv.text = "Not logged in"
        }
        else{
            val email = firebaseUser. email

            binding.subTitleTv.text = email
        }
    }
}