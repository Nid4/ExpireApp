package com.kasjan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasjan.R
import java.text.SimpleDateFormat
import java.util.*

class DaysAdapter(
    private val days: List<Date>,
    private val onDayClick: (Date) -> Unit
) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val date = days[position]
        holder.bind(date, onDayClick)
    }

    override fun getItemCount(): Int = days.size

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val textViewDayName: TextView = itemView.findViewById(R.id.textViewDayName)

        fun bind(date: Date, onDayClick: (Date) -> Unit) {
            val formatDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val formatDayName = SimpleDateFormat("EEEE", Locale.getDefault())

            textViewDate.text = formatDate.format(date)
            textViewDayName.text = formatDayName.format(date)

            itemView.setOnClickListener { onDayClick(date) }
        }
    }
}
