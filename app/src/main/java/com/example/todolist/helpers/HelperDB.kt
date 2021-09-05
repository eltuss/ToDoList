package com.example.todolist.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.todolist.model.Task

@RequiresApi(Build.VERSION_CODES.P)
class HelperDB(
    context: Context?,
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {

    companion object{
        private val NOME_BANCO = "tarefas.db"
        private val VERSAO_ATUAL = 1
    }

    val TABLE_NAME = "tarefas"
    val COLUMNS_ID = "id"
    val COLUMNS_TITULO = "titulo"
    val COLUMNS_DATA = "data"
    val COLUMNS_HORARIO = "horario"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE  $TABLE_NAME(" +
            "$COLUMNS_ID INTEGER NOT NULL," +
            "$COLUMNS_TITULO TEXT NOT NULL," +
            "$COLUMNS_DATA TEXT NOT NULL," +
            "$COLUMNS_HORARIO TEXT NOT NULL," +
            "" +
            "PRIMARY KEY($COLUMNS_ID AUTOINCREMENT" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion){
            db?.execSQL(DROP_TABLE)

        }
        onCreate(db)

    }
    fun buscarTarefas(busca: String): List<Task>{
        val db = readableDatabase ?: return mutableListOf()
        var lista = mutableListOf<Task>()
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_TITULO LIKE '%$busca%'"
        var cursor = db.rawQuery(sql, arrayOf())
        if (cursor == null){
            db.close()
            return mutableListOf()
        }
        while (cursor.moveToNext()){
            var tarefa = Task(
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_TITULO)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DATA)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_HORARIO))
            )
            lista.add(tarefa)
        }
        db.close()
        return lista
    }

    fun salvarTarefas(task: Task){
        val db = writableDatabase ?: return
        val sql = "INSERT INTO $TABLE_NAME ($COLUMNS_TITULO,$COLUMNS_DATA,$COLUMNS_HORARIO) VALUES (?,?,?,?)"
        val array = arrayOf(task.title, task.date, task.hour)
        db.execSQL(sql, array)
        db.close()

        Log.e("CriarBanco", "   Dados Adicionados(salvos)")

    }


}