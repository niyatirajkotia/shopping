// Make sure your package name is correct
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

// Your class name
class ListDeatailsActivity : AppCompatActivity() {

    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var newItemEditText: EditText
    private lateinit var addItemButton: Button
    private lateinit var listNameTitle: TextView
    private lateinit var shoppingItemAdapter: Shopping_item_Adapter

    private var currentItems = ArrayList<ShoppingItem>()

    // This launcher handles the result from your edit screen
    private val editItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val index = data?.getIntExtra("UPDATED_ITEM_INDEX", -1) ?: -1

            if (index != -1) {
                // Get the updated data from the result
                val name = data!!.getStringExtra("UPDATED_ITEM_NAME") ?: ""
                val description = data.getStringExtra("UPDATED_ITEM_DESCRIPTION") ?: ""
                val quantity = data.getIntExtra("UPDATED_ITEM_QUANTITY", 1)

                // Update the item in our list
                val itemToUpdate = currentItems[index]

                // --- THIS IS THE FIX ---
                itemToUpdate.name = name // Add this line

                itemToUpdate.description = description
                itemToUpdate.quantity = quantity

                // Refresh the list to show the changes
                shoppingItemAdapter.notifyItemChanged(index)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_deatails)

        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)
        newItemEditText = findViewById(R.id.newItemEditText)
        addItemButton = findViewById(R.id.addItemButton)
        listNameTitle = findViewById(R.id.listNameTitle)

        val listName = intent.getStringExtra("LIST_NAME") ?: "My List"
        val listIndex = intent.getIntExtra("LIST_INDEX", -1)
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
        resultIntent.putExtra("UPDATED_LIST_INDEX", listIndex)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    private fun setupRecyclerView() {
        // THE FIX IS HERE: We now pass the onEditClick function
        shoppingItemAdapter = Shopping_item_Adapter(currentItems) { position ->
            launchItemCustomizationActivity(currentItems[position], position)
        }
        itemsRecyclerView.adapter = shoppingItemAdapter
    }

    // This function starts the new edit activity
    private fun launchItemCustomizationActivity(item: ShoppingItem, position: Int) {
        val intent = Intent(this, ItemCustomizationActivity::class.java).apply {
            putExtra("ITEM_INDEX", position)
            putExtra("ITEM_NAME", item.name)
            putExtra("ITEM_DESCRIPTION", item.description)
            putExtra("ITEM_QUANTITY", item.quantity)
        }
        editItemLauncher.launch(intent)
    }
}