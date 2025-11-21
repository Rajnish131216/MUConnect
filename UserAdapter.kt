package com.example.muconnect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muconnect.R
import com.example.muconnect.databinding.ItemUserBinding
import com.example.muconnect.models.User

class UserAdapter(
    private val users: List,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter() {

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvName.text = user.name
            binding.tvDepartment.text = "${user.department} - ${user.year}"
            binding.tvStatus.text = if (user.isOnline) "Online" else "Offline"

            if (user.profileImageUrl.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .into(binding.ivProfile)
            }

            binding.root.setOnClickListener {
                onUserClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size
}