package com.example.contacts.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contacts.Clickable
import com.example.contacts.R
import com.example.contacts.databinding.ContactItemBinding
import com.example.contacts.entity.User
import kotlin.math.log

class ContactListAdapter(
    private val context: Context,
    private val click: Clickable
) : ListAdapter<User, ContactListAdapter.ContactItemViewHolder>(ListAdapterCallBack()){

    val colors = context.resources.getIntArray(R.array.colors)
    lateinit var filteringList: List<User>
    private val valueFilter : ValueFilter? = null

    class ListAdapterCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            Log.i("<<<", "areContentsTheSame: $oldItem $newItem")
            return oldItem == newItem
        }
    }


    inner class ContactItemViewHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(user: User){
            binding.tvName.text = user.name
            user.photoUri?.let {
                Glide.with(context).load(it.toUri()).into(binding.photoPicker)
                binding.tvProfile.visibility = View.GONE
            } ?: kotlin.run{
                binding.tvProfile.text = user.name[0].uppercase()
                println(colors[0])
//                context.resources.getLayout(R.layout.contact_item).setProperty("")
//                binding.tvProfile.background.colorFilter(colors.random(), PorterDuff.Mode.SRC_ATOP)
                val drawable: GradientDrawable = binding.tvProfile.background as GradientDrawable

                drawable.setColor(colors.random())
                binding.contactProfile.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        val user = getItem(position)
        holder.itemView.setOnClickListener { click.onContactItemClicked(user) }
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
