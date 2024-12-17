package com.kasjan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        // Liczba fragmentów (możesz zmienić na dynamiczne)
        return 7 // np. na tydzień
    }

    override fun createFragment(position: Int): Fragment {
        // Zwraca odpowiedni fragment na podstawie pozycji
        return DayFragment.newInstance(position)
    }
}

