package com.example.notessqlite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notessqlite.databinding.ActivityUpdateTaskBinding

class UpdateTask : AppCompatActivity() {

    private lateinit var binding:ActivityUpdateTaskBinding
    private lateinit var db :TasksDatabaseHelper
    private var noteID:Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        noteID = intent.getIntExtra("note_id", -1)
        if (noteID==-1){
            finish()
            return
        }


        val note = db.getTaskByID(noteID)
        binding.EditNotesContent.setText(note.Content)

        binding.EditNotesubmit.setOnClickListener {
            var newContent = binding.EditNotesContent.text.toString()
            var complete = false
            val updateTask= Tasks(noteID,newContent,complete)
            db.updateTask(updateTask)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()

        }

    }
}