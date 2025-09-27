package com.example.shopping_app_final // Make sure package is correct

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShoppingItemAdapter(
    private val shoppingItems: ArrayList<ShoppingItem>,
    private val onEditClick: (Int) -> Unit
) : RecyclerView.Adapter<ShoppingItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.itemCheckBox)
        val quantityTextView: TextView = view.findViewById(R.id.itemQuantityTextView)
        val editButton: Button = view.findViewById(R.id.editItemButton)
        val descriptionTextView: TextView = view.findViewById(R.id.itemDescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = shoppingItems[position]

        holder.checkBox.setText(currentItem.name)
        holder.quantityTextView.setText("Qty: ${currentItem.quantity}")
        holder.checkBox.isChecked = currentItem.isChecked

        if (currentItem.description.isNotBlank()) {
            holder.descriptionTextView.visibility = View.VISIBLE
            holder.descriptionTextView.text = currentItem.description
        } else {
            holder.descriptionTextView.visibility = View.GONE
        }

        fun applyStrikeThrough(isChecked: Boolean) {
            if (isChecked) {
                holder.checkBox.paintFlags = holder.checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.checkBox.paintFlags = holder.checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        applyStrikeThrough(holder.checkBox.isChecked)
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.isChecked = isChecked
            applyStrikeThrough(isChecked)
        }

        holder.editButton.setOnClickListener {
            onEditClick(position)
        }
    }

    override fun getItemCount() = shoppingItems.size
}