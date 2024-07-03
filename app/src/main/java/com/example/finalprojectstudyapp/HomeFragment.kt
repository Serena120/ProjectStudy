package com.example.finalprojectstudyapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val AssignmentTextView: TextView = view.findViewById(R.id.assignmentTextView)
        val CalendarTextView: TextView = view.findViewById(R.id.calendarTextView)
        val PomodoroTextView: TextView = view.findViewById(R.id.pomodoroTextView)
        val ToDoTextView: TextView = view.findViewById(R.id.todoTextView)
        val NotesTextView: TextView = view.findViewById(R.id.notesTextView)

        AssignmentTextView.setOnClickListener{
            val intent = Intent(activity, AssignmentActivity::class.java)
            intent.putExtra("key","value")
            startActivity(intent)
        }

        PomodoroTextView.setOnClickListener{
            val intent = Intent(activity, PomodoroActivity::class.java)
            intent.putExtra("key","value")
            startActivity(intent)
        }

        ToDoTextView.setOnClickListener{
            /*val intent = Intent(activity, PomodoroActivity::class.java)
            intent.putExtra("key","value")
            startActivity(intent)*/
            val toDoFragment = ToDoFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, toDoFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        CalendarTextView.setOnClickListener{
            val intent = Intent(activity, CalendarActivity::class.java)
            intent.putExtra("key","value")
            startActivity(intent)
        }

        NotesTextView.setOnClickListener{
            val intent = Intent(activity, NotesActivity::class.java)
            intent.putExtra("key","value")
            startActivity(intent)
        }
        return view
    }

}