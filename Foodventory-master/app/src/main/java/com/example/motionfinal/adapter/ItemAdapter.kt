package com.example.motionfinal.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.motionfinal.R
import com.example.motionfinal.model.Item
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ItemAdapter(private var items: List<Item>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val db = Firebase.firestore

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val txtCode: TextView = itemView.findViewById(R.id.details_txt_code)
        private val txtName: TextView = itemView.findViewById(R.id.details_txt_name_container)
        private val txtUsername: TextView = itemView.findViewById(R.id.details_txt_username_container)
        private val txtEmail: TextView = itemView.findViewById(R.id.details_txt_email_container)
        private val txtAdrees: TextView = itemView.findViewById(R.id.details_txt_address_container)

        private val btDelete: ImageView = itemView.findViewById(R.id.details_delete)

        fun bindItem(item: Item) {
            txtCode.text = item.id
            txtName.text = item.name
            txtUsername.text = item.username
            txtEmail.text = item.name
            txtAdrees.text = item.street +", " +item.suite +", " +item.city+", " +item.zipcode

            btDelete.setOnClickListener {
                val alert = AlertDialog.Builder(itemView.context)
                alert.setTitle("${item.id}: ${item.name}")
                alert.setMessage("Are you sure you want to delete this item?")

                alert.setPositiveButton("Yes") { dialog, which ->
                    db.collection("Items")
                        .document(item.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(itemView.context, "Item ${item.id} deleted successfully!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e->
                            Log.w("", "Error deleting with code: ", e)
                        }
                }
                alert.setNegativeButton("No") { dialog, which ->
                    Toast.makeText(itemView.context, "Cancelled deleting item", Toast.LENGTH_SHORT).show()
                }

                alert.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_details, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun showEditDialog(itemView: View, item: Item) {
        val dialog = Dialog(itemView.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_edit)

        dialog.window?.setLayout(
            AbsListView.LayoutParams.MATCH_PARENT,
            AbsListView.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var txtCode: TextView? = null

        var edtName: EditText? = null
        var edtQuantity: EditText? = null

        txtCode = dialog.findViewById(R.id.edit_dialog_code)

        edtName = dialog.findViewById(R.id.edit_dialog_name)
        edtQuantity = dialog.findViewById(R.id.edit_dialog_quantity)

        txtCode.text = item.id

        edtName.setText(item.name)
        edtQuantity.setText(item.username.toString())

        val btYes = dialog.findViewById(R.id.edit_dialog_yes) as TextView
        btYes.setOnClickListener {
            val name = edtName.text.toString()
            val quantity = edtQuantity.text.toString().toInt()

            db.collection("Items")
                .document(item.id)
                .update(
                    "code", item.id,
                    "name", name,
                    "quantity", quantity
                )
                .addOnSuccessListener {
                    Toast.makeText(itemView.context, "Successfully updated!", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(itemView.context, "Failed to update item!", Toast.LENGTH_SHORT)
                        .show()
                }
            dialog.dismiss()
        }

        val btNo = dialog.findViewById(R.id.edit_dialog_no) as TextView
        btNo.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

}