package com.example.kulendar.alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.R

class AlarmRecyclerViewAdapter(val alarmList: MutableList<Alarm>) : RecyclerView.Adapter<AlarmRecyclerViewAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view = v

        fun bind(alarm: Alarm) {
            view.findViewById<TextView>(R.id.alarm_date).text = alarm.date
            view.findViewById<TextView>(R.id.alarm_infotext).text = alarm.title

            if(alarm.repeatOnOff == 0) {
                view.findViewById<ImageView>(R.id.repeatImage).setImageResource(R.drawable.star_off)
            } else if(alarm.repeatOnOff == 1) {
                view.findViewById<ImageView>(R.id.repeatImage).setImageResource(R.drawable.star_on)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.alarmitem_list, parent, false)
        return MyViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = alarmList[position]

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.apply {
            bind(item)
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    fun moveItem(oldPos:Int, newPos:Int){
        val item = alarmList[oldPos]
        alarmList.removeAt(oldPos)
        alarmList.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos:Int){
        alarmList.removeAt(pos)
        notifyItemRemoved(pos)
    }

}