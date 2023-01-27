package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidsApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteroidsFilter

class MainFragment : Fragment() {

    //Setting up the MainviewModel of the MainFragment
    private val asteroidsViewModel: MainViewModel by viewModels {
        val application = requireActivity().application
        MainViewModel.Factory((application as AsteroidsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflating the layout using data binding and setting the lifecycle owner to this fragment
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = asteroidsViewModel

        //Setting up the adapter of the asteroids recyclerview
        val adapter = AsteroidListAdapter(AsteroidListAdapter.AsteroidListener { asteroid ->
            asteroid.let {
                this.findNavController().navigate(
                    MainFragmentDirections
                        .actionShowDetail(asteroid)
                )
            }
        })
        binding.asteroidRecycler.adapter = adapter

        //Observing the asteroids change and resetting the adapter's data
        asteroidsViewModel.asteroids.observe(viewLifecycleOwner, Observer { asteroids ->
            asteroids.let {
                adapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        asteroidsViewModel.onChangeFilter(
            when (item.itemId) {
                R.id.view_today_asteroids -> AsteroidsFilter.TODAYS_ASTEROIDS
                R.id.view_saved_asteroids -> AsteroidsFilter.SAVED_ASTEROIDS
                else -> AsteroidsFilter.WEEK_ASTEROIDS
            }
        )
        return true
    }
}

