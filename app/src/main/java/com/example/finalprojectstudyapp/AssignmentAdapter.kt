package com.example.finalprojectstudyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssignmentAdapter(private val assignments: MutableList<Assignment>) :
    RecyclerView.Adapter<AssignmentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTimeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_assignment, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val assignment = assignments[position]
        holder.titleTextView.text = assignment.title
        holder.messageTextView.text = assignment.message

        //holder.dateTimeTextView.text = assignment.dateTime
        val dateFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.getDefault())
        val date = Date(assignment.timestamp.toLong())
        holder.dateTimeTextView.text = dateFormat.format(date)
    }

    override fun getItemCount(): Int {
        return assignments.size
    }


}