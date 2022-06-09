package com.example.kulendar.dday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.databinding.DdayitemListBinding

class MyDdayAdapter(val items:ArrayList<MyDdayData>) : RecyclerView.Adapter<MyDdayAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: DdayitemListBinding):RecyclerView.ViewHolder(binding.root){
        //val textView = itemView.findViewById<TextView>(R.id.textView)
        //val meaning=itemView.findViewById<TextView>(R.id.meaning)
    }

    fun updateRecycleerView(){
        notifyDataSetChanged()
    }

    fun moveItem(oldPos:Int,newPos:Int){
        val item=items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos,item)
        notifyItemMoved(oldPos, newPos)
    }
    fun removeItem(pos:Int):Int{
        items.removeAt(pos)
        notifyItemRemoved(pos)
        return pos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= DdayitemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ddayTitle.text = items[position].title
        if (items[position].resultNumber >= 0) {
            holder.binding.ddayDday.text = String.format("D-%d", items[position].resultNumber)
        } else {
            val absR = Math.abs(items[position].resultNumber)
            holder.binding.ddayDday.text = String.format("D+%d", absR)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}