package com.example.tpjoansmithleolemercier.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpjoansmithleolemercier.R
import com.example.tpjoansmithleolemercier.network.Api
import com.example.tpjoansmithleolemercier.task.TaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private val tasksRepository = TasksRepository()
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

        tasksRepository.taskList.observe(viewLifecycleOwner) { list ->
            taskList.clear()
            taskList.addAll(list)//modifier la liste et charger les nouvelles valeurs
            adapter.notifyDataSetChanged()
        }

        recyclerView.adapter = adapter

        adapter.onEditTask = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, task)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        adapter.onDeleteTask = { task ->
//            taskList.remove(task)
            lifecycleScope.launch {
                tasksRepository.deleteTask(task)
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data?.getSerializableExtra(TaskActivity.TASK_KEY) as? Task ?: return
            val indexTask = taskList.indexOfFirst{it.id == task.id}
            if (indexTask < 0) {
                lifecycleScope.launch {
                    tasksRepository.createTask(task)
                }
//                taskList.add(task)
            }
            else {
                lifecycleScope.launch {
                    tasksRepository.updateTask(task)
                }
//                taskList[indexTask] = task
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        val textView = view?.findViewById<TextView>(R.id.userInfo)
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            textView?.text = "${userInfo.firstName} ${userInfo.lastName}"
            tasksRepository.refresh()
        }
    }
}


