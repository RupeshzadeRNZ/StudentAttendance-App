package com.example.studentattend.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.studentattend.R
import com.example.studentattend.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LogIn : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)

        binding.createAcc.setOnClickListener {

            val intent = Intent(this, CreateAccount::class.java)
            // start your next activity
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty()) {
                binding.editTextEmail.error = "Please enter email"
            } else if (password.isEmpty()) {
                binding.editTextPassword.error = "Please enter password"
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login successful
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                            val bundle = Bundle()
                            bundle.putString("email",binding.editTextEmail.text.toString())
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)

                        } else {
                            // Login failed
                            val exception = task.exception
                            if (exception is FirebaseAuthInvalidUserException) {
                                Toast.makeText(this, "Email is not registered.", Toast.LENGTH_SHORT).show()
                            } else if (exception is FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Please Create Account.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }

    }
}