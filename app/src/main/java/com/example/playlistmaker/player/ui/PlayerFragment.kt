package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.example.playlistmaker.medialibrary.domain.PlayList
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()
    private var isClickAllowed = true
    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private val playLists = ArrayList<PlayList>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.setListener()
            viewModel.preparePlayer()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.overlay.isVisible = true
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.isVisible = true
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        Unit
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        bottomSheetAdapter = BottomSheetAdapter(playLists) { playList, _ ->
            if (viewModel.addTrackToPlayList(playList)) {
                Snackbar.make(
                    view,
                    getString(R.string.added_to_playlist, playList.title), Snackbar.LENGTH_LONG
                )
                    .show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                Snackbar.make(
                    view,
                    getString(R.string.track_already_in_playlist, playList.title),
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
        }

        binding.playListsRecycler.adapter = bottomSheetAdapter

        viewModel.getPlayLists().observe(viewLifecycleOwner) { lifePlayLists ->
            if (lifePlayLists.isEmpty()) {
                playLists.clear()
                binding.playListsRecycler.isVisible = false
            } else {
                playLists.clear()
                playLists.addAll(lifePlayLists)
                bottomSheetAdapter.notifyDataSetChanged()
                binding.playListsRecycler.isVisible = true
            }
        }

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

        binding.addToPlayList.setOnClickListener {
            viewModel.updatePlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
        viewModel.pausePlayer()
        findNavController().navigateUp()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCurrentTrack()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L

        enum class PlayerButtons(val button: Int) {
            PLAY(R.drawable.play_button),
            PAUSE(R.drawable.pause_button)
        }
    }
}