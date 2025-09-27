package com.example.shopping_app_final

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ListDetailsActivity : AppCompatActivity() {

    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var newItemEditText: EditText
    private lateinit var addItemButton: Button
    private lateinit var listNameTitle: TextView
    private lateinit var shoppingItemAdapter: Shopping_item_Adapter

    private var currentItems = ArrayList<ShoppingItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_deatails) // This should be your layout file name

        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)
        newItemEditText = findViewById(R.id.newItemEditText)
        addItemButton = findViewById(R.id.addItemButton)
        listNameTitle = findViewById(R.id.listNameTitle)

        val listName = intent.getStringExtra("LIST_NAME") ?: "My List"
        val listIndex = intent.getIntExtra("LIST_INDEX", -1)
        listNameTitle.text = listName

        // This is the crucial part that was missing. It gets the data.
        currentItems = DataManager.currentShoppingItems ?: arrayListOf()

        // This part connects your data to the visual list.
        setupRecyclerView()

        // This part handles the "Add" button clicks.
        addItemButton.setOnClickListener {
            val itemName = newItemEditText.text.toString()
            if (itemName.isNotBlank()) {
                val newItem = ShoppingItem(name = itemName, description = "", quantity = 1, isChecked = false)
                currentItems.add(newItem)
                shoppingItemAdapter.notifyItemInserted(currentItems.size - 1)
                newItemEditText.text.clear()
            } else {
                Toast.makeText(this, "Item name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // This part sets up the result to send back to the main screen.
        val resultIntent = Intent()
        resultIntent.putExtra("UPDATED_LIST_INDEX", listIndex)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    private fun setupRecyclerView() {
        // This was also missing. It creates the adapter and links it to the RecyclerView.
        shoppingItemAdapter = Shopping_item_Adapter(currentItems)
        itemsRecyclerView.adapter = shoppingItemAdapter
    }
}


