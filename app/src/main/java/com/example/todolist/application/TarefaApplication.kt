package com.example.todolist.application

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.todolist.helpers.HelperDB

class TarefaApplication : Application() {

    var helperDB: HelperDB? = null
        private set

    companion object{
        lateinit var instance: TarefaApplication
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }
}