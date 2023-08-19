package com.example.studentattend.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentattend.R
import com.example.studentattend.stumodel.StudentModel


class StuAdapterAttend(private val stuList: MutableList<StudentModel>) :
    RecyclerView.Adapter<StuAdapterAttend.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stu_list_item_attend, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStu = stuList[position]
        holder.tvStuName.text = currentStu.stuName

    }

    override fun getItemCount(): Int {
        return stuList.size
    }

    class ViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView) {

        val tvStuName: TextView = itemView.findViewById(R.id.tvStuName)

    }

}