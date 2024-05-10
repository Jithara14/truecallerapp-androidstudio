package com.example.truecallerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private var uploadPopupWindow: PopupWindow? = null
    private var updatePopupWindow: PopupWindow? = null
    private var deletePopupWindow: PopupWindow? = null

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().reference

        // Find the upload button
        val uploadButton: Button = findViewById(R.id.mainUpload)

        // Set OnClickListener for the upload button
        uploadButton.setOnClickListener {
            // Show the upload popup window
            showUploadPopupWindow()
        }

        // Find the update button
        val updateButton: Button = findViewById(R.id.mainUpdate)

        // Set OnClickListener for the update button
        updateButton.setOnClickListener {
            // Show the update popup window
            showUpdatePopupWindow()
        }

        // Find the delete button
        val deleteButton: Button = findViewById(R.id.mainDelete)

        // Set OnClickListener for the delete button
        deleteButton.setOnClickListener {
            // Show the delete popup window
            showDeletePopupWindow()
        }
    }

    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun showUploadPopupWindow() {
        // Inflate the layout for the upload popup window
        val popupView = LayoutInflater.from(this).inflate(R.layout.upload_confirmation_popup, null)

        // Create a PopupWindow object for upload
        uploadPopupWindow = PopupWindow(popupView, -1, -2, true)

        // Set the elevation of the upload popup window
        uploadPopupWindow?.elevation = 10.0F

        // Show the upload popup window at the center of the activity's root view
        uploadPopupWindow?.showAtLocation(window.decorView, 17, 0, 0)

        // Handle save button click inside the upload popup window
        val saveButton: Button = popupView.findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            // Retrieve data from EditText fields
            val uploadName = popupView.findViewById<EditText>(R.id.uploadName).text.toString()
            val uploadOperator = popupView.findViewById<EditText>(R.id.uploadOperator).text.toString()
            val uploadLocation = popupView.findViewById<EditText>(R.id.uploadLocation).text.toString()
            val uploadPhone = popupView.findViewById<EditText>(R.id.uploadPhone).text.toString()

            // Save data to Firebase
            saveDataToFirebase(uploadName, uploadOperator, uploadLocation, uploadPhone)

            // Dismiss the popup window after saving data
            uploadPopupWindow?.dismiss()
        }
    }

    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun showUpdatePopupWindow() {
        // Inflate the layout for the update popup window
        val popupView = LayoutInflater.from(this).inflate(R.layout.update_confirmation_popup, null)

        // Create a PopupWindow object for update
        updatePopupWindow = PopupWindow(popupView, -1, -2, true)

        // Set the elevation of the update popup window
        updatePopupWindow?.elevation = 10.0F

        // Show the update popup window at the center of the activity's root view
        updatePopupWindow?.showAtLocation(window.decorView, 17, 0, 0)

        // Handle update button click inside the update popup window
        val updateButton: Button = popupView.findViewById(R.id.updateButton)
        updateButton.setOnClickListener {
            // Retrieve data from EditText fields
            val updateName = popupView.findViewById<EditText>(R.id.updateName).text.toString()
            val updateOperator = popupView.findViewById<EditText>(R.id.updateOperator).text.toString()
            val updateLocation = popupView.findViewById<EditText>(R.id.updateLocation).text.toString()
            val updatePhone = popupView.findViewById<EditText>(R.id.referencePhone).text.toString()

            // Call a function to update data in Firebase based on the phone number
            updateDataInFirebase(updateName, updateOperator, updateLocation, updatePhone)

            // Dismiss the popup window after updating data
            updatePopupWindow?.dismiss()
        }
    }

    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun showDeletePopupWindow() {
        // Inflate the layout for the delete popup window
        val popupView = LayoutInflater.from(this).inflate(R.layout.delete_confirmation_popup, null)

        // Create a PopupWindow object for delete
        deletePopupWindow = PopupWindow(popupView, -1, -2, true)

        // Set the elevation of the delete popup window
        deletePopupWindow?.elevation = 10.0F

        // Show the delete popup window at the center of the activity's root view
        deletePopupWindow?.showAtLocation(window.decorView, 17, 0, 0)

        // Handle delete button click inside the delete popup window
        val deleteButton: Button = popupView.findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            // Retrieve the mobile number to be deleted
            val mobileNumber = popupView.findViewById<EditText>(R.id.deletePhone).text.toString()

            // Call a function to delete data from Firebase based on the mobile number
            deleteDataFromFirebase(mobileNumber)

            // Dismiss the popup window after deletion
            deletePopupWindow?.dismiss()
        }
    }

    private fun deleteDataFromFirebase(mobileNumber: String) {
        // Reference the Firebase node where the data is stored
        val uploadsRef = database.child("uploads")

        // Query the database to find the data with the provided mobile number
        val query = uploadsRef.orderByChild("phone").equalTo(mobileNumber)

        // Execute the query
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if there are any matching records
                if (dataSnapshot.exists()) {
                    // Iterate through the matching records
                    for (snapshot in dataSnapshot.children) {
                        // Remove the record from Firebase
                        snapshot.ref.removeValue()
                    }
                } else {
                    // Handle case where no matching records were found
                    // For example, show a toast indicating that no data was found for the provided mobile number
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle potential errors
                // For example, show a toast indicating that an error occurred while accessing the database
            }
        })
    }

    private fun updateDataInFirebase(updateName: String, updateOperator: String, updateLocation: String, updatePhone: String) {
        // Reference the Firebase node where the data is stored
        val uploadsRef = database.child("uploads")

        // Query the database to find the data with the provided mobile number
        val query = uploadsRef.orderByChild("phone").equalTo(updatePhone)

        // Execute the query
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if there are any matching records
                if (dataSnapshot.exists()) {
                    // Iterate through the matching records
                    for (snapshot in dataSnapshot.children) {
                        // Update the record in Firebase
                        snapshot.ref.child("name").setValue(updateName)
                        snapshot.ref.child("operator").setValue(updateOperator)
                        snapshot.ref.child("location").setValue(updateLocation)
                    }
                } else {
                    // Handle case where no matching records were found
                    // For example, show a toast indicating that no data was found for the provided mobile number
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle potential errors
                // For example, show a toast indicating that an error occurred while accessing the database
            }
        })
    }

    private fun saveDataToFirebase(uploadName: String, uploadOperator: String, uploadLocation: String, uploadPhone: String) {
        // Create a hashmap with data to be saved
        val dataToSave = hashMapOf(
            "name" to uploadName,
            "operator" to uploadOperator,
            "location" to uploadLocation,
            "phone" to uploadPhone
        )

        // Push the data to Firebase under a specific node (e.g., "uploads")
        database.child("uploads").push().setValue(dataToSave)
    }
}
