package com.xan.sqliteexample

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast



class DatabaseHandler(private var context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {



    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_CONTACTS = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"




    }

    override fun onCreate(db: SQLiteDatabase?) {


        val CREATE_CONTACTS_TABLE = ("CREATE TABLE '" + TABLE_CONTACTS +"'('"
                + KEY_ID + "' INTEGER PRIMARY KEY,'" + KEY_NAME + "' TEXT,'"
                + KEY_EMAIL + "' TEXT"+")")

        db?.execSQL(CREATE_CONTACTS_TABLE)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun addEmployee(emp: EmpModelClass):Long{

        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL,emp.userEmail )

        val success = db.insert(TABLE_CONTACTS,null,contentValues)
        db.close()

        return success


    }

    fun BuildTable():Cursor{
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(selectQuery,null)
        if(cursor != null){
            cursor.moveToFirst()
        }
        return cursor
    }

    fun viewEmployee():List<EmpModelClass>{

        val empList:ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: SQLiteException){
            return ArrayList()
        }
        var userId: String
        var userName: String
        var userEmail: String

        if(cursor.moveToFirst()){
            do{
                userId = cursor.getString(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))

                val emp = EmpModelClass(userId = userId, userName = userName, userEmail = userEmail)
                empList.add(emp)
            }while(cursor.moveToNext())
        }
        return empList
    }

    fun updateEmployee(emp: EmpModelClass): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID,emp.userId)
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL,emp.userEmail)
        val success = db.update(TABLE_CONTACTS,contentValues,"id="+emp.userId,null)
        db.close()
        return success
    }

    fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID,emp.userId)
        val success = db.delete(TABLE_CONTACTS,"id="+emp.userId,null)
        db.close()
        return success
    }


}