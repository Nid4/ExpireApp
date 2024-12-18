package com.kasjan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kasjan.fragments.DayFragment
import com.kasjan.fragments.ListDaysFragment
import com.kasjan.fragments.SettingsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = arrayOfNulls<Fragment>(itemCount)

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            1 -> DayFragment()
            2 -> ListDaysFragment()
            3 -> SettingsFragment()
            else -> DayFragment()
        }
        fragments[position] = fragment
        return fragment
    }

//    fun getFragment(position: Int): Fragment? {
//        return fragments[position]
//    }
}

