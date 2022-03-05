package com.test.fitnessstudios.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.fitnessstudios.data.Business
import com.test.fitnessstudios.databinding.BusinessListItemBinding

class BusinessListAdapter(
    private var onBusinessSelectedListener: OnBusinessSelectedListener
) : RecyclerView.Adapter<BusinessListAdapter.ViewHolder>() {

    private lateinit var binding: BusinessListItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BusinessListAdapter.ViewHolder {
        binding =
            BusinessListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: BusinessListAdapter.ViewHolder, position: Int) {
        val business = differ.currentList[position]
        holder.setData(business)
        holder.itemView.setOnClickListener {
            onBusinessSelectedListener.onBusinessSelected(business)
        }
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<Business>() {
        override fun areItemsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)


    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun setData(item: Business) {
            binding.apply {
                tvName.text = item.name
                tvDistance.text = "$$$$ - ${item.distance}"
            }
        }
    }

    interface OnBusinessSelectedListener {
        fun onBusinessSelected(business: Business)
    }
}