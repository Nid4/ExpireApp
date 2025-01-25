package com.kasjan.activities


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kasjan.R
import com.kasjan.adapter.ViewPagerAdapter
import com.kasjan.databinding.ActivityMainBinding
import com.kasjan.fragments.DayFragment
import com.kasjan.fragments.DaysListFragment
import com.kasjan.fragments.SettingsFragment
import com.kasjan.model.ProductRepository
import kotlinx.coroutines.launch
import java.util.Date
class MainActivity : AppCompatActivity(), DaysListFragment.DaySelectionListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Konfiguracja ViewPager i TabLayout
        configureViewPagerAndTabs()

        // Synchronizacja danych
        syncDataWithFirebaseAndRoom()
    }

    override fun onDaySelected(date: Date) {
        viewPager.setCurrentItem(0, true) // Przełącz na fragment DayFragment
        val fragment = supportFragmentManager.findFragmentByTag("f0") as? DayFragment
        fragment?.updateDate(date) ?: Log.e("MainActivity", "DayFragment not found")
    }


    private fun configureViewPagerAndTabs() {
        viewPager = binding.viewpagerMain

        // Lista fragmentów dla ViewPager
        val fragments = listOf(
            DayFragment.newInstance(Date()), // Pierwszy fragment dla dzisiejszego dnia
            DaysListFragment(), // Fragment listy dni
            SettingsFragment() // Fragment ustawień (dodaj SettingsFragment w swoim kodzie)
        )

        // Ustawienie adaptera
        viewPagerAdapter = ViewPagerAdapter(this, fragments)
        viewPager.adapter = viewPagerAdapter

        // Połączenie z TabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewpagerMain) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_day)
                1 -> tab.text = getString(R.string.tab_list)
                2 -> tab.text = getString(R.string.tab_settings)
            }
        }.attach()
    }

    private fun syncDataWithFirebaseAndRoom() {
        val repository = ProductRepository(this)

        lifecycleScope.launch {
            // Synchronizacja danych
            repository.syncFirebaseToRoom()
            repository.syncRoomToFirebase()
        }
    }
}






