package com.test.fitnessstudios.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.fitnessstudios.ui.fragment.ListFragment
import com.test.fitnessstudios.ui.fragment.MapFragment

class HomeScreenAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MapFragment()
            else -> ListFragment()
        }
    }

}