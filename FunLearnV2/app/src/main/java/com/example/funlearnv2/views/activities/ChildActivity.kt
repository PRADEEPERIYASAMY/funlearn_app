package com.example.funlearnv2.views.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.ActivityChildBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChildActivity : BaseActivity() {

    private var _binding: ActivityChildBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var appBarConfigurationDrawer: AppBarConfiguration
    private lateinit var appBarConfigurationBottom: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChildBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigationComponent()
    }

    private fun initNavigationComponent() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.child_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.childNavView.setupWithNavController(navController)
        binding.childBottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(onDestinationChangeListener)
        binding.childBottomNavView.menu[3].setOnMenuItemClickListener(menuItemClickListener)
    }

    private val onDestinationChangeListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        binding.childDrawerLayout.closeDrawer(GravityCompat.START)
        when (destination.id) {
            R.id.nav_profile, R.id.nav_notification, R.id.nav_setting, R.id.alphabetListFragment, R.id.nav_community_chat -> {
                binding.childBottomNavView.visibility = View.GONE
            }
            R.id.nav_game, R.id.nav_learn, R.id.nav_dashboard -> {
                binding.childBottomNavView.visibility = View.VISIBLE
            }
        }
    }

    private val menuItemClickListener = MenuItem.OnMenuItemClickListener {
        if (binding.childDrawerLayout.isOpen)
            binding.childDrawerLayout.closeDrawer(GravityCompat.START)
        else
            binding.childDrawerLayout.openDrawer(GravityCompat.START)
        true
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfigurationDrawer) || super.onSupportNavigateUp()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
