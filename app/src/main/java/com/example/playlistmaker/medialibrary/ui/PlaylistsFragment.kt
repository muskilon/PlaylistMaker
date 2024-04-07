package com.example.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by activityViewModel<PlaylistsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_medialibraryFragment_to_newPlayListFragment
            )
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}