package com.example.firstapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class HistoryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        auth = FirebaseAuth.getInstance()
        database = DatabaseHelper(this)

        initViews()
        loadHistory()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadHistory() {
        val currentUser = auth.currentUser
        val userName = currentUser?.email?.split("@")?.get(0) ?: ""

        val records = database.getBMIRecordsByUser(userName)

        if (records.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_records), Toast.LENGTH_SHORT).show()
        }

        adapter = HistoryAdapter(records)
        recyclerView.adapter = adapter
    }
}