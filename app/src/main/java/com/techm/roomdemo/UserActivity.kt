package com.techm.roomdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.techm.roomdemo.databinding.ActivityMainBinding
import com.techm.roomdemo.db.User
import com.techm.roomdemo.db.UserDatabase
import com.techm.roomdemo.db.UserRepository

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: MyRecyclerViewAdapter
    private lateinit var factory: UserViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = UserDatabase.getInstance(application).userDAO
        val repository = UserRepository(dao)

        factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        binding.myViewModel = userViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        userViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun initRecyclerView() {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyRecyclerViewAdapter { selectedItem: User -> listItemClicked(selectedItem) }
        binding.subscriberRecyclerView.adapter = adapter
        displayUsersList()
    }

    private fun displayUsersList() {
        userViewModel.users.observe(this, Observer {
            Log.i("MYTAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(user: User) {
        //Toast.makeText(this,"selected name is ${user.name}",Toast.LENGTH_LONG).show()
        userViewModel.initUpdateAndDelete(user)
    }
}
