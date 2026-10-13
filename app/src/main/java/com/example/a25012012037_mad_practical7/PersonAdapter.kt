package com.example.a25012012037_mad_practical7

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class PersonAdapter(
    val context: Context,
    private var personList: ArrayList<Person>,
    private val onDeleteClickListener: ((Person) -> Unit)? = null
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.avatar)
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val phoneText: TextView = itemView.findViewById(R.id.phoneText)
        val emailText: TextView = itemView.findViewById(R.id.emailText)
        val addressText: TextView = itemView.findViewById(R.id.addressText)
        val buttonMap: ImageButton = itemView.findViewById(R.id.buttonMap)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.singleitem,
            parent,
            false
        )
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]

        holder.nameText.text = person.name
        holder.phoneText.text = person.phoneNo
        holder.emailText.text = person.emailId
        holder.addressText.text = person.address

        // Convert Person Object into Serializable and set to putExtra() method.
        // It will be used when user clicks on map button in PersonAdapter Class
        val obj = person as Serializable
        holder.buttonMap.setOnClickListener {
            Intent(this@PersonAdapter.context, MapActivity::class.java).apply {
                putExtra("Object", obj)
                this@PersonAdapter.context.startActivity(this)
            }
        }

        holder.deleteBtn.setOnClickListener {
            onDeleteClickListener?.invoke(person)
        }
    }

    override fun getItemCount(): Int = personList.size

    fun updateData(newList: ArrayList<Person>) {
        personList = newList
        notifyDataSetChanged()
    }
}
