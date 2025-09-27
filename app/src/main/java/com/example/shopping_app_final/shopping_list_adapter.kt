
package com.example.shopping_app_final

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Class name matches file name and uses your "Shopping_item" class
class Shopping_item_Adapter(
    private val shoppingItems: ArrayList<ShoppingItem>
) : RecyclerView.Adapter<Shopping_item_Adapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.itemCheckBox)
        val quantityTextView: TextView = view.findViewById(R.id.itemQuantityTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_list, parent, false) // This layout needs to be created
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = shoppingItems[position]
        holder.checkBox.text = currentItem.name
        holder.checkBox.isChecked = currentItem.isChecked
        holder.quantityTextView.text = "Qty: ${currentItem.quantity}"
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.isChecked = isChecked
        }
        if (currentItem.isChecked) {
            holder.checkBox.paintFlags = holder.checkBox.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.checkBox.paintFlags = holder.checkBox.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun getItemCount() = shoppingItems.size
}