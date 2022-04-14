package com.example.techmihirnaik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.techmihirnaik.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var evEmail: EditText
    private lateinit var evPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnReg: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferenceClass: SharedPreferenceClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth

        sharedPreferenceClass = SharedPreferenceClass(this)

        evEmail = binding.evEmail
        evPassword = binding.evPassword
        btnLogin = binding.btnLogin
        btnReg = binding.btnReg
        progressBar = binding.progressBar

        btnReg.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            loginFunc()
        }
    }

    private fun loginFunc() {

        progressBar.visibility = View.VISIBLE

        val email = evEmail.text.toString()
        val password = evPassword.text.toString()

        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {

                    sharedPreferenceClass.setValueString("uid", it.user?.uid)

                    startActivity(Intent(this, MainActivity::class.java))
                    progressBar.visibility = View.GONE
                }.addOnFailureListener {
                    evPassword.error = "Wrong Password"
                    progressBar.visibility = View.GONE
                }
            } else {
                evPassword.error = "Enter password"
                progressBar.visibility = View.GONE
            }
        } else {
            evEmail.error = "Enter email address"
            progressBar.visibility = View.GONE
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