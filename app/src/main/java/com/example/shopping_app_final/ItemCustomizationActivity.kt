package com.example.shopping_app_final

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class ItemCustomizationActivity : AppCompatActivity() {
    private lateinit var nameEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var quantityEditText: TextInputEditText
    private lateinit var saveButton: Button

    private var itemIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_customization)

        nameEditText = findViewById(R.id.nameEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        saveButton = findViewById(R.id.saveButton)
        val extras = intent.extras
        if (extras != null) {
            itemIndex = extras.getInt("item_idx", -1)
            val name = extras.getString("item_name", "")
            val description = extras.getString("item_desc", "")
            val quantity = extras.getInt("item_qty", 1)
            nameEditText.setText(name)
            descriptionEditText.setText(description)
            quantityEditText.setText(quantity.toString())

        } else {
            Toast.makeText(this, "Error loading item", Toast.LENGTH_SHORT).show()
            finish()
        }
        saveButton.setOnClickListener {
            returnUpdatedItemData()
        }
    }

    private fun returnUpdatedItemData() {
        val updatedName = nameEditText.text.toString()
        if (updatedName.isBlank()) {
            Toast.makeText(this, "Item name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent()
        val updatedDescription = descriptionEditText.text.toString()
        val updatedQuantity = quantityEditText.text.toString().toIntOrNull() ?: 1
        resultIntent.putExtra("new_item_idx", itemIndex)
        resultIntent.putExtra("new_item_name", updatedName)
        resultIntent.putExtra("new_item_desc", updatedDescription)
        resultIntent.putExtra("new_item_qty", updatedQuantity)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}