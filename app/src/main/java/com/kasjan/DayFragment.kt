package com.kasjan
import ProductAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasjan.model.Product

class DayFragment : Fragment() {

    companion object {
        fun newInstance(day: Int): DayFragment {
            val fragment = DayFragment()
            val args = Bundle()
            args.putInt("DAY", day)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Przykładowe dane
        val productList = listOf(
            Product("Mleko", "Nabiał", 10, "2024-12-31"),
            Product("Chleb", "Pieczywo", 5, "2024-12-15"),
            Product("Masło", "Nabiał", 2, "2024-12-20")
        )

        recyclerView.adapter = ProductAdapter(productList)
        return view
    }
}
