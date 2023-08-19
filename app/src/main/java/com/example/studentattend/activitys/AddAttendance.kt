package com.example.studentattend.activitys

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentattend.Adapter.StuAdapterAttend
import com.example.studentattend.R
import com.example.studentattend.databinding.ActivityAddAttendanceBinding
import com.example.studentattend.stumodel.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class AddAttendance : AppCompatActivity() {

    private lateinit var openbtn: TextView
    private lateinit var calender: Calendar
    private lateinit var stuRecyclerView: RecyclerView
    private lateinit var binding: ActivityAddAttendanceBinding
    private lateinit var stuList: ArrayList<StudentModel>
    private lateinit var dbRef: DatabaseReference


    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDate()
        setDataRecyclerView()
        getStudentData()

        binding.SavedataBTM.setOnClickListener {

            val dialog = Dialog(this@AddAttendance)
            dialog.setContentView(R.layout.loadingdialogbox)
            dialog.show()
            Toast.makeText(applicationContext,"Save Data", Toast.LENGTH_LONG).show()
        }
    }


    //Show data using recyclerview
    private fun setDataRecyclerView() {
        stuRecyclerView = findViewById(R.id.stuRecyclerView)
        binding.stuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.stuRecyclerView.setHasFixedSize(true)
        stuList = arrayListOf<StudentModel>()
    }

    // set date in button
    private fun setDate() {
        openbtn = findViewById(R.id.datepickBtn)
        calender = Calendar.getInstance()
        openbtn.setOnClickListener {
            val datePicker = DatePickerDialog(
                this, { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    openbtn.text = selectedDate
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)

            )
            datePicker.show()
        }
    }

    // get student data  in firebase and set recycler view also, send data studnet detail class using put extras
    private fun getStudentData() {
        stuRecyclerView.visibility = View.GONE
        binding.tvLoadingData.visibility = View.VISIBLE

        // fetch the data from Firebase and set up RecyclerView
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = firebaseDatabase.getReference("Users/$uid/Student")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stuList = mutableListOf<StudentModel>()

                if (snapshot.exists()) {
                    for (stuSnap in snapshot.children) {
                        val stuData = stuSnap.getValue(StudentModel::class.java)
                        stuData?.let { stuList.add(it) }
                    }

                    val mAdapter = StuAdapterAttend(stuList)
                    stuRecyclerView.layoutManager =
                        LinearLayoutManager(this@AddAttendance) // Replace with your activity class name
                    stuRecyclerView.adapter = mAdapter

                    stuRecyclerView.visibility = View.VISIBLE
                    binding.tvLoadingData.visibility = View.GONE
                } else {
                    // Handle the case where no data exists
                    // You can show a message or perform other actions here
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the onCancelled event
            }
        })
    }
}