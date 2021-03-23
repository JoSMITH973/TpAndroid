package com.example.tpjoansmithleolemercier.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpjoansmithleolemercier.R
import com.example.tpjoansmithleolemercier.task.TaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListFragment : Fragment() {
    private val taskList = mutableListOf(
            Task(id = "id_1", title = "Task 1", description = "description 1"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3")
    )
    private val adapter: TaskListAdapter = TaskListAdapter(taskList)

    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)


        val openAddTask = view.findViewById<FloatingActionButton>(R.id.openAddTask)
        openAddTask.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        recyclerView.adapter = adapter

        adapter.onEditTask = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, task)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        adapter.onDeleteTask = { task ->
            taskList.remove(task)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data?.getSerializableExtra(TaskActivity.TASK_KEY) as? Task ?: return
            val indexTask = taskList.indexOfFirst{it.id == task.id}
            if (indexTask < 0) {
                taskList.add(task)
            }
            else {
                taskList[indexTask] = task
            }
        }
        adapter.notifyDataSetChanged()
    }
}


