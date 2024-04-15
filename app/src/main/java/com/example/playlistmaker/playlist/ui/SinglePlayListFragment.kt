package com.example.playlistmaker.playlist.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSinglePlaylistBinding
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.SearchResultAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SinglePlayListFragment : Fragment() {
    private lateinit var binding: FragmentSinglePlaylistBinding
    private lateinit var navBar: BottomNavigationView
    private lateinit var tracksResultsAdapter: SearchResultAdapter
    private val viewModel by viewModel<SinglePlayListViewModel>()
    private val playListTracks = ArrayList<Track>()
    private var isClickAllowed = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSinglePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navBar = requireActivity().findViewById(R.id.bottomNavigationView)
        navBar.isVisible = false
        binding.arrowBack.setOnClickListener { exit() }

        viewModel.getPlayList(requireArguments().getLong(PLAYLIST))

        tracksResultsAdapter = SearchResultAdapter(playListTracks) { track ->
            if (clickDebounce()) {
                viewModel.onTrackClick(track)
                findNavController().navigate(
                    R.id.action_singlePlayListFragment_to_playerFragment
                )
            }
        }
        binding.playListTracksRecycler.adapter = tracksResultsAdapter

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            if (state.currentPlayList.trackCount > 0) {
                playListTracks.clear()
                playListTracks.addAll(state.currentPlayListTracks)
                tracksResultsAdapter.notifyDataSetChanged()
            } else playListTracks.clear()

            binding.length.text = MyApplication.getAppResources()
                .getQuantityString(R.plurals.minutes_plurals, state.totalTime, state.totalTime)
            binding.trackCount.text = MyApplication.getAppResources()
                .getQuantityString(
                    R.plurals.track_plurals,
                    state.currentPlayList.trackCount,
                    state.currentPlayList.trackCount
                )
            binding.description.text = state.currentPlayList.description
            binding.title.text = state.currentPlayList.title
            Glide.with(binding.cover)
                .load(state.currentPlayList.cover)
                .placeholder(R.drawable.placeholder)
                .into(binding.cover)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exit()
                }
            })
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

    private fun exit() {
        findNavController().navigateUp()
    }

    override fun onResume() {
        super.onResume()
        navBar.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        navBar.isVisible = true
    }

    companion object {
        const val EMPTY = ""
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        const val PLAYLIST = "playlist"
    }
}