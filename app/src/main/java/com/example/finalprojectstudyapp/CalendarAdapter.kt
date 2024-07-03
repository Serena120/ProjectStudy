package com.example.finalprojectstudyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarAdapter (private val calendarEvents: MutableList<CalendarEvent>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTimeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_event, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calendarEvent = calendarEvents[position]
        holder.titleTextView.text = calendarEvent.title

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(calendarEvent.timestamp.toLong())
        holder.dateTimeTextView.text = dateFormat.format(date)
    }

    override fun getItemCount(): Int {
        return calendarEvents.size
    }
}