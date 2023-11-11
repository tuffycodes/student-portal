package com.example.diptistudentportal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diptistudentportal.databinding.ActivityMainBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity(),DataAdapter.ItemClickListener {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val db = FirebaseFirestore.getInstance()
    private val dataCollection = db.collection("data")
    private val data = mutableListOf<Data>()
    private lateinit var adapter: DataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = DataAdapter(data,this)

        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addBtn.setOnClickListener {
            val studentid = binding.studentIdET.text.toString()
            val name = binding.nameET.text.toString()
            val email = binding.emailET.text.toString()
            val subject = binding.subjectET.text.toString()
            val birthdate = binding.birthdateET.text.toString()

            if(studentid.isNotEmpty()&& name.isNotEmpty() && email.isNotEmpty() && subject.isNotEmpty() && birthdate.isNotEmpty()){
                addData(studentid,name,email,subject,birthdate)
            }
        }
        fetchData()
    }

    private fun fetchData() {

        dataCollection
            .orderBy("timestamp",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                data.clear()
                for (document in it){
                    val item = document.toObject(Data::class.java)
                    item.id = document.id
                    data.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data Fetch Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addData(studentid:String,name: String, email: String,subject:String,birthdate:String) {
        val newData = Data(studentid = studentid, name = name,email = email,subject = subject,birthdate = birthdate, timestamp = Timestamp.now())
        dataCollection.add(newData)
            .addOnSuccessListener {
                newData.id = it.id
                data.add(newData)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Data added Successfully", Toast.LENGTH_SHORT).show()
                binding.studentIdET.text?.clear()
                binding.nameET.text?.clear()
                binding.emailET.text?.clear()
                binding.subjectET.text?.clear()
                binding.birthdateET.text?.clear()
                fetchData()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Data added failed",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onEditItemClick(data: Data) {
        binding.studentIdET.setText(data.studentid)
        binding.nameET.setText(data.name)
        binding.emailET.setText(data.email)
        binding.subjectET.setText(data.subject)
        binding.birthdateET.setText(data.birthdate)
        binding.addBtn.text = "Update"

        binding.addBtn.setOnClickListener {
            val updateStudentId = binding.studentIdET.text.toString()
            val updateName = binding.nameET.text.toString()
            val updateEmail = binding.emailET.text.toString()
            val updateSubject = binding.subjectET.text.toString()
            val updateBirthdate = binding.birthdateET.text.toString()

            if (updateStudentId.isNotEmpty() && updateName.isNotEmpty() && updateEmail.isNotEmpty() && updateSubject.isNotEmpty() && updateBirthdate.isNotEmpty()){
                val updateData = Data(data.id,updateStudentId, updateName,updateEmail,updateSubject,updateBirthdate)

                dataCollection.document(data.id!!).set(updateData)
                    .addOnSuccessListener {
                        binding.studentIdET.text?.clear()
                        binding.nameET.text?.clear()
                        binding.emailET.text?.clear()
                        binding.subjectET.text?.clear()
                        binding.birthdateET.text?.clear()
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this,"Data Updated",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"Data Updated Failed",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onDeleteItemClick(data: Data) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Delete Files")
        dialog.setMessage("Do You want to Delete Files")
        dialog.setIcon(R.drawable.delete)
        dialog.setPositiveButton("YES") { dialogInterface, which ->
            dataCollection.document(data.id!!).delete()
                .addOnSuccessListener {
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this,"Data Deleted",Toast.LENGTH_SHORT).show()
                    fetchData()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Data Deletion Failed!",Toast.LENGTH_SHORT).show()
                }

        }
        dialog.setNegativeButton("No") { dialogInterface, which ->
            //startActivity((Intent(this,MainActivity::class.java)))
        }

        val alertDialog: AlertDialog = dialog.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
}