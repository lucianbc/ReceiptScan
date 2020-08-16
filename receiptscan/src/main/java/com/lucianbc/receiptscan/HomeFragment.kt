package com.lucianbc.receiptscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lucianbc.receiptscan.home.DashboardFragment
import com.lucianbc.receiptscan.home.ExportsFragment
import com.lucianbc.receiptscan.home.PendingFragment
import com.lucianbc.receiptscan.home.SettingsFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavigation()
    }

    private fun setNavigation() =
        DestinationsAdapter(this).attachToNav(home_nav, home_destinations_host)

    private class DestinationsAdapter(parent: Fragment): FragmentStateAdapter(parent) {
        private val destinations = listOf(
            { DashboardFragment() },
            { PendingFragment() },
            { ExportsFragment() },
            { SettingsFragment() }
        )

        override fun getItemCount() = destinations.size

        override fun createFragment(position: Int) =
            destinations[position].invoke()

        fun attachToNav(navigationView: BottomNavigationView, pager: ViewPager2) {
            pager.apply {
                adapter = this@DestinationsAdapter
                isUserInputEnabled = false
            }
            navigationView.setOnNavigationItemSelectedListener {
                val index = when(it.itemId) {
                    R.id.dashboard -> 0
                    R.id.pending -> 1
                    R.id.exports -> 2
                    R.id.settings -> 3
                    else -> null
                }
                index?.let { i -> pager.setCurrentItem(i, false) }
                true
            }
        }
    }
}
