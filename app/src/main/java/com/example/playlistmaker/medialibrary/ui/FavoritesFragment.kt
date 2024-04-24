package com.example.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.search.ui.SearchResultAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavoritesViewModel>()

    private lateinit var favoritesAdapter: SearchResultAdapter
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavorites()

        setAdapter()

        viewModel.getSongs().observe(viewLifecycleOwner) { favorites ->
            if (favorites.isNotEmpty()) {
                favoritesAdapter.setData(favorites)
                showFavorites()
            } else {
                emptyFavorites()
            }
        }
    }

    private fun setAdapter() {
        favoritesAdapter = SearchResultAdapter { track ->
            if (clickDebounce()) {
                viewModel.onTrackClick(track)
                findNavController().navigate(
                    R.id.action_medialibraryFragment_to_playerFragment
                )
            }
        }
        binding.favoritesRecyclerView.adapter = favoritesAdapter
    }

    private fun showFavorites() {
        binding.emptyMedialibrary.emptyMedialibrary.isVisible = false
        binding.favoritesRecyclerView.isVisible = true
    }

    private fun emptyFavorites() {
        binding.emptyMedialibrary.emptyMedialibrary.isVisible = true
        binding.favoritesRecyclerView.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        fun newInstance() = FavoritesFragment()
    }
}