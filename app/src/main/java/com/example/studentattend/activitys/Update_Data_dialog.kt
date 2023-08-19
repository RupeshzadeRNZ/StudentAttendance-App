package com.example.studentattend.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.studentattend.databinding.ActivityUpdateDataDialogBinding
import com.example.studentattend.stumodel.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Update_Data_dialog : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateDataDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //get data in studentDetails activity and set data in edit text view
        val bundle = intent.extras
        if (bundle != null){
            //get data
            val receivedBundle = intent.extras
            val stuKey  = receivedBundle?.getString("stuKey")
            val stuName  = receivedBundle?.getString("stuName")
            val stuDepartment = receivedBundle?.getString("stuDepartment")
            val stuBirthday = receivedBundle?.getString("stuBirthday")
            val stuPhoneNo = receivedBundle?.getString("stuPhoneNo")

            //set data
            binding.etStuName.setText(stuName)
            binding.etStuDepartment.setText(stuDepartment)
            binding.etStuDoB.setText(stuBirthday)
            binding.etStuPhone.setText(stuPhoneNo)

            binding.tvTitle.text = ("Updating $stuName Record")

            // Update data button
            binding.btnUpdateData.setOnClickListener {
                updateStuData(stuKey.toString())
                Toast.makeText(applicationContext,"Update Data", Toast.LENGTH_LONG).show()
            }

        }
    }
    //data update in firebase database
    private fun updateStuData(stuKey: String) {
        var name = binding.etStuName.text.toString()
        var depart = binding.etStuDepartment.text.toString()
        var dof = binding.etStuDoB.text.toString()
        var phone = binding.etStuPhone.text.toString()

        if (name.isEmpty()) {
            binding.etStuName.error = "Please enter Name"
        } else if (depart.isEmpty()) {
            binding.etStuDepartment.error = "Please enter Department"
        } else if (dof.isEmpty()) {
            binding.etStuDoB.error = "Please enter Id No"
        } else if (phone.isEmpty()) {
            binding.etStuPhone.error = "Please enter Birthday"
        }  else {

            val firebaseDatabase = FirebaseDatabase.getInstance()
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            var dbRef =  firebaseDatabase.getReference("Users/$uid/Student/$stuKey")

            val stuInfo = StudentModel(stuKey,name,depart,dof,phone)
            dbRef.setValue(stuInfo)
        }

    }
}