package com.kasjan.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kasjan.fragments.DayFragment
import com.kasjan.fragments.DaysListFragment
import com.kasjan.fragments.SettingsFragment

class ViewPagerAdapter(
    activity: AppCompatActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}


//    fun getFragment(position: Int): Fragment? {
//        return fragments[position]
//    }


