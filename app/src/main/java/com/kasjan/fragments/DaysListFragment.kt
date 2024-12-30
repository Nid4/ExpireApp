package com.kasjan.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasjan.R
import com.kasjan.adapter.DaysAdapter
import java.text.SimpleDateFormat
import java.util.*

class DaysListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_days_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewDays)

        // Lista dni: od roku wstecz do 3 lat do przodu
        val calendar = Calendar.getInstance()
        val days = mutableListOf<Date>()
        for (i in -365..1095) { // Zakres: rok wstecz do 3 lat do przodu
            val day = calendar.clone() as Calendar
            day.add(Calendar.DAY_OF_YEAR, i)
            days.add(day.time)
        }

        // Konfiguracja RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = DaysAdapter(days) { selectedDate ->
            onDayClicked(selectedDate)
        }

        // Przewiń listę, aby dzisiejszy dzień był widoczny na starcie
        val todayIndex = 365 // Dzisiaj jest dokładnie w środku naszej listy
        recyclerView.post {
            layoutManager.scrollToPositionWithOffset(todayIndex, 0) // Scroll na dzisiejszy dzień
        }

        return view
    }

    private fun onDayClicked(date: Date) {
        // Obsłuż kliknięcie na dzień (np. przejście do innego fragmentu)
        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val formattedDate = format.format(date)
        println("Clicked on: $formattedDate") // Debugging

    }


}
