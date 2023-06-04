package com.naumenko.rickandmorty.presentation

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.*
import com.naumenko.rickandmorty.R
import com.naumenko.rickandmorty.appComponent
import com.naumenko.rickandmorty.databinding.ActivityMainBinding
import com.naumenko.rickandmorty.presentation.characters.fragments.CharactersDetailsFragment
import com.naumenko.rickandmorty.presentation.characters.fragments.CharactersFilterBottomSheetFragment
import com.naumenko.rickandmorty.presentation.characters.fragments.CharactersListFragment
import com.naumenko.rickandmorty.presentation.episodes.fragments.EpisodesDetailsFragment
import com.naumenko.rickandmorty.presentation.episodes.fragments.EpisodesFilterBottomSheetFragment
import com.naumenko.rickandmorty.presentation.episodes.fragments.EpisodesListFragment
import com.naumenko.rickandmorty.presentation.locations.fragments.LocationsDetailsFragment
import com.naumenko.rickandmorty.presentation.locations.fragments.LocationsFilterBottomSheetFragment
import com.naumenko.rickandmorty.presentation.locations.fragments.LocationsListFragment

class MainActivity : AppCompatActivity(), Navigation {

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container)!!

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_characters -> {
                    clearBackStack()
                    onPressedCharactersList()
                }
                R.id.ic_locations -> {
                    clearBackStack()
                    onPressedLocationsList()
                }
                R.id.ic_episodes -> {
                    clearBackStack()
                    onPressedEpisodesList()
                }
            }
            return@setOnItemSelectedListener true
        }
        this.appComponent.inject(this)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CharactersListFragment>(R.id.fragment_container)
            }
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallback, true)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallback)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }

    override fun onPressedCharactersList() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<CharactersListFragment>(R.id.fragment_container)
        }
    }

    override fun onPressedCharacterDetails(characterId: Int) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container,
                CharactersDetailsFragment.newInstance(characterId)
            )
            addToBackStack(null)
        }
    }

    override fun onPressedCharactersFilter() {
        CharactersFilterBottomSheetFragment().show(
            supportFragmentManager,
            "CharactersFilterBottomSheet"
        )
    }

    override fun onPressedLocationsList() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<LocationsListFragment>(R.id.fragment_container)
        }
    }

    override fun onPressedLocationsDetails(locationId: Int) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container,
                LocationsDetailsFragment.newInstance(locationId)
            )
            addToBackStack(null)
        }
    }

    override fun onPressedLocationsFilter() {
        LocationsFilterBottomSheetFragment().show(
            supportFragmentManager,
            "LocationFilterBottomSheet"
        )
    }

    override fun onPressedEpisodesList() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<EpisodesListFragment>(R.id.fragment_container)
        }
    }

    override fun onPressedEpisodesDetails(episodeId: Int) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container,
                EpisodesDetailsFragment.newInstance(episodeId)
            )
            addToBackStack(null)
        }
    }

    override fun onPressedEpisodesFilter() {
        EpisodesFilterBottomSheetFragment().show(
            supportFragmentManager,
            "EpisodesFilterBottomSheet"
        )
    }

    override fun goBack() {
        supportFragmentManager.popBackStack()
    }

    private fun renderToolbar() = with(binding) {
        val fragment = currentFragment
        if (fragment is HasCustomTitle) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            binding.toolbar.title = getString(fragment.getTitleRes())
            binding.toolbar.setTitleTextColor(Color.WHITE)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
            binding.toolbar.title = getString(R.string.app_name)
        }
    }

    private fun clearBackStack() {
        val count = supportFragmentManager.backStackEntryCount
        for (i in 0..count) {
            supportFragmentManager.popBackStack()
        }
    }

    private val fragmentCallback = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            renderToolbar()
        }
    }
}