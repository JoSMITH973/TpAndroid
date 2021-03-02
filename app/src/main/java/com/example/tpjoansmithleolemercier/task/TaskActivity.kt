package com.example.tpjoansmithleolemercier.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.tpjoansmithleolemercier.R
import com.example.tpjoansmithleolemercier.tasklist.Task
import com.example.tpjoansmithleolemercier.tasklist.TaskListAdapter
import com.example.tpjoansmithleolemercier.tasklist.TaskListFragment
import java.util.*

class TaskActivity : AppCompatActivity() {
    companion object {
        const val TASK_KEY = "TASK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val buttonConfirm = findViewById<Button>(R.id.editConfirm)
        val editTitle = findViewById<EditText>(R.id.editTitle)
        val editDescription = findViewById<EditText>(R.id.editDescription)

        buttonConfirm.setOnClickListener{
            val taskAdd = Task(id = UUID.randomUUID().toString(), title = editTitle.text.toString(), description = editDescription.text.toString())
            intent.putExtra(TASK_KEY, taskAdd)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}