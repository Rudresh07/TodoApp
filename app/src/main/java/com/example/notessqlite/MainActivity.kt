package com.example.notessqlite

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notessqlite.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: TasksDatabaseHelper
    private lateinit var tasksAdapter: TasksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = TasksDatabaseHelper(this)
        tasksAdapter = TasksAdapter(db.getAllTasks(), this)





        // Setting layout for recyclerView with 2 columns
        val spanCount = 1
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing) // 8dp spacing

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.TasksRecyclerView.layoutManager = layoutManager

        val itemDecoration = GridSpacingItemDecoration(spanCount, spacing)
        binding.TasksRecyclerView.addItemDecoration(itemDecoration)

        binding.TasksRecyclerView.adapter = tasksAdapter

        binding.AddNewTask.setOnClickListener {
            val intent = Intent(this, AddTasks::class.java)
            startActivity(intent)
        }


    }

    // Refresh data method
    override fun onResume() {
        super.onResume()
        tasksAdapter.refreashData(db.getAllTasks())
    }
}