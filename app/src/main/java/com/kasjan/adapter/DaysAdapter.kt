package com.kasjan.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasjan.R
import java.text.SimpleDateFormat
import java.util.*

class DaysAdapter(

    private val daysList: List<Date>,
    private val onDayClick: (Date) -> Unit // Dodano obsługę kliknięć
) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false)
        return DayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val date = daysList[position]

        // Formatowanie daty na tekst
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.dayText.text = dateFormat.format(date)

        // Porównanie dat (dzień, miesiąc, rok)
        val calendar = Calendar.getInstance()
        calendar.time = date

        val todayCalendar = Calendar.getInstance()
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
        todayCalendar.set(Calendar.MINUTE, 0)
        todayCalendar.set(Calendar.SECOND, 0)
        todayCalendar.set(Calendar.MILLISECOND, 0)

        val isToday = calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR)

        // Zmień kolor tła dla dzisiejszego dnia
        if (isToday) {
            holder.itemView.setBackgroundColor(Color.parseColor("#F44336"))
        }else {
            holder.itemView.setBackgroundColor(Color.parseColor("#004D40")) // Domyślne tło
        }

        // Obsługa kliknięcia elementu
        holder.itemView.setOnClickListener {
            Log.d("DaysAdapter", "Clicked date: ${dateFormat.format(date)}")
            onDayClick(date)
        }
    }

    override fun getItemCount(): Int {
        return daysList.size
    }
}

