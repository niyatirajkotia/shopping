// Make sure your package name is correct
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

    private var itemIndex: Int = -1 // To remember which item we are editing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_customization)

        nameEditText = findViewById(R.id.nameEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        saveButton = findViewById(R.id.saveButton)

        // Get the data passed from the previous screen
        val extras = intent.extras
        if (extras != null) {
            itemIndex = extras.getInt("ITEM_INDEX", -1)
            val name = extras.getString("ITEM_NAME", "")
            val description = extras.getString("ITEM_DESCRIPTION", "")
            val quantity = extras.getInt("ITEM_QUANTITY", 1)

            // Fill the UI with the item's current data
            nameEditText.setText(name)
            descriptionEditText.setText(description)
            quantityEditText.setText(quantity.toString())

        } else {
            Toast.makeText(this, "Error loading item", Toast.LENGTH_SHORT).show()
            finish() // Close if no data was sent
        }

        saveButton.setOnClickListener {
            returnUpdatedItemData()
        }
    }

    private fun returnUpdatedItemData() {
        val updatedName = nameEditText.text.toString()
        if (updatedName.isBlank()) {
            Toast.makeText(this, "Item name cannot be empty", Toast.LENGTH_SHORT).show()
            return // Don't save if name is empty
        }

        val resultIntent = Intent()
        val updatedDescription = descriptionEditText.text.toString()
        val updatedQuantity = quantityEditText.text.toString().toIntOrNull() ?: 1

        // Put all the updated data into the intent to send it back
        resultIntent.putExtra("UPDATED_ITEM_INDEX", itemIndex)
        resultIntent.putExtra("UPDATED_ITEM_NAME", updatedName)
        resultIntent.putExtra("UPDATED_ITEM_DESCRIPTION", updatedDescription)
        resultIntent.putExtra("UPDATED_ITEM_QUANTITY", updatedQuantity)

        setResult(Activity.RESULT_OK, resultIntent)
        finish() // Close the edit screen
    }
}