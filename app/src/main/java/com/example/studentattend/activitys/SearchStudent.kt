package com.example.studentattend.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentattend.Adapter.StuAdapter
import com.example.studentattend.R
import com.example.studentattend.databinding.ActivitySearchStudentBinding
import com.example.studentattend.stumodel.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchStudent : AppCompatActivity() {

    private lateinit var stuRecyclerView: RecyclerView
    private lateinit var binding: ActivitySearchStudentBinding
    private lateinit var stuList: ArrayList<StudentModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stuRecyclerView = findViewById(R.id.stuRecyclerView)
        binding.stuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.stuRecyclerView.setHasFixedSize(true)

        stuList = arrayListOf<StudentModel>()
        getStudentData()
    }

    // get student data  in firebase and set recycler view also
    // send data studnet detail class using put extras
    private fun getStudentData() {

        stuRecyclerView.visibility = View.GONE
        binding.tvLoadingData.visibility = View.VISIBLE

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef =  firebaseDatabase.getReference("Users/$uid/Student")


        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                stuList.clear()
                if (snapshot.exists()) {
                    for (stuSnap in snapshot.children) {
                        val stuData = stuSnap.getValue(StudentModel::class.java)
                        stuList.add(stuData!!)
                    }
                    val mAdapter = StuAdapter(stuList)
                    stuRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : StuAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@SearchStudent, StudentDetails::class.java)

                            //put extras
                            intent.putExtra("stuKey", stuList[position].stuKey)
                            intent.putExtra("stuName", stuList[position].stuName)
                            intent.putExtra("stuDepartment", stuList[position].stuDepartment)
                            intent.putExtra("stuBirthday", stuList[position].stuBirthday)
                            intent.putExtra("stuPhoneNo", stuList[position].stuPhoneNo)
                            startActivity(intent)
                        }

                    })

                    stuRecyclerView.visibility = View.VISIBLE
                    binding.tvLoadingData.visibility = View.GONE
//                }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}