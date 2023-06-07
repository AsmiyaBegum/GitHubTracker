package com.ab.githubtrackerapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.ab.githubtrackerapplication.databinding.ActivityMainBinding
import com.ab.githubtrackerapplication.gitRepo.LandingFragment
import com.jakewharton.rxbinding.view.clicks
import rx.android.schedulers.AndroidSchedulers

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.addRepository.clicks()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.action_LandingFragment_to_AddRepoFragment)
            }

    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val currentDestination = navController.currentDestination?.id

        if (currentDestination == R.id.LandingFragment) {
            (currentDestination as LandingFragment)?.onBackPressed()
        } else {
            // Navigate to the LandingFragment when pressing back from other destinations
            navController.navigate(R.id.action_AddRepoFragment_to_LandingFragment)
        }
    }

}