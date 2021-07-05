package com.druger.refresher.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.druger.refresher.R
import com.druger.refresher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            navController
        )
        setVisibilityNavigation(navController)
    }

    private fun setVisibilityNavigation(navController: NavController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.current,
                R.id.done -> {
                    showBottomNav()
                    showToolbar()
                }
                else -> {
                    hideBottomNav()
                    hideToolbar()
                }
            }
        }
    }

    private fun showToolbar() {
        binding.appBar.isVisible = true
    }

    private fun showBottomNav() {
        binding.bottomNavigation.isVisible = true
    }

    private fun hideToolbar() {
        binding.appBar.isVisible = false
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.isVisible = false
    }
}
