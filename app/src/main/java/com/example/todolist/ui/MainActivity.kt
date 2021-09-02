package com.example.todolist.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }


    private val ARQUIVO_PREFERENCIA = "salvarnomes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //chama o metodo de atualizar a lista
        updateList()

        //chama o metodo para inserir itens no app
        insertListeners()

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
            updateList()
        }
        //Botão do menu para remover a tarefa
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK)
            updateList()

    }

    private fun updateList() {
        val list = TaskDataSource.getList()

        if (list.isEmpty()) {
            binding.includeEmpty.emptyState.visibility = View.VISIBLE
        } else {
            binding.includeEmpty.emptyState.visibility = View.GONE
        }

        binding.rvTasks.adapter = adapter
        adapter.submitList(list)
        Log.e("TAG", "Atualizado : - $list")
    }
    @SuppressLint("CommitPrefEdits")
    fun save(){

    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}