package com.example.finalprojectstudyapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.finalprojectstudyapp.databinding.ActivityPomodoroBinding

class PomodoroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPomodoroBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var remainingTime: Long = 1500000
    private var isWorkInterval = true
    private var workDuration: Long = 1500000
    private var breakDuration: Long = 300000
    private var totalWorkDuration: Long = 0

    private var sessionCount = 0
    private var sessionCountKey = "sessionCount"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPomodoroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        sessionCount = sharedPreferences.getInt(sessionCountKey, 0)
        //totalWorkDuration = 0
        loadAndDisplayTotalWorkDuration()
        initializedUI()
    }

    private fun initializedUI() {

        val timerText = binding.timerText
        val startBtn = binding.startBtn
        val resetBtn = binding.resetBtn
        val breakText = binding.breakText
        val focusText = binding.focusText
        val totalText = binding.totalText

        setClickListeners()
        binding.sessionCountText.text = getString(R.string.session_count, sessionCount )
        totalText.text = getString(R.string.total_time_format, formatTime(totalWorkDuration))
    }

    private fun setClickListeners(){
        binding.startBtn.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        binding.resetBtn.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        val duration = if (isWorkInterval) workDuration else breakDuration

        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                binding.timerText.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                if (!isWorkInterval) {
                    isRunning = false
                    binding.startBtn.visibility = View.GONE
                    binding.resetBtn.visibility = View.VISIBLE
                    binding.breakText.visibility = View.GONE
                    binding.focusText.visibility = View.GONE

                    totalWorkDuration += workDuration
                    saveTotalWorkDuration(totalWorkDuration)
                    sessionCount++

                    with(sharedPreferences.edit()){
                        putInt(sessionCountKey, sessionCount)
                        apply()
                    }

                    binding.totalText.text = getString(R.string.total_time_format, formatTime(totalWorkDuration))
                    binding.sessionCountText.text = getString(R.string.session_count, sessionCount)
                    //binding.sessionCountText.text = sessionCount.toString()
                } else {//break
                    isWorkInterval = !isWorkInterval
                    remainingTime = breakDuration
                    startTimer()
                    binding.breakText.visibility = View.VISIBLE
                    binding.focusText.visibility = View.GONE
                }
            }
        }.start()
        isRunning = true
        binding.focusText.visibility = View.VISIBLE

        binding.startBtn.text = "Pause"
        binding.resetBtn.visibility = View.GONE
    }

    private fun pauseTimer() {
        timer?.cancel()
        isRunning = false
        binding.startBtn.text = "Resume"
        binding.resetBtn.visibility = View.VISIBLE
    }

    private fun resetTimer() {
        timer?.cancel()
        remainingTime = workDuration
        isWorkInterval = true
        binding.timerText.text = formatTime(remainingTime)
        binding.startBtn.text = "Start"
        binding.startBtn.visibility = View.VISIBLE
        binding.resetBtn.visibility = View.GONE
    }

    private fun formatTime(milliseconds: Long): String {
        val minutes = milliseconds / 60000
        val seconds = (milliseconds % 60000) / 1000
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun saveTotalWorkDuration(totalWorkDuration: Long){
        with(sharedPreferences.edit()){
            putLong("totalWorkDuration", totalWorkDuration)
            apply()
        }
    }

    private fun loadAndDisplayTotalWorkDuration(){
        totalWorkDuration = sharedPreferences.getLong("totalWorkDuration", 0)
    }

}