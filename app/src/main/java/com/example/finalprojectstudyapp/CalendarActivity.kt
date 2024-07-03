package com.example.finalprojectstudyapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalprojectstudyapp.databinding.ActivityCalendarBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private val calendarEvents = mutableListOf<CalendarEvent>()
    private var calendarAdapter: CalendarAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        loadCalendarEventsFromPreferences()

        calendarAdapter = CalendarAdapter(calendarEvents)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = calendarAdapter

        binding.addEventBtn.setOnClickListener {
            toggleAddEventCardVisibility()
        }

        binding.submitButton.setOnClickListener {
            createCalendarEvent()
        }

        binding.cancelButton.setOnClickListener {
            toggleAddEventCardVisibility()
        }

        createNotificationChannel()
        checkAndRemoveExpiredEvents()
    }

    private fun toggleAddEventCardVisibility() {
        if (binding.addEventCard.visibility == android.view.View.VISIBLE) {
            binding.addEventCard.visibility = android.view.View.GONE
        } else {
            binding.addEventCard.visibility = android.view.View.VISIBLE
        }
    }

    private fun createCalendarEvent() {
        val title = binding.titleET.text.toString()

        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)

        val minNotificationTime = System.currentTimeMillis() + 1 * 60 * 1000

        if (calendar.timeInMillis < minNotificationTime) {
            Toast.makeText(this, "Please choose a time at least 1 minute from now", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.isNotEmpty()) {
            val calendarEvent = CalendarEvent(title, calendar.timeInMillis.toString())
            calendarEvents.add(calendarEvent)
            calendarAdapter?.notifyItemInserted(calendarEvents.size - 1)
            calendarAdapter?.notifyDataSetChanged()

            scheduleNotification(calendarEvent, calendar.timeInMillis)
            saveCalendarEventsToPreferences()

            binding.titleET.text?.clear()
        } else {
            Toast.makeText(this, "Please enter a title for your event", Toast.LENGTH_SHORT).show()
        }

        binding.addEventCard.visibility = android.view.View.GONE
    }

    private fun scheduleNotification(calendarEvent: CalendarEvent, notificationTimeMillis: Long) {
        val intent = Intent(applicationContext, CalendarNotification::class.java)
        intent.putExtra(eventExtra, calendarEvent.title)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            calendarNotificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC,
            notificationTimeMillis,
            pendingIntent
        )
        showAlert(notificationTimeMillis, calendarEvent.title)
    }

    private fun showAlert(time: Long, title: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = SimpleDateFormat("HH:mm")

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title\nAt: ${dateFormat.format(date)}:${timeFormat.format(date)}"
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(calendarChannelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun saveCalendarEventsToPreferences() {
        val editor = sharedPreferences.edit()
        val calendarEventsJson = Gson().toJson(calendarEvents)
        editor.putString("calendarEvents", calendarEventsJson)
        editor.apply()
    }

    private fun loadCalendarEventsFromPreferences() {
        val calendarEventsJson = sharedPreferences.getString("calendarEvents", null)
        if (calendarEventsJson != null) {
            val type = object : TypeToken<MutableList<CalendarEvent>>() {}.type
            calendarEvents.addAll(Gson().fromJson(calendarEventsJson, type))
            calendarAdapter?.notifyDataSetChanged()
        }
    }

    private fun checkAndRemoveExpiredEvents() {
        val currentTime = System.currentTimeMillis()
        val iterator = calendarEvents.iterator()

        while (iterator.hasNext()) {
            val calendarEvent = iterator.next()
            val eventTime = calendarEvent.timestamp.toLong()

            if (currentTime > eventTime.toLong()) {
                iterator.remove()
            }
        }

        calendarAdapter?.notifyDataSetChanged()
    }
}