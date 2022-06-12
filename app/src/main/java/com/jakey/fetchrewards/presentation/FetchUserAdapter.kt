package com.jakey.fetchrewards.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakey.fetchrewards.data.remote.responses.FetchUser
import com.jakey.fetchrewards.databinding.ListItemBinding

class FetchUserAdapter : RecyclerView.Adapter<FetchUserAdapter.FetchUserViewHolder>() {

    inner class FetchUserViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<FetchUser>() {
        override fun areItemsTheSame(oldItem: FetchUser, newItem: FetchUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FetchUser, newItem: FetchUser): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var fetchUser: List<FetchUser>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FetchUserViewHolder {
        return FetchUserViewHolder(ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: FetchUserViewHolder, position: Int) {
        holder.binding.apply {
            val user = fetchUser[position]
            tvIdValue.text = user.id.toString()
            tvNameValue.text = user.name
            tvListidValue.text = user.listId.toString()
        }
    }

    override fun getItemCount(): Int {
        return fetchUser.size
    }

}