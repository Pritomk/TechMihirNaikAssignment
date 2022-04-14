package com.example.techmihirnaik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.techmihirnaik.databinding.ActivityUploadedBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UploadedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadedBinding
    private lateinit var ivUploaded: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ivUploaded = binding.ivUploaded

        val sharedPreferenceClass = SharedPreferenceClass(this)
        Firebase.storage.reference.child("images/${sharedPreferenceClass.getValueString("token")}").downloadUrl
            .addOnSuccessListener {
                Glide.with(this).load(it).into(ivUploaded)
            }
            .addOnCanceledListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}