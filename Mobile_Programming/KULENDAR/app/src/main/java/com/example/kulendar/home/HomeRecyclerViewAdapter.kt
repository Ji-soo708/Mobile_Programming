package com.example.kulendar.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.databinding.HometabitemListBinding


class HomeRecyclerViewAdapter(val items:ArrayList<MyNotice>): RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>(),
    Filterable {
    var filteredNotice=ArrayList<MyNotice>()
    var itemFilter=ItemFilter()
    init{
        filteredNotice.addAll(items)
        notifyDataSetChanged()
    }
    inner class ItemFilter: Filter(){
        override fun performFiltering(p0: CharSequence): FilterResults {
            val filterString=p0.toString()
            Log.i("filter",filterString)
            val results= FilterResults()
            val filteredList:ArrayList<MyNotice> =ArrayList<MyNotice>()
            if(filterString.trim{it<=' '}.isEmpty()){
                results.values=items
                results.count=items.size
                return results
            }
            else {
                for (Notice in items) { // 나머지
                    if (Notice.notice_Title.contains(filterString) ||Notice.notice_Type.contains(filterString)) {
                        filteredList.add(Notice)
                    }
                }
            }
            results.values=filteredList
            results.count=filteredList.size

            return results
        }
        override fun publishResults(p0: CharSequence?, filterResults: FilterResults) {
            filteredNotice.clear()
            filteredNotice.addAll(filterResults.values as ArrayList<MyNotice>)
            notifyDataSetChanged()
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int){}
    }

    var nItemClickListener: OnItemClickListener?=null

    override fun getItemCount(): Int {
        if(filteredNotice.isEmpty()) {
            return items.size
        }
        else {
            return filteredNotice.size
        }
    }
    override fun onBindViewHolder(holder: HomeRecyclerViewAdapter.ViewHolder, position: Int) {
        if(filteredNotice.isEmpty()) {
            holder.binding.noticeType.text=items[position].notice_Type
            holder.binding.noticeTitle.text=items[position].notice_Title


        }
        else {

            holder.binding.noticeType.text = filteredNotice[position].notice_Type
            holder.binding.noticeTitle.text = filteredNotice[position].notice_Title

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: HometabitemListBinding = HometabitemListBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: HometabitemListBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.noticeTitle.setOnClickListener {
                nItemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

}




















