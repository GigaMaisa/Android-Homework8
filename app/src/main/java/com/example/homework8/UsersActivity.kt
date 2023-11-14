package com.example.homework8

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework8.databinding.ActivityMainBinding

class UsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recycler: RecyclerView
    private val listOfUsers = UsersList.listOfUsers

    private val dataBetweenActivities = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
                binding.recycler.adapter?.notifyItemInserted(listOfUsers.size - 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            recycler = it.recycler
        }

        setContentView(binding.root)
        addUserClick()
        setUpUserRecycler()
    }

    override fun onStart() {
        super.onStart()
        val indexChangedUser = intent.getIntExtra("UPDATED_USER_INDEX", -1)
        if (indexChangedUser != -1) {

            recycler.adapter?.notifyItemChanged(indexChangedUser)
        }
    }

    private fun setUpUserRecycler() {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = UserRecyclerAdapter(listOfUsers)
    }

    private fun addUserClick() {
        binding.ibAdd.setOnClickListener {
            startUserActivity()
        }
    }

    private fun startUserActivity() {
        Intent(this, UserActivity::class.java).also {
            it.putExtra("SOURCE_ACTIVITY_NAME", this::class.java.toString())
            dataBetweenActivities.launch(it)
        }
    }
}