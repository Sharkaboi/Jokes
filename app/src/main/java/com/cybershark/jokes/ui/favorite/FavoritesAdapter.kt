package com.cybershark.jokes.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cybershark.jokes.data.room.JokeEntity
import com.cybershark.jokes.databinding.JokeListItemBinding

class FavoritesAdapter(
    private val removeAction: (JokeEntity) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<JokeEntity>() {
        override fun areItemsTheSame(oldItem: JokeEntity, newItem: JokeEntity): Boolean {
            return oldItem.jokeId == newItem.jokeId
        }

        override fun areContentsTheSame(oldItem: JokeEntity, newItem: JokeEntity): Boolean {
            return oldItem == newItem
        }

    }
    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)

    class FavoritesViewHolder(
        private val binding: JokeListItemBinding,
        private val removeAction: (JokeEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(jokeEntity: JokeEntity) {
            binding.tvJokeSetup.text = jokeEntity.jokeSetup
            binding.tvJokePunchline.text = jokeEntity.jokePunchline
            binding.ibFav.setOnClickListener { removeAction(jokeEntity) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding =
            JokeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding, removeAction)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bindData(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int = listDiffer.currentList.size

    fun submitList(list: List<JokeEntity>) = listDiffer.submitList(list)
}