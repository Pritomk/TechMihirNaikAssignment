package com.example.techmihirnaik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.techmihirnaik.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var evEmail: EditText
    private lateinit var evPassword: EditText
    private lateinit var btnLogin: TextView
    private lateinit var btnReg: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferenceClass: SharedPreferenceClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth

        sharedPreferenceClass = SharedPreferenceClass(this)

        evEmail = binding.evEmail
        evPassword = binding.evPassword
        btnLogin = binding.btnLogin
        btnReg = binding.btnReg
        progressBar = binding.progressBar

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnReg.setOnClickListener {
            regFunc()
        }

    }

    private fun regFunc() {
        progressBar.visibility = View.VISIBLE

        val email = evEmail.text.toString()
        val password = evPassword.text.toString()

        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (password.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {

                    sharedPreferenceClass.setValueString("uid", it.user?.uid)

                    startActivity(Intent(this, MainActivity::class.java))
                    progressBar.visibility = View.GONE
                }.addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            } else {
                progressBar.visibility = View.GONE
                evPassword.error = "Enter password"
            }
        } else {
            progressBar.visibility = View.GONE
            evEmail.error = "Enter email address"
        }
    }

    override fun onStart() {
        super.onStart()

        val sharedRef = getSharedPreferences("user_tech_mihir_naik", MODE_PRIVATE)
        if (sharedRef.contains("uid")) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}