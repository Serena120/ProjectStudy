package com.example.finalprojectstudyapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalprojectstudyapp.databinding.ActivityAssignmentBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AssignmentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAssignmentBinding
    private val assignments = mutableListOf<Assignment>()
    private var assignmentAdapter : AssignmentAdapter? =null
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        loadAssignmentsFromPreferences()

        assignmentAdapter = AssignmentAdapter(assignments)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = assignmentAdapter

        binding.submitButton.setOnClickListener {
            createAssignment()
        }
        createNotificationChannel()

        checkAndRemoveExpiredAssignments()
    }

    private fun createAssignment() {
        val title = binding.titleET.text.toString()
        val message = binding.messageET.text.toString()

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

        if (title.isNotEmpty() && message.isNotEmpty()) {
            val assignment = Assignment(title, message, calendar.timeInMillis.toString())
            assignments.add(assignment)
            assignmentAdapter?.notifyItemInserted(assignments.size - 1)
            assignmentAdapter?.notifyDataSetChanged()

            scheduleNotification(assignment, calendar.timeInMillis)

            saveAssignmentsToPreferences()

            binding.titleET.text?.clear()
            binding.messageET.text?.clear()
        } else {
            Toast.makeText(this, "Please enter title and description for your message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scheduleNotification(assignment: Assignment, notificationTimeMillis: Long) {
        val intent = Intent(applicationContext, Notification::class.java)
        intent.putExtra(titleExtra, assignment.title)
        intent.putExtra(messageExtra, assignment.message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC,
            notificationTimeMillis,
            pendingIntent
        )
        showAlert(notificationTimeMillis, assignment.title, assignment.message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = SimpleDateFormat("HH:mm")

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + ":" + timeFormat.format(date)
            )
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel()
    {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun saveAssignmentsToPreferences() {
        val editor = sharedPreferences.edit()
        val assignmentsJson = Gson().toJson(assignments)
        editor.putString("assignments", assignmentsJson)
        editor.apply()
    }

    private fun loadAssignmentsFromPreferences() {
        val assignmentsJson = sharedPreferences.getString("assignments", null)
        if (assignmentsJson != null) {
            val type = object : TypeToken<MutableList<Assignment>>() {}.type
            assignments.addAll(Gson().fromJson(assignmentsJson, type))
            assignmentAdapter?.notifyDataSetChanged()
        }
    }

    private fun checkAndRemoveExpiredAssignments() {
        val currentTime = System.currentTimeMillis()
        val iterator = assignments.iterator()

        while (iterator.hasNext()) {
            val assignment = iterator.next()
            val deadline = assignment.timestamp.toLong()

            if (currentTime > deadline) {
                iterator.remove()
            }
        }

        assignmentAdapter?.notifyDataSetChanged()
    }
}