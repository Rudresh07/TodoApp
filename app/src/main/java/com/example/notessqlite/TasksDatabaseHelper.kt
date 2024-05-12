package com.example.notessqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TasksDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "todoapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME_TASKES = "alltasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_COMPLETED = "completed"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createNotesTableQuery =
            "CREATE TABLE $TABLE_NAME_TASKES($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_CONTENT TEXT, $COLUMN_COMPLETED INTEGER)"
        db?.execSQL(createNotesTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropNotesTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME_TASKES"
        db?.execSQL(dropNotesTableQuery)
        onCreate(db)
    }

    fun insertTask(note: Tasks) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CONTENT, note.Content)
            put(COLUMN_COMPLETED, 0) // Convert boolean to integer
        }
        val noteID = db.insert(TABLE_NAME_TASKES, null, values)
        db.close()
    }

    fun updateTask(note: Tasks) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CONTENT, note.Content)
            put(COLUMN_COMPLETED, 0) // Convert boolean to integer
        }
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME_TASKES, values, whereClause, whereArgs)
        db.close()
    }

    fun getAllTasks(): List<Tasks> {
        val notesList = mutableListOf<Tasks>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_TASKES"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1 // Convert integer to boolean
            val note = Tasks(id, content, completed)
            notesList.add(note)
        }

        cursor.close()
        db.close()
        return notesList
    }

    fun getTaskByID(noteID: Int): Tasks {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_TASKES WHERE $COLUMN_ID=$noteID"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1 // Convert integer to boolean

        cursor.close()
        db.close()
        return Tasks(id,  content, completed)
    }

    fun deleteTask(noteID: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(noteID.toString())
        db.delete(TABLE_NAME_TASKES, whereClause, whereArgs)
        db.close()
    }
    fun StatusUpdate(noteID: Int, completed: Boolean) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COMPLETED, if (completed) 1 else 0) // Convert boolean to integer
        }
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(noteID.toString())
        db.update(TABLE_NAME_TASKES, values, whereClause, whereArgs)
        db.close()
    }

    fun getStatus(noteID: Int): Boolean {
        val db = readableDatabase
        val query = "SELECT $COLUMN_COMPLETED FROM $TABLE_NAME_TASKES WHERE $COLUMN_ID = ?"
        val selectionArgs = arrayOf(noteID.toString())
        val cursor = db.rawQuery(query, selectionArgs)

        var completed = false
        if (cursor != null && cursor.moveToFirst()) {
            completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
            cursor.close()
        }

        db.close()
        return completed
    }


}
