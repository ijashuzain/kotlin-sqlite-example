package com.xan.sqliteexample

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val u_id = findViewById<EditText>(R.id.u_id)
        val u_name = findViewById<EditText>(R.id.u_name)
        val u_email = findViewById<EditText>(R.id.u_email)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnView = findViewById<Button>(R.id.btnView)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnSave.setOnClickListener(){
            val id = u_id.text.toString()
            val name = u_name.text.toString()
            val email = u_email.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (id.trim() != "" && name.trim() != "" && email.trim() != "") {
                val status = databaseHandler.addEmployee(EmpModelClass(id, name, email))
                if (status > -1){
                    Toast.makeText(applicationContext,"Saved",Toast.LENGTH_SHORT).show()
                    u_id.text.clear()
                    u_name.text.clear()
                    u_email.text.clear()

                }else{
                    Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_SHORT).show()
            }
        }

        btnView.setOnClickListener(){

            val listView = findViewById<ListView>(R.id.listView)

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val emp:List<EmpModelClass> = databaseHandler.viewEmployee()
            val empArrayId = Array<String>(emp.size){"null"}
            val empArrayName = Array<String>(emp.size){"null"}
            val empArrayEmail = Array<String>(emp.size){"null"}
            var index = 0

            for (e in emp){
                empArrayId[index] = e.userId
                empArrayName[index] = e.userName
                empArrayEmail[index] = e.userEmail
                index++
            }
            val myListAdapter = MyListAdapter(this,empArrayId,empArrayName,empArrayEmail)
            listView.adapter = myListAdapter

        }

        btnUpdate.setOnClickListener(){
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.update_dialog,null)
            dialogBuilder.setView(dialogView)

            val edtId = dialogView.findViewById(R.id.updateId) as EditText
            val edtName = dialogView.findViewById(R.id.updateName) as EditText
            val edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText

            dialogBuilder.setTitle("Update")
            dialogBuilder.setMessage("Enter data")
            dialogBuilder.setPositiveButton("Update",DialogInterface.OnClickListener{_,_->
                val updateId = edtId.text.toString()
                val updateName = edtName.text.toString()
                val updateEmail = edtEmail.text.toString()

                val databaseHandler: DatabaseHandler = DatabaseHandler(this)

                if(updateId.trim()!="" && updateName.trim()!="" && updateEmail.trim()!=""){
                    val status = databaseHandler.updateEmployee(EmpModelClass(updateId,updateName,updateEmail))
                    if(status>-1){
                        Toast.makeText(applicationContext,"Updated",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
                }
            })

            dialogBuilder.setNegativeButton("Cancel",DialogInterface.OnClickListener{ dialog, which ->

            })
            val b = dialogBuilder.create()
            b.show()
        }

        btnDelete.setOnClickListener(){
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.delete_dialog,null)
            dialogBuilder.setView(dialogView)

            val delId = dialogView.findViewById(R.id.deleteId) as EditText
            dialogBuilder.setTitle("Delete")
            dialogBuilder.setMessage("Enter ID")
            dialogBuilder.setPositiveButton("Delete",DialogInterface.OnClickListener{_,_->
                val deleteId = delId.text.toString()

                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                if(deleteId.trim()!=""){
                    val status = databaseHandler.deleteEmployee(EmpModelClass(deleteId,"",""))
                    if (status > -1){
                        Toast.makeText(applicationContext,"Deleted",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT)
                    }
                }else{
                    Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
                }
            })
            dialogBuilder.setNegativeButton("Cancel",DialogInterface.OnClickListener{_,_->

            })
            val b = dialogBuilder.create()
            b.show()
        }

    }
}














