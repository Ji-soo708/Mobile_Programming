package com.example.kulendar.home

import android.content.Context
import android.icu.text.IDNA
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.R
import com.example.kulendar.databinding.HometabitemListBinding


class HomeRecyclerViewAdapter(private val items:ArrayList<InfoData>): RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>(){
    interface MyItemClickListener{
        fun onItemClick(item: InfoData)
    }

    private lateinit var nItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        nItemClickListener=itemClickListener

    }
    fun addItem(item: InfoData){
        items.add(item)
        notifyDataSetChanged()

    }
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: HomeRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener{
            nItemClickListener.onItemClick(items[position])
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: HometabitemListBinding = HometabitemListBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: HometabitemListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: InfoData ){
            binding.homeCategory.text=item.info_title
            binding.homeTitle.text=item.info_text

        }
    }
}






