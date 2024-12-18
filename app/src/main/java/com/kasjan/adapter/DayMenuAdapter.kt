package com.kasjan.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kasjan.MainActivity
import com.kasjan.R
import java.text.SimpleDateFormat
import java.util.*

class DayMenuAdapter(
    private val days: List<Date>,
    private val onDaySelected: (Int) -> Unit,
    var selectedDayIndex: Int
) : RecyclerView.Adapter<DayMenuAdapter.DayViewHolder>() {

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.tv_day)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val context = holder.itemView.context
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        holder.dayText.text = dateFormat.format(days[position])

        // Wyróżnij wybrany dzień
        if (position == selectedDayIndex) {
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.buttonColor))
        } else {
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }

        // Kliknięcie na dzień
        holder.itemView.setOnClickListener {
            val previousSelected = selectedDayIndex
            selectedDayIndex = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedDayIndex)
            onDaySelected(position)
        }
    }

    override fun getItemCount(): Int = days.size
}
