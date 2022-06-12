package com.jakey.fetchrewards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.jakey.fetchrewards.data.remote.responses.FetchUser
import com.jakey.fetchrewards.databinding.ActivityMainBinding
import com.jakey.fetchrewards.presentation.FetchUserAdapter
import com.jakey.fetchrewards.presentation.FetchViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fetchAdapter: FetchUserAdapter
    private lateinit var viewModel: FetchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FetchViewModel::class.java]

        viewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it
        }
        viewModel.getUsers()

        setupRecyclerView()


    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
        fetchAdapter = FetchUserAdapter()
        adapter = fetchAdapter

        viewModel.fetchUsers.observe(this@MainActivity) { response ->
            var dataList: MutableList<FetchUser> = emptyList<FetchUser>().toMutableList()
            // Grouped all the users' list_id as keys in a map. Observing the response in
            // the LiveData
            var dataMap = response.groupBy { it.listId }

            // I took each entry from the map and sorted its value(List<FetchUser>) based on the its name parameter.
            dataMap.entries.forEach {

                /**
                 This was easily the trickiest part of the whole project.
                 String values like "Item 4" come before "Item 380" because a string only cares
                 that the 4 is bigger than the 3 in 380. I had the right plan to fix it right away
                 however, I kept trying to parse the string before I filtered out the blank/nulls.
                 */

                // This funtion takes the names and splits the string at a " ". then takes the last
                // second element and compares it as an Int as opposed to a string.
                // This only works because I can guarantee the the name format as "Item {number}"
                fun number(str: String) = str.split(" ")[1].toInt()

                // Filtering the non nulls and blanks before I sort so that I am not trying to parse
                // null or blank strings for my number function
                val users = it.value.filter { it.name != null }.filter { it.name.isNotBlank() }.sortedBy { number(it.name) }
                users.forEach { user ->
                    dataList.add(user)
                }

            }

            // Setting my adapter list sorted by list Id. I already sorted the names within the listIds
            // So that now the users are sorted by names within their respected listId.
            fetchAdapter.fetchUser = dataList.sortedBy { it.listId }

        }
    }
}

