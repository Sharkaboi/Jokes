package com.cybershark.jokes.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybershark.jokes.data.room.JokeEntity
import com.cybershark.jokes.databinding.FragmentFavoritesBinding
import com.cybershark.jokes.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var favoritesAdapter: FavoritesAdapter
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoritesViewModel by viewModels<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter(deleteJokeAction)
        binding.rvFavorites.apply {
            this.adapter = favoritesAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            this.itemAnimator = DefaultItemAnimator()
            this.setHasFixedSize(true)
        }
    }

    private val deleteJokeAction: (JokeEntity) -> Unit = { joke ->
        Log.e("favoriteFragment", "delete clicked")
        favoritesViewModel.removeJoke(joke)
    }

    private fun setupObservers() {
        observe(favoritesViewModel.listOfFavoriteJokes) { listOfJokes ->
            binding.tvEmptyHint.isVisible = listOfJokes.isEmpty()
            favoritesAdapter.submitList(listOfJokes)
        }
        observe(favoritesViewModel.uiState) { uiState ->
            when (uiState) {
                is UIState.Loading -> binding.pbLoading.makeVisible()
                is UIState.Success -> {
                    binding.pbLoading.makeGone()
                    binding.root.shortSnackBar(uiState.message)
                }
                is UIState.Error -> {
                    binding.pbLoading.makeGone()
                    binding.root.shortSnackBar(uiState.message)
                }
                else -> binding.pbLoading.makeGone()
            }
        }
    }
}