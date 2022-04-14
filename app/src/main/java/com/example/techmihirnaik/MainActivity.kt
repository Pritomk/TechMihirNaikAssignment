package com.example.techmihirnaik

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.techmihirnaik.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ivUpload: ImageView
    private lateinit var btnUpload: Button
    private lateinit var btnUploaded: Button
    private var imageUri: Uri? = null
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ivUpload = binding.ivUpload
        btnUpload = binding.btnUpload
        btnUploaded = binding.btnUploaded
        btnLogout = binding.btnLogout

        btnUploaded.setOnClickListener {
            startActivity(Intent(this, UploadedActivity::class.java))
        }

        ivUpload.setOnClickListener {
            imagePic()
        }

        btnUpload.setOnClickListener {
            uploadImageFunc()
        }

        btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            val sharedPreferenceClass = SharedPreferenceClass(this)
            sharedPreferenceClass.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun imagePic() {
        ImagePicker.with(this)
            .crop()
            .compress(2048)
            .maxResultSize(1080, 1080)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            Glide.with(this).load(imageUri).into(ivUpload)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageFunc() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select pic first", Toast.LENGTH_SHORT).show()
            return
        }
        val sharedPreferenceClass = SharedPreferenceClass(this)
        Firebase.storage.reference.child("images/${sharedPreferenceClass.getValueString("token")}").putFile(
            imageUri!!
        )
            .addOnSuccessListener {
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }.addOnCanceledListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()

        val sharedRef = getSharedPreferences("user_tech_mihir_naik", MODE_PRIVATE)
        if (!sharedRef.contains("uid")) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}