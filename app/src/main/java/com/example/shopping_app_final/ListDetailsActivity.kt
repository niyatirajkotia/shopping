package com.example.shopping_app_final

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ListDetailsActivity : AppCompatActivity() {

    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var newItemEditText: EditText
    private lateinit var addItemButton: Button
    private lateinit var listNameTitle: TextView
    private lateinit var shoppingItemAdapter: ShoppingItemAdapter
    private var currentItems = ArrayList<ShoppingItem>()
    private val editItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val index = data?.getIntExtra("new_item_idx", -1) ?: -1
            if (index != -1) {
                val name = data!!.getStringExtra("new_item_name") ?: ""
                val description = data.getStringExtra("new_item_desc") ?: ""
                val quantity = data.getIntExtra("new_item_qty", 1)
                val itemToUpdate = currentItems[index]
                itemToUpdate.name = name
                itemToUpdate.description = description
                itemToUpdate.quantity = quantity
                shoppingItemAdapter.notifyItemChanged(index)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_details)
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)
        newItemEditText = findViewById(R.id.newItemEditText)
        addItemButton = findViewById(R.id.addItemButton)
        listNameTitle = findViewById(R.id.listNameTitle)

        val listName = intent.getStringExtra("list_name") ?: "My List"
        val listIndex = intent.getIntExtra("list_idx", -1)
        listNameTitle.text = listName

        currentItems = DataManager.currentShoppingItems ?: arrayListOf()

        setupRecyclerView()

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

        val resultIntent = Intent()
        resultIntent.putExtra("UPDATED_list_idx", listIndex)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    private fun setupRecyclerView() {
        shoppingItemAdapter = ShoppingItemAdapter(currentItems) { position ->
            launchItemCustomizationActivity(currentItems[position], position)
        }
        itemsRecyclerView.adapter = shoppingItemAdapter
    }

    private fun launchItemCustomizationActivity(item: ShoppingItem, position: Int) {
        val intent = Intent(this, ItemCustomizationActivity::class.java).apply {
            putExtra("item_idx", position)
            putExtra("item_name", item.name)
            putExtra("item_desc", item.description)
            putExtra("item_qty", item.quantity)
        }
        editItemLauncher.launch(intent)
    }
}