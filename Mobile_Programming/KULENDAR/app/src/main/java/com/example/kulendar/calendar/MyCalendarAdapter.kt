package com.example.kuleander.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.databinding.CaleanderitemListBinding

class MyCalendarAdapter(val items:ArrayList<MyCalendarData>) : RecyclerView.Adapter<MyCalendarAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: CaleanderitemListBinding):RecyclerView.ViewHolder(binding.root){
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
    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= CaleanderitemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.binding.title.text = items[position].Year.toString()+items[position].Month.toString()+items[position].Day.toString()+"   "+items[position].title
        holder.binding.title.text = items[position].title
    }

    override fun getItemCount(): Int {
        return items.size
    }
}