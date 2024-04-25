package com.example.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModel<PlaylistsViewModel>()
    private lateinit var playListAdapter: PlayListAdapter
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updatePlayLists()
        viewModel.getPlayLists().observe(viewLifecycleOwner) { newPlayLists ->
            if (newPlayLists.isNotEmpty()) {
                showPlayLists()
            } else {
                emptyPlayLists()
            }
            playListAdapter.setData(newPlayLists)
        }

        binding.playListRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        playListAdapter = PlayListAdapter { playList ->
            if (clickDebounce()) {
                findNavController().navigate(
                    R.id.action_medialibraryFragment_to_singlePlayListFragment,
                    bundleOf(PLAYLIST to playList.id)
                )
            }
        }
        binding.playListRecyclerView.adapter = playListAdapter

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_medialibraryFragment_to_newPlayListFragment
            )
        }
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

    private fun showPlayLists() {
        binding.emptyPlaylists.noPlayLists.isVisible = false
        binding.playListRecyclerView.isVisible = true
    }

    private fun emptyPlayLists() {
        binding.emptyPlaylists.noPlayLists.isVisible = true
        binding.playListRecyclerView.isVisible = false
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val PLAYLIST = "playlist"
    }

}