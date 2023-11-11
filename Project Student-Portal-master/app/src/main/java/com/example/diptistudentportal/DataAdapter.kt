package com.example.diptistudentportal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(private val data:List<Data>,private val itemClickListener: ItemClickListener):RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onEditItemClick(data: Data)
        fun onDeleteItemClick(data: Data)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.nameTV)
        val email = itemView.findViewById<TextView>(R.id.emailTV)
        val studentid = itemView.findViewById<TextView>(R.id.studentIdTV)
        val subject = itemView.findViewById<TextView>(R.id.subjectTV)
        val birthdate = itemView.findViewById<TextView>(R.id.birthdateTV)
        val edit = itemView.findViewById<ImageButton>(R.id.editBtn)
        val delete = itemView.findViewById<ImageButton>(R.id.deleteBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.studentid.text = item.studentid
        holder.name.text = item.name
        holder.email.text = item.email
        holder.subject.text = item.subject
        holder.birthdate.text = item.birthdate

        holder.edit.setOnClickListener {
            itemClickListener.onEditItemClick(item)

        }
        holder.delete.setOnClickListener {
            itemClickListener.onDeleteItemClick(item)
        }


    }
}