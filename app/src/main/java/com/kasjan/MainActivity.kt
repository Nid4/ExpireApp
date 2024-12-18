package com.kasjan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.kasjan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
}






