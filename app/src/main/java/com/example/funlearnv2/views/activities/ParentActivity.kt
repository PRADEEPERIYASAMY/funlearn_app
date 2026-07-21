package com.example.funlearnv2.views.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.ActivityParentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentActivity : BaseActivity() {

    private var _binding: ActivityParentBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var appBarConfigurationDrawer: AppBarConfiguration
    private lateinit var appBarConfigurationBottom: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigationComponent()
    }

    private fun initNavigationComponent() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.parent_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.parentNavView.setupWithNavController(navController)
        binding.parentBottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(onDestinationChangeListener)
        binding.parentBottomNavView.menu[3].setOnMenuItemClickListener(menuItemClickListener)
    }

    private val onDestinationChangeListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        binding.parentDrawerLayout.closeDrawer(GravityCompat.START)
        when (destination.id) {
            /*R.id.nav_profile,R.id.nav_notification,R.id.nav_setting,R.id.nav_community_chat,R.id.alphabetListFragment -> binding.parentBottomNavView.visibility = View.GONE
            R.id.nav_game,R.id.nav_learn,R.id.nav_dashboard -> binding.parentBottomNavView.visibility = View.VISIBLE*/
        }
    }

    private val menuItemClickListener = MenuItem.OnMenuItemClickListener {
        if (binding.parentDrawerLayout.isOpen)
            binding.parentDrawerLayout.closeDrawer(GravityCompat.START)
        else
            binding.parentDrawerLayout.openDrawer(GravityCompat.START)
        true
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfigurationDrawer) || super.onSupportNavigateUp()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
