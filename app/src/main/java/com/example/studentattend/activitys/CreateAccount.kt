package com.example.studentattend.activitys

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.studentattend.R
import com.example.studentattend.databinding.ActivityCreateAccountBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.Base64

class CreateAccount : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        auth = Firebase.auth
        fun showErrorMessage(m: String) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("Enter the Correct Information")
            builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.show()
        }
        binding.createbutton.setOnClickListener {
            // plasea wite dialog box
            val dialog = Dialog(this@CreateAccount)
            dialog.setContentView(R.layout.loadingdialogbox)
            dialog.show()

            val email = binding.Email.text.toString()
            val password = binding.Pass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                // Proceed with authentication or registration process
                @SuppressLint("SuspiciousIndentation")
                fun handleSignUp() {
                    val email = binding.Email.text.toString()// Get the entered email from your UI component, e.g., emailEditText.text.toString()

                    // Check if the email is already registered in Firebase
                    auth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val signInMethods = task.result?.signInMethods
                                if (signInMethods != null && signInMethods.isNotEmpty()) {
                                    // Email is already registered
                                    dialog.hide()
                                    showErrorMessage("This email is already registered.")
                                } else {
                                    // Proceed with the signup process
                                    // e.g., auth.createUserWithEmailAndPassword(email, password)
                                    auth.createUserWithEmailAndPassword(
                                        binding.Email.text.toString(),
                                        binding.Pass.text.toString()
                                    )
                                }
                            } else {
                                // Handle the error
                                dialog.hide()
                                showErrorMessage("An error occurred while checking the email. Please try again later.")
                            }
                        }
                }

                // Other necessary functions and UI setup methods
                auth.createUserWithEmailAndPassword(
                    binding.Email.text.toString(),
                    binding.Pass.text.toString()
                ).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            createstudemail()
//                       updateUI(user)
                            dialog.hide()
                            Toast.makeText(this,"successfully create account".toString(),Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LogIn::class.java)
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
//                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            dialog.hide()
                            Toast.makeText(
                                baseContext, "Check Your Internet ", Toast.LENGTH_SHORT,
                            ).show()
//                        updateUI(null)
                        }
                    }
            } else {
                // Show an error message or take appropriate action
                dialog.hide()
                showErrorMessage("Email and password cannot be empty.")
            }
        }
        binding.btnlogin.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            // start your next activity
            startActivity(intent)
        }

        //google butto
        binding.google.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }
    }

    private fun createstudemail() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val userEmail = currentUser.email

            // Create a user node in the database using the UID as the key
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

            // Store user's email in the user node
            userRef.child("email").setValue(userEmail)

            // Continue with your activity or UI logic here
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "DONE   ", Toast.LENGTH_LONG).show()


                        } else {
                            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {

                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()

            }
        }



    }


