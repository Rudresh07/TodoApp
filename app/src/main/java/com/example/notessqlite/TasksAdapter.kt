package com.example.notessqlite

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(private var tasks: List<Tasks>, private val context: Context) :
    RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private val db: TasksDatabaseHelper = TasksDatabaseHelper(context)

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ContentText: TextView = itemView.findViewById(R.id.NotesContent)
        val UpdateBtn: ImageView = itemView.findViewById(R.id.EditNote)
        val DeleteBtn: ImageView = itemView.findViewById(R.id.DeleteNote)
        val CompleteBtn: CheckBox = itemView.findViewById(R.id.checkbox)
        val Linearlayout: View = itemView.findViewById(R.id.MainLinearlayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.ContentText.text = task.Content

        holder.UpdateBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateTask::class.java).apply {
                putExtra("note_id", task.id)
            }

            holder.itemView.context.startActivity(intent)
        }

        holder.CompleteBtn.isChecked = task.completed


        holder.CompleteBtn.setOnCheckedChangeListener { _, isChecked ->
            task.completed = isChecked
            db.StatusUpdate(task.id, isChecked)

            // Update the background color of the item view
            updateBackgroundColor(holder, isChecked)
        }
        val status = db.getStatus(task.id)

        // Use ContextCompat to resolve color resources
        val backgroundColor = if (status) {
            ContextCompat.getColor(holder.itemView.context, R.color.red)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.green)
        }
//        holder.itemView.setBackgroundColor(backgroundColor)
        holder.Linearlayout.setBackgroundColor(backgroundColor)

        holder.DeleteBtn.setOnClickListener {
            AlertDeleteDialogBox(task.id)
        }
    }
    private fun updateBackgroundColor(holder: TaskViewHolder, isChecked: Boolean) {
        val backgroundColor = ContextCompat.getColor(
            holder.itemView.context,
            if (isChecked) R.color.red else R.color.green
        )
        holder.Linearlayout.setBackgroundColor(backgroundColor)
    }

    fun AlertDeleteDialogBox(NoteID: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)


        alertDialogBuilder.setTitle("Delete Task")
        alertDialogBuilder.setMessage("Are you sure you want to delete this task?")


        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->

            db.deleteTask(NoteID)
            refreashData(db.getAllTasks())
            Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
        }


        alertDialogBuilder.setNegativeButton("No") { dialog, which ->

            dialog.dismiss()
        }


        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun refreashData(newTasks: List<Tasks>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}

