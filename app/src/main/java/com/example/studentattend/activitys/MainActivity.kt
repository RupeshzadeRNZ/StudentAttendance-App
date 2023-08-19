package com.example.studentattend.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.studentattend.R
import com.example.studentattend.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertData : CardView
    private lateinit var btnFetchData  : CardView
    private lateinit var btnattendance  : CardView
    private lateinit var binding : ActivityMainBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        var emaildata: String? = null
        if (bundle != null){
              emaildata = "${bundle.getString("email")}" }
        val sharedPref = getSharedPreferences("my_shared_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("email", emaildata)
        editor.apply()

        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        btnInsertData = findViewById<CardView>(R.id.btnInsertData)
        btnFetchData = findViewById<CardView>(R.id.btnFetchData)
        btnattendance = findViewById<CardView>(R.id.btnattendance)

        btnInsertData.setOnClickListener {
            var intent = Intent(this, InsterData::class.java)
            startActivity(intent)
        }
        btnFetchData.setOnClickListener {
            var intent = Intent(this, SearchStudent::class.java)
            startActivity(intent)
        }
        btnattendance.setOnClickListener {
            var intent = Intent(this, AddAttendance::class.java)
            startActivity(intent)
        }


    }
}