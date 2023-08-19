package com.example.studentattend.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.studentattend.R
import com.example.studentattend.databinding.ActivityStudentDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class StudentDetails : AppCompatActivity() {


    private lateinit var binding: ActivityStudentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValuesToViews()

        //sent data  in update activity
        binding.btnUpdate.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("stuKey",binding.tvStuId.text.toString())
            bundle.putString("stuName",binding.tvStuName.text.toString())
            bundle.putString("stuDepartment",binding.tvStuDepartment.text.toString())
            bundle.putString("stuBirthday",binding.tvStuDoB.text.toString())
            bundle.putString("stuPhoneNo",binding.tvStuPhoneNo.text.toString())

            val intent = Intent(this,Update_Data_dialog::class.java)
            intent.putExtras(bundle)
            startActivity(intent)


        }

        binding.btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("stuKey").toString()
            )
        }
    }

    private fun deleteRecord(id: String) {

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        var dbRef =  firebaseDatabase.getReference("Users/$uid/Student/$id")

        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Student data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, SearchStudent::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    //set data in text view
    private fun setValuesToViews() {
        binding.tvStuId.text = intent.getStringExtra("stuKey")
        binding.tvStuName.text = intent.getStringExtra("stuName")
        binding.tvStuDepartment.text = intent.getStringExtra("stuDepartment")
        binding.tvStuDoB.text = intent.getStringExtra("stuBirthday")
        binding.tvStuPhoneNo.text = intent.getStringExtra("stuPhoneNo")
    }

}

