package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private val viewModel by activityViewModel<PlayerViewModel>()
    private var isClickAllowed = true
    private lateinit var navBar: BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TAG", savedInstanceState.toString())

        if (savedInstanceState == null) {
            viewModel.setListener()
            viewModel.preparePlayer()
        }
        navBar = requireActivity().findViewById(R.id.bottomNavigationView)
        navBar.isVisible = false

        viewModel.getPlayStatus().observe(viewLifecycleOwner) { status ->
            binding.timeElapsed.text = status.timeElapsed
            binding.playButton.isClickable = status.playButtonClickableState
            binding.playButton.setImageResource(PlayerButtons.valueOf(status.playButtonImage).button)
            if (status.isFavorites) {
                binding.addToFavorites.setImageResource(R.drawable.favorite_button_on)
            } else {
                binding.addToFavorites.setImageResource(R.drawable.favorite_button_off)
            }

            Glide.with(this)
                .load(status.currentTrack.artworkUrl512)
                .placeholder(R.drawable.placeholder_big)
                .transform(
                    RoundedCorners(
                        view.resources.getDimension(R.dimen.player_album_cover_corner_radius)
                            .toInt()
                    )
                )
                .into(binding.albumCover)
            binding.trackName.text = status.currentTrack.trackName
            binding.artistName.text = status.currentTrack.artistName
            binding.trackDuration.text = status.currentTrack.trackTime
            binding.album.text = status.currentTrack.collectionName
            binding.year.text = status.currentTrack.year
            binding.genre.text = status.currentTrack.primaryGenreName
            binding.country.text = status.currentTrack.country
        }

        binding.playButton.setOnClickListener { viewModel.playbackControl() }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.addToPlayList.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.overlay.isVisible = true
        }

        binding.addToFavorites.setOnClickListener {
            if (clickDebounce()) viewModel.addToFavorites()
        }

        binding.arrowBack.setOnClickListener {
            exit()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exit()
                }
            })
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_playerFragment_to_newPlayListFragment
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

    fun exit() {
        viewModel.stopPlayer()
        findNavController().navigateUp()
    }

    override fun onPause() {
        Log.d("TAG", "Paused")
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        Log.d("TAG", "Destroyed")
        super.onDestroy()
        viewModel.stopPlayer()
        navBar.isVisible = true
    }

    override fun onResume() {
        super.onResume()
        navBar.isVisible = false
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L

        enum class PlayerButtons(val button: Int) {
            PLAY(R.drawable.play_button),
            PAUSE(R.drawable.pause_button)
        }
    }
}