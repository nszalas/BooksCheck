package com.nszalas.bookscheck

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nszalas.bookscheck.databinding.ActivityAddBookBinding
import com.nszalas.bookscheck.databinding.ActivityCategoryAddBinding

class AddBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.show()

        binding.submitBtn.setOnClickListener {
            validateData()
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private var bookTitle = ""
    private var author = ""
    private var description = ""
    private var read:Boolean = false

    private fun validateData() {

        bookTitle = binding.bookTitleAdd.text.toString().trim()
        author = binding.authorAdd.text.toString().trim()
        description = binding.descriptionAdd.text.toString().trim()

        if(bookTitle.isEmpty() || author.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Wrong data!", Toast.LENGTH_SHORT).show()
        }
        else{
            addBookFirebase()
        }



    }

    private fun addBookFirebase() {
        progressDialog.show()

        val timestamp = System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["bookTitle"] = bookTitle
        hashMap["author"] = author
        hashMap["description"] = description
        hashMap["read"] = read
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"


        val ref = FirebaseDatabase.getInstance().getReference("Books")

        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Book added!", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "Operation failed due to ${e.message}!!!", Toast.LENGTH_SHORT).show()

            }
    }
}