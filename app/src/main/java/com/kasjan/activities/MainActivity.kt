package com.kasjan.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kasjan.R
import com.kasjan.adapter.ViewPagerAdapter
import com.kasjan.databinding.ActivityMainBinding
import com.kasjan.fragments.DayFragment
import com.kasjan.fragments.DaysListFragment
import java.util.Date

class MainActivity : AppCompatActivity(), DaysListFragment.DaySelectionListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicjalizacja ViewPager2 i fragmentów
        viewPager = findViewById(R.id.viewpagerMain)

        val fragments = listOf(
            DayFragment.newInstance(Date()), // Pierwszy fragment wyświetla dzisiejszy dzień
            DaysListFragment()
        )

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        val adapter = ViewPagerAdapter(this)
        binding.viewpagerMain.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerMain) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_day)
                1 -> tab.text = getString(R.string.tab_list)
                2 -> tab.text = getString(R.string.tab_settings)
            }
        }.attach()

    }
    override fun onDaySelected(date: Date) {
        // Przełącz na pierwszy fragment
        viewPager.setCurrentItem(0, true)

        // Zaktualizuj DayFragment z wybraną datą
        (supportFragmentManager.findFragmentByTag("f0") as? DayFragment)?.updateDate(date)
    }
}





