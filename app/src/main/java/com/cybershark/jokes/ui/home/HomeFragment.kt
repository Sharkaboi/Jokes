package com.cybershark.jokes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.cybershark.jokes.R
import com.cybershark.jokes.databinding.FragmentHomeBinding
import com.cybershark.jokes.ui.home.util.JokeState
import com.cybershark.jokes.util.UIState
import com.cybershark.jokes.util.observe
import com.cybershark.jokes.util.shortSnackBar
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.bindProgressButton(binding.btnGetJoke)
        setupListeners()
        setupObservers()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupObservers() {
        observe(homeViewModel.uiState) { uiState: UIState ->
            binding.btnGetJoke.isEnabled = uiState !is UIState.Loading
            when (uiState) {
                is UIState.Loading -> {
                    binding.btnGetJoke.showProgress {
                        buttonText = "Loading"
                        gravity = DrawableButton.GRAVITY_TEXT_START
                        progressColorRes = R.color.white
                    }
                }
                is UIState.Error -> {
                    binding.btnGetJoke.hideProgress(R.string.get_joke)
                    binding.root.shortSnackBar(uiState.message)
                }
                is UIState.Success -> {
                    binding.btnGetJoke.hideProgress(R.string.get_joke)
                    binding.root.shortSnackBar(uiState.message)
                }
                else -> {
                    binding.btnGetJoke.hideProgress(R.string.get_joke)
                }
            }
        }
        observe(homeViewModel.currentJoke) { jokeState: JokeState ->
            binding.cardJoke.isVisible = true
            binding.tvJokeSetupHome.text = jokeState.joke.setup
            binding.tvJokePunchlineHome.text = jokeState.joke.punchline

            if (jokeState.isJokeFavorite) {
                binding.ibFavHome.load(R.drawable.ic_favorite_selected)
                binding.ibFavHome.setOnClickListener { homeViewModel.removeJoke(jokeState.joke) }
            } else {
                binding.ibFavHome.load(R.drawable.ic_favorite)
                binding.ibFavHome.setOnClickListener { homeViewModel.saveJoke(jokeState.joke) }
            }
        }
    }

    private fun setupListeners() {
        binding.btnGetJoke.setOnClickListener { homeViewModel.getRandomJoke() }
    }

}