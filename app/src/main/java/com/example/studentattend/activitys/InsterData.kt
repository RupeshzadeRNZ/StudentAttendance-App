package com.example.studentattend.activitys

import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.studentattend.R
import com.example.studentattend.databinding.ActivityInsterDataBinding
import com.example.studentattend.stumodel.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class InsterData : AppCompatActivity() {

    private lateinit var binding: ActivityInsterDataBinding
    private lateinit var calender: Calendar
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsterDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef =  firebaseDatabase.getReference("Users/$uid/Student")

        //calender logic
        calender = Calendar.getInstance()
        binding.openbtndate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this, { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    binding.inBirthday.setText(selectedDate)

                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)

            )
            datePicker.show()
        }

        binding.saveBtn.setOnClickListener {

            saveStudentData()
        }
    }
    private fun saveStudentData() {
        // Getting values
        val stuName = binding.inName.text.toString()
        val stuDepartment = binding.inDepartment.text.toString()
        val stuId = binding.inId.text.toString()
        val stuBirthday = binding.inBirthday.text.toString()
        val stuPhoneNo = binding.inMobail.text.toString()

        if (stuName.isEmpty()) {
            binding.inName.error = "Please enter Name"
        } else if (stuDepartment.isEmpty()) {
            binding.inDepartment.error = "Please enter Department"
        } else if (stuId.isEmpty()) {
            binding.inId.error = "Please enter Id No"
        } else if (stuBirthday.isEmpty()) {
            binding.inBirthday.error = "Please enter Birthday"
        } else if (stuPhoneNo.isEmpty()) {
            binding.inMobail.error = "Please enter Phone.No"
        } else {
            // Check if student with the same stuId exists
            dbRef.child(stuId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        binding.inId.error = "Student with this ID already exists"
                    } else {

                        val dialog = Dialog(this@InsterData)
                        dialog.setContentView(R.layout.loadingdialogbox)

                        val student = StudentModel(stuId, stuName, stuDepartment, stuBirthday, stuPhoneNo)

                        dialog.show()

                        dbRef.child(stuId).setValue(student)
                            .addOnCompleteListener {

                                binding.inName.text?.clear()
                                binding.inDepartment.text?.clear()
                                binding.inId.text?.clear()
                                binding.inBirthday.text?.clear()
                                binding.inMobail.text?.clear()
                                dialog.hide()
                            }
                            .addOnFailureListener { err ->
                                Toast.makeText(this@InsterData, "Error ${err.message}", Toast.LENGTH_LONG).show()
                            }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@InsterData, "Error ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }  }
