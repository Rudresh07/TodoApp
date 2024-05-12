package com.example.notessqlite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.databinding.ActivityAddTasksBinding

class AddTasks : AppCompatActivity() {

    private lateinit var binding:ActivityAddTasksBinding
    private lateinit var db:TasksDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTasksBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = TasksDatabaseHelper(this)


        binding.submit.setOnClickListener{
            var content = binding.NotesContent.text.toString()
            var completed = false
            val task= Tasks(0,content,completed)
            db.insertTask(task)
            finish()
            Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()
        }

        }
    }
