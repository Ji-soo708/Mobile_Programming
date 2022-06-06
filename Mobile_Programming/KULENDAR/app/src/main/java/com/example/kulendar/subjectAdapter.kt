package com.example.kulendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Subject
import com.example.kulendar.databinding.SubrowBinding


class subjectAdapter(val items:ArrayList<Subject>): RecyclerView.Adapter<subjectAdapter.subjectViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class subjectViewHolder(val binding: SubrowBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.SubName.setOnClickListener {
                itemClickListener?.OnItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): subjectViewHolder {
        val view=SubrowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return subjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: subjectViewHolder, position: Int) {
        holder.binding.SubName.text=items[position].SubName
        holder.binding.SubNum.text=items[position].SubNum
        holder.binding.SubProf.text=items[position].Professor
        holder.binding.SubScore.text=items[position].Score
        holder.binding.SubTime.text=items[position].SubTime

    }

    override fun getItemCount(): Int {
        return items.size
    }
}