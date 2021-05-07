package com.obelab.ui.ui.healthmaxisb

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.obelab.ui.databinding.RecyclerViewItemBinding

class AdapterDeviceList(private val onclick: ((ListItem) -> Unit)) : RecyclerView.Adapter<AdapterDeviceList.ViewHolderDeivce>() {
    var deviceList: List<ListItem> = emptyList()

    @BindingAdapter("title", "vid")
    fun bindinttext(view: TextView, title: String?, vid: Int?) {
        view.text = "$title = $vid"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDeivce {
        val binding : RecyclerViewItemBinding =  RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.setClickListiner {
            binding.listitem?.let {
                onclick.invoke(it)
            }



        }
        return ViewHolderDeivce(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderDeivce, position: Int) {
        val item: ListItem = deviceList[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = deviceList.size


    class ViewHolderDeivce(private val binding: RecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: ListItem) {
            binding.apply {
                listitem = item
            }
        }

    }
}