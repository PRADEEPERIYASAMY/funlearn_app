package com.example.funlearnv2.views.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.funlearnv2.R
import com.example.funlearnv2.databinding.ActivityChildBinding
import com.example.funlearnv2.utils.toast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChildActivity : BaseActivity() {

    private var _binding: ActivityChildBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var appBarConfigurationDrawer: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChildBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigationComponent()
        toast(firebaseAuth.currentUser!!.uid.toString())
    }

    private fun initNavigationComponent() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.child_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.childNavView.setupWithNavController(navController)
        binding.childBottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(onDestinationChangeListener)
        binding.childBottomNavView.menu[3].setOnMenuItemClickListener(menuItemClickListener)
        binding.childNavView.menu[4].setOnMenuItemClickListener(menuItemClickListener)
        binding.childDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private val onDestinationChangeListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        binding.childDrawerLayout.closeDrawer(GravityCompat.START)
        when (destination.id) {
            R.id.nav_game, R.id.nav_learn, R.id.nav_dashboard -> {
                binding.childBottomNavView.visibility = View.VISIBLE
            }
            else -> binding.childBottomNavView.visibility = View.GONE
        }
    }

    private val menuItemClickListener = MenuItem.OnMenuItemClickListener {
        if (binding.childDrawerLayout.isOpen)
            binding.childDrawerLayout.closeDrawer(GravityCompat.START)
        else
            binding.childDrawerLayout.openDrawer(GravityCompat.START)

        if (it.itemId == R.id.log_out && firebaseAuth.currentUser != null) {
            toast("signed out")
            firebaseAuth.signOut()
            navController.navigate(R.id.authenticationActivity)
        }

        true
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfigurationDrawer) || super.onSupportNavigateUp()


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
