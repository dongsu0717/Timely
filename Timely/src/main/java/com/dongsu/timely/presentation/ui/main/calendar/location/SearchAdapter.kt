package com.dongsu.timely.presentation.ui.main.calendar.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongsu.timely.R
import com.dongsu.timely.domain.model.PoiItem

class SearchAdapter(
    private val onPlaceClick: (PoiItem) -> Unit
) : ListAdapter<PoiItem, SearchAdapter.SearchViewHolder>(PlaceDiffCallback()) {

    // ViewHolder 클래스
    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlaceName: TextView = itemView.findViewById(R.id.tvPlaceName)

        fun bind(poiItem: PoiItem, onPlaceClick: (PoiItem) -> Unit) {
            tvPlaceName.text = poiItem.name
            itemView.setOnClickListener {
                onPlaceClick(poiItem)
            }
        }
    }

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place, parent, false)
        return SearchViewHolder(view)
    }

    // ViewHolder에 데이터 바인딩
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place, onPlaceClick)
    }

    // DiffUtil 콜백
    class PlaceDiffCallback : DiffUtil.ItemCallback<PoiItem>() {
        override fun areItemsTheSame(oldItem: PoiItem, newItem: PoiItem): Boolean {
            return oldItem.noorLat == newItem.noorLat && oldItem.noorLon == newItem.noorLon
        }

        override fun areContentsTheSame(oldItem: PoiItem, newItem: PoiItem): Boolean {
            return oldItem == newItem
        }
    }
}