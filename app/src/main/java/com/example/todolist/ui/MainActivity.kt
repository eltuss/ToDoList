package com.example.todolist.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.model.Task
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Flow

@RequiresApi(Build.VERSION_CODES.P)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //chama o metodo para inserir itens no app
        insertListeners()

        popular() //mock

        //chama o metodo de atualizar a lista
        binding.rvTasks.adapter = adapter
        updateList()
        loadData()


    }



    private fun insertListeners() {
        //Botão da tela inicial para criar uma nova tarefa
        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)

        }
        //Botão do menu para editar a tarefa
        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)

        }
        //Botão do menu para remover a tarefa
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            updateList()

        }
    }

    private fun updateList() {
        val list = TaskDataSource.getList()

        if (list.isEmpty()) {
            binding.includeEmpty.emptyState.visibility = View.VISIBLE
        } else {
            binding.includeEmpty.emptyState.visibility = View.GONE
        }
        binding.rvTasks.adapter = adapter
        adapter.submitList(null)
        adapter.submitList(list)
    }

    fun  loadData(){

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedString = sharedPreferences.getString("STRING_KEY",null)

        // list <-> adapter <-> savedstring

    }

    //função de MOCK
    fun popular(){
        val addtask = Task(
            title = "tarefa01",
            date = "01/01/01",
            hour = "01:01",
            id = intent.getIntExtra(AddTaskActivity.TASK_ID, 0),

        )
        TaskDataSource.insertTask(addtask)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}