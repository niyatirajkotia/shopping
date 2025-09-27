package com.example.shopping_app_final

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainListAdapter(
    private val shoppingLists: List<ShoppingList>,
    private val onListClick: (Int) -> Unit // Lambda to handle clicks on a list
) : RecyclerView.Adapter<MainListAdapter.ListViewHolder>() {

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listNameTextView: TextView = view.findViewById(R.id.listNameTextView)
        val itemCountTextView: TextView = view.findViewById(R.id.itemCountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_list, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentList = shoppingLists[position]
        holder.listNameTextView.text = currentList.name
        holder.itemCountTextView.text = "${currentList.items.size} items"

        holder.itemView.setOnClickListener {
            onListClick(position)
        }
    }

    override fun getItemCount() = shoppingLists.size
}