package com.example.tpjoansmithleolemercier.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tpjoansmithleolemercier.network.Api

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
        }
    }

    suspend fun createTask(task: Task): Task? {
        val createResponse = tasksWebService.createTask(task)
//        val createdTask = createResponse.body()!!
//        val editableList = _taskList.value.orEmpty().toMutableList()
//        editableList.add(createdTask)
//        _taskList.value = editableList
        return if(createResponse.isSuccessful) createResponse.body() else null
    }

    suspend fun updateTask(task: Task): Task? {
        val updateResponse = tasksWebService.updateTask(task)
//        val updatedTask = updateResponse.body()!!
//        val editableList = _taskList.value.orEmpty().toMutableList()
//        val position = editableList.indexOfFirst { updatedTask.id == it.id }
//        editableList[position] = updatedTask
//        _taskList.value = editableList

        return if(updateResponse.isSuccessful) updateResponse.body() else null
    }

    suspend fun removeTask(task: Task): Unit? {
        val deleteResponse = tasksWebService.deleteTask(task.id)
//        val editableList = _taskList.value.orEmpty().toMutableList()
//        editableList.remove(task)
//        _taskList.value = editableList
        return if(deleteResponse.isSuccessful) deleteResponse.body() else null
    }
}