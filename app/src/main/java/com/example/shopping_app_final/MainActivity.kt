package com.example.shopping_app_final

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

// Make sure this class name matches your file name (MainActivity)
class MainActivity : AppCompatActivity() {

    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var newListNameEditText: EditText
    private lateinit var addListButton: Button
    private lateinit var mainListAdapter: MainListAdapter

    private var allShoppingLists = ArrayList<ShoppingList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This must match your layout file name
        setContentView(R.layout.activity_main)

        mainRecyclerView = findViewById(R.id.mainRecyclerView)
        newListNameEditText = findViewById(R.id.newListNameEditText)
        addListButton = findViewById(R.id.addListButton)

        if (allShoppingLists.isEmpty()) {
            addSampleData()
        }

        setupRecyclerView()

        addListButton.setOnClickListener {
            val listName = newListNameEditText.text.toString()
            if (listName.isNotBlank()) {
                val newList = ShoppingList(name = listName, items = ArrayList())
                allShoppingLists.add(newList)
                mainListAdapter.notifyItemInserted(allShoppingLists.size - 1)
                newListNameEditText.text.clear()
            } else {
                Toast.makeText(this, "List name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        mainListAdapter = MainListAdapter(allShoppingLists) { position ->
            launchListDetailsActivity(position)
        }
        mainRecyclerView.adapter = mainListAdapter
    }

    private val listDetailsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val updatedIndex = data?.getIntExtra("UPDATED_LIST_INDEX", -1) ?: -1

            if (updatedIndex != -1) {
                mainListAdapter.notifyItemChanged(updatedIndex)
            }
        }
        DataManager.currentShoppingItems = null
    }

    private fun launchListDetailsActivity(position: Int) {
        val selectedList = allShoppingLists[position]
        DataManager.currentShoppingItems = selectedList.items

        // IMPORTANT: You need to create ListDetailsActivity for this to work.
        // For now, Android Studio will show an error on "ListDetailsActivity". That is OKAY.
        val intent = Intent(this, ListDeatailsActivity::class.java).apply {
            putExtra("LIST_NAME", selectedList.name)
            putExtra("LIST_INDEX", position)
        }
        listDetailsLauncher.launch(intent)
    }

    private fun addSampleData() {
        val groceryItems = arrayListOf(
            ShoppingItem("Milk", "1 gallon, whole", 1, false),
            ShoppingItem("Bread", "Wheat", 1, true),
            ShoppingItem("Eggs", "Dozen", 1, false)
        )
        val hardwareItems = arrayListOf(
            ShoppingItem("Nails", "2-inch", 50, false),
            ShoppingItem("Hammer", "Claw hammer", 1, true)
        )
        allShoppingLists.add(ShoppingList("Groceries", groceryItems))
        allShoppingLists.add(ShoppingList("Hardware Store", hardwareItems))
    }
}