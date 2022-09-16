package com.example.contacts.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.FavouriteClickable
import com.example.contacts.databinding.FavouriteItemBinding
import com.example.contacts.entity.User


class FavouriteListAdapter(private val context: Context, private val click: FavouriteClickable) : ListAdapter<User, FavouriteListAdapter.FavouriteItemViewHolder> (ListAdapterCallBack()) {

    lateinit var filteringList: List<User>

    private val valueFilter : ValueFilter? = null

    val colors = listOf("#1e7814", "#211da1", "#cc7518", "#0d68de")

    inner class FavouriteItemViewHolder(val binding: FavouriteItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.favFirstLetter.text = user.name[0].uppercase()
            binding.favouriteContainer.setBackgroundColor(Color.parseColor(colors.random()))
            binding.favName.text = user.name
            binding.favDetailedView.setOnClickListener{
                click.onFavouriteItemMenuClicked(user)
            }

            binding.root.setOnClickListener {
                click.onFavouriteItemClicked()
            }

        }
    }

    class ListAdapterCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteItemViewHolder {
        val binding = FavouriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteItemViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    fun getMyFilter(): Filter {
        return valueFilter ?: ValueFilter()
    }

    inner class ValueFilter : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            if (constraint != null && constraint.isNotEmpty()) {
                val filterList: MutableList<User> = ArrayList()
                for (i in filteringList.indices) {
                    if (filteringList[i].name.uppercase().contains(constraint.toString().uppercase())) {
                        filterList.add(filteringList[i])
                    }
                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = filteringList.size
                results.values = filteringList
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            submitList(results?.values as? List<User>)
        }

    }
}

