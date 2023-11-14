package com.example.homework8

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homework8.databinding.RecyclerItemBinding

class UserRecyclerAdapter(private val users: MutableList<User>) : RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindData()
        holder.deleteUser()
        holder.editUser()
    }

    inner class UserViewHolder(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun deleteUser() {
            binding.ivDelete.setOnClickListener{
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    users.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }

        fun editUser() {
            binding.ivEdit.setOnClickListener {
                Intent(binding.root.context, UserActivity::class.java).also {
                    it.putExtra("userEditing", users[adapterPosition])
                    binding.root.context.startActivity(it)
                }
            }
        }

        fun bindData() = with(binding) {
            with(users[adapterPosition]) {
                tvUserFirstName.text = firstName
                tvUserLastName.text = lastName
                tvUserEmail.text = email
            }
        }
    }
}